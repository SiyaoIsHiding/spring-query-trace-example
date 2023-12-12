package tacos;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.metadata.schema.KeyspaceMetadata;

@Configuration
@EnableCassandraRepositories(basePackages = { "tacos" })
@EnableConfigurationProperties(AstraConfig.class)
public class CassandraConfig {

    @Value("${spring.data.cassandra.keyspace-name}")
    public String keyspace;

    @Bean
    KeyspaceMetadata keyspaceMetadata(CqlSession session) {
        return session.getMetadata().getKeyspace(keyspace).get();
    }

    @Bean
    CqlSessionBuilderCustomizer sessionBuilderCustomizer(AstraConfig astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }

    public String getKeyspace() {
        return keyspace;
    }
}