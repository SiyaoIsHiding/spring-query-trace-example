package tacos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;

@Configuration
@EnableCassandraRepositories(basePackages = { "tacos" })
public class CassandraConfig {

    // bean for session
    @Bean
    CqlSession session() {
        CqlSessionBuilder builder = CqlSession.builder().withKeyspace("taco_cloud")
                .withLocalDatacenter("datacenter1");
        return builder.build();
    }
}