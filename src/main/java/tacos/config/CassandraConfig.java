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
    public String keyspace;

    @Value("${spring.data.cassandra.local-datacenter}")
    public String localDatacenter;

    @Bean
    CqlSession cassandraSession(QueryTraceCache traceCache) {
        CqlSession delegate = CqlSession.builder().withKeyspace(keyspace)
        .withLocalDatacenter(localDatacenter).build();
        return new QueryTraceCqlSession(delegate, traceCache);
    }

}