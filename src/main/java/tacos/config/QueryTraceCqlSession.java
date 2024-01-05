package tacos.config;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.context.DriverContext;
import com.datastax.oss.driver.api.core.cql.ExecutionInfo;
import com.datastax.oss.driver.api.core.cql.QueryTrace;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Statement;
import com.datastax.oss.driver.api.core.metadata.Metadata;
import com.datastax.oss.driver.api.core.metrics.Metrics;
import com.datastax.oss.driver.api.core.session.Request;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

import tacos.data.QueryTraceCache;

public class QueryTraceCqlSession implements CqlSession {

    private final CqlSession delegate;
    private final QueryTraceCache traceCache;

    public QueryTraceCqlSession(CqlSession delegate, QueryTraceCache traceCache) {
        this.delegate = delegate;
        this.traceCache = traceCache;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public Metadata getMetadata() {
        return delegate.getMetadata();
    }

    @Override
    public boolean isSchemaMetadataEnabled() {
        return delegate.isSchemaMetadataEnabled();
    }

    @Override
    public CompletionStage<Metadata> setSchemaMetadataEnabled(Boolean newValue) {
        return delegate.setSchemaMetadataEnabled(newValue);
    }

    @Override
    public CompletionStage<Metadata> refreshSchemaAsync() {
        return delegate.refreshSchemaAsync();
    }

    @Override
    public CompletionStage<Boolean> checkSchemaAgreementAsync() {
        return delegate.checkSchemaAgreementAsync();
    }

    @Override
    public DriverContext getContext() {
        return delegate.getContext();
    }

    @Override
    public Optional<CqlIdentifier> getKeyspace() {
        return delegate.getKeyspace();
    }

    @Override
    public Optional<Metrics> getMetrics() {
        return delegate.getMetrics();
    }

    @Override
    public <RequestT extends Request, ResultT> ResultT execute(RequestT request, GenericType<ResultT> resultType) {
        return delegate.execute(request, resultType);
    }

    @Override
    public ResultSet execute(Statement<?> statement) {
        Statement<?> injected = statement.setTracing(true);
        ResultSet rs = delegate.execute(injected);
        if (injected.isTracing()) {
            ExecutionInfo info = rs.getExecutionInfo();
            QueryTrace queryTrace = info.getQueryTrace();
            traceCache.cache.put(info.getTracingId(), queryTrace);
        }
        return rs;
    }

    @Override
    public CompletionStage<Void> closeFuture() {
        return delegate.closeFuture();
    }

    @Override
    public CompletionStage<Void> closeAsync() {
        return delegate.closeAsync();
    }

    @Override
    public CompletionStage<Void> forceCloseAsync() {
        return delegate.forceCloseAsync();
    }

}
