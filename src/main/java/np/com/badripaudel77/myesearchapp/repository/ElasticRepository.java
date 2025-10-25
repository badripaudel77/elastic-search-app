package np.com.badripaudel77.myesearchapp.repository;

import np.com.badripaudel77.myesearchapp.model.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticRepository extends ElasticsearchRepository<Employee, String> {

}
