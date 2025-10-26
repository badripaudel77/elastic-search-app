package np.com.badripaudel77.myesearchapp.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {
    @Value("${es.app.elasticsearch.host:localhost}")
    private String host;

    @Value("${es.app.elasticsearch.port:9200}")
    private Integer port;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        // Use this ObjectMapper in the Elasticsearch client
        JacksonJsonpMapper jsonpMapper = new JacksonJsonpMapper(mapper);
        RestClient restClient = RestClient.builder(new HttpHost(host, port)).build();
        RestClientTransport transport = new RestClientTransport(restClient, jsonpMapper);

        ElasticsearchClient esClient = new ElasticsearchClient(transport);
        return esClient;
    }

}
