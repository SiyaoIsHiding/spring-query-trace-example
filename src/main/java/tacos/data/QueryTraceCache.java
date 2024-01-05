package tacos.data;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.cql.QueryTrace;
import com.datastax.oss.driver.shaded.guava.common.cache.Cache;
import com.datastax.oss.driver.shaded.guava.common.cache.CacheBuilder;

@Component
public class QueryTraceCache {
    public Cache<UUID, QueryTrace> cache = CacheBuilder.newBuilder().maximumSize(100).build();
    
}
