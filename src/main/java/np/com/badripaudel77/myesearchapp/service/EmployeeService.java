package np.com.badripaudel77.myesearchapp.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import np.com.badripaudel77.myesearchapp.repository.ElasticRepository;
import np.com.badripaudel77.myesearchapp.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class EmployeeService {
    // Not using because of version mismatch of java client and elastic search.
    // Cause this project has java client and spring elasticsearch data
    // client version is not supported in spring and downgrading client version
    // causes the breaking changes in code. But the way of implementing
    // with repository is same as JPA.
     private final ElasticRepository repository;
     private final ElasticsearchOperations esOperation;
     private final ElasticsearchClient esClient;

     @Autowired
     public EmployeeService(ElasticsearchOperations esOperation, ElasticsearchClient esClient, ElasticRepository repository) {
        this.esOperation = esOperation;
        this.repository = repository;
        this.esClient = esClient;
     }

    public Employee addEmployee(Employee employee) {
        Employee savedEmployee = this.esOperation.save(employee);
        return savedEmployee;
    }

    public Employee getEmployeeById(String id) {
        Employee employee = esOperation.get(id, Employee.class);
        return employee;
    }

    public List<Employee> getAllEmployees() throws IOException {
        SearchResponse<Employee> allHits = esClient.search(s ->
                s.index("employees")
                        .query(q ->
                                q.matchAll(m -> m)
                        ), Employee.class
        );
        List<Employee> employees = new ArrayList<>();

        allHits.hits().hits().forEach(e ->
                employees.add(e.source())
        );
        return employees;
    }

    public List<Employee> getAllEmployeesInRange() throws IOException {
        Query byLat = RangeQuery.of(r -> r
                        .number(n -> n
                                .field("address.location.lat")
                                .lte(37.7749)
                        ))._toQuery();
        Query byLong = RangeQuery.of(r -> r
                        .number(n -> n
                                .field("address.location.lon")
                                .gte(-122.4194)
                        ))._toQuery();

        SearchResponse<Employee> searchResponse = esClient.search(s ->
                s.index("employees")
                        .query(q ->
                                q.bool(b -> b.must(byLat)
                                        .must(byLong)
                                )
                        ), Employee.class);
        List<Employee> employees = new ArrayList<>();
        searchResponse
                .hits()
                .hits()
                .forEach(employeeHit -> employees.add(employeeHit.source()));
        return employees;
    }

    public List<Employee> getAllEmployeeInRangeDistance(Double lat, Double lon, String distance) throws IOException {
        double lt = lat != null ? lat: 37.7749;
        double ln = lon != null ? lon : -122.4194;
        String dist =  distance != null ? distance: "1200km";

        GeoLocation location = new GeoLocation.Builder()
                .latlon(l -> l.lat(lt).lon(ln))
                .build();

        Query geoQuery = GeoDistanceQuery.of(g -> g
                .field("address.location")
                .distance(dist)
                .location(location)
        )._toQuery();

        SearchResponse<Employee> response = esClient.search(s -> s
                        .index("employees")
                        .query(geoQuery),
                Employee.class
        );
        List<Employee> employees = new ArrayList<>();
        for (Hit<Employee> hit : response.hits().hits()) {
            employees.add(hit.source());
        }

        return employees;
    }
}
