package tacos;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.metadata.schema.KeyspaceMetadata;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.datastax.oss.driver.api.core.type.codec.registry.MutableCodecRegistry;

import tacos.domain.IngredientCodec;
import tacos.domain.TacoCodec;

@Configuration
@EnableCassandraRepositories(basePackages = { "tacos" })
public class CassandraConfig extends AbstractCassandraConfiguration {

    public static final String KEYSPACE = "taco_cloud";

    @Bean
    KeyspaceMetadata keyspaceMetadata(CqlSession session) {
        return session.getMetadata().getKeyspace(KEYSPACE).get();
    }

    @PostConstruct
    void registerCodec(){
        CqlSession session = getRequiredSession();
        UserDefinedType tacoUDT = session.getMetadata().getKeyspace(CassandraConfig.KEYSPACE).get().getUserDefinedType("taco").get();
        UserDefinedType ingredientUDT = session.getMetadata().getKeyspace(CassandraConfig.KEYSPACE).get().getUserDefinedType("ingredient").get();
        
        MutableCodecRegistry codecRegistry = (MutableCodecRegistry) session.getContext().getCodecRegistry();
        
        TacoCodec tacoCodec = new TacoCodec(tacoUDT, codecRegistry.codecFor(tacoUDT));
        codecRegistry.register(tacoCodec);

        IngredientCodec ingredientCodec = new IngredientCodec(ingredientUDT, codecRegistry.codecFor(ingredientUDT));
        codecRegistry.register(ingredientCodec);
    }

    @Override
    protected String getKeyspaceName() {
        return KEYSPACE;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

}