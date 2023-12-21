package tacos.data;

import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.cql.TraceEvent;
import com.datastax.oss.driver.shaded.guava.common.collect.EvictingQueue;

@Component
public class QueryTraceCache {
    public EvictingQueue<TraceEvent> cache = EvictingQueue.create(100);
    
}
