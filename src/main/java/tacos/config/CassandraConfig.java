package tacos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.datastax.oss.driver.api.core.CqlSession;

import tacos.data.QueryTraceCache;

@Configuration
@EnableCassandraRepositories(basePackages = { "tacos" })
public class CassandraConfig  {

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspace;

    @Value("${spring.data.cassandra.local-datacenter}")
    private String localDatacenter;

    @Value("${spring.data.cassandra.username}")
    private String username;

    @Value("${spring.data.cassandra.Password}")
    private String password;


    @Bean
    CqlSession cassandraSession(QueryTraceCache traceCache, AstraConfig astraProperties) {
        CqlSession delegate = CqlSession.builder().withKeyspace(keyspace)
        .withLocalDatacenter(localDatacenter)
        .withAuthCredentials(username, password)
        .withCloudSecureConnectBundle(astraProperties.getSecureConnectBundle().toPath()).build();
        return new QueryTraceCqlSession(delegate, traceCache);
    }

}