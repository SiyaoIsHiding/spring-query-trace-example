package tacos.data;

import java.sql.Types;

import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.cql.CqlOperations;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.stereotype.Repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ExecutionInfo;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.QueryTrace;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Statement;

import lombok.extern.slf4j.Slf4j;

import tacos.TacoOrder;

@Slf4j
@Repository
public class QueryTraceOrderRepository implements OrderRepository {

    private final CqlSession session;
    private final PreparedStatement saveStatement;

    public QueryTraceOrderRepository(CqlSession session) {
        this.session = session;
        this.saveStatement = session.prepare("INSERT INTO orders (id, deliveryname, deliverystreet, deliverycity, " +
                        "deliverystate, deliveryzip, ccnumber, ccexpiration, cccvv, placedat)" +
                        "values (?,?,?,?,?,?,?,?,?,?)");
    }

    @Override
    public void save(TacoOrder order) {
        BoundStatement statement = this.saveStatement.bind(order.getId(), order.getDeliveryName(), order.getDeliveryStreet(),
                order.getDeliveryCity(), order.getDeliveryState(), order.getDeliveryZip(),
                order.getCcNumber(), order.getCcExpiration(), order.getCcCVV(),
                order.getPlacedAt()).setTracing(true);
        ResultSet rs = session.execute(statement);
        ExecutionInfo info = rs.getExecutionInfo();
        QueryTrace queryTrace = info.getQueryTrace();
        log.info("QueryTraceDurationMicros: {}", queryTrace.getDurationMicros());
    }

}
