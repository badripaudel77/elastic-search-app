package np.com.badripaudel77.normal_java;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import java.io.IOException;
import java.util.Map;

// Using Pure Java Client (low level API)
// Using No framework
// For framework or combination, see the main package.
public class TestJavaESClient {
    static void main() throws IOException {
        String searchTerm = "Fairfield";

        ElasticsearchClient esClient = ElasticsearchClient.of(b -> b
                .host("http://localhost:9200")
        );

        // TODO: Uncomment if need to test.
        SearchResponse<Map> search1 = esClient.search(
                s -> s.index("users")
                        .query(q -> q
                                .term(t -> t
                                        .field("city.keyword").value(v -> v.stringValue(searchTerm)))),
                Map.class
        );

        SearchResponse<Map> search2 = esClient.search( s -> s.index("users")
                        .query(q ->
                             q.bool(
                                     b ->
                                             b.must(m -> m.term(t -> t.field("city").value(searchTerm)))
                             )
              ), Map.class
        );

        // Search by age range
        Query byAge = RangeQuery.of(r -> r
                .number(n -> n
                        .field("age")
                        .gt(25.0)
                        .lte(26.0))
        )._toQuery();

        SearchResponse<Map> search3 = esClient.search(
                s -> s.index("users")
                        .query(q -> q.bool(bq -> bq
                                        .must(m -> m.term(t -> t.field("city.keyword").value(v -> v.stringValue("Fairfield"))))
                                        .must(byAge)
                                .must(m -> m.match(ma -> ma.field("name").query("Paudel")))
                                        .should(sh -> sh.wildcard(w -> w.field("name.keyword").value("A*")))
                                        .should(sh -> sh.prefix(p -> p.field("name.keyword").value("Olivia")))
                                        .mustNot(mn -> mn.term(t -> t.field("name.keyword").value(v -> v.stringValue("Noah Johnson"))))
                        )),
                Map.class
        );

        System.out.println("printing ... " + search3);
        for (Hit<Map> hit : search3.hits().hits()) {
            System.out.println(hit.source());
        }
    }
}
