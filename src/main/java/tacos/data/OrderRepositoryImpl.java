package tacos.data;

import java.sql.Types;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.cql.CqlOperations;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ExecutionInfo;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.QueryTrace;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Statement;
import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.metadata.schema.KeyspaceMetadata;
import com.datastax.oss.driver.api.core.type.UserDefinedType;

import lombok.extern.slf4j.Slf4j;
import tacos.domain.TacoOrder;
import tacos.domain.TacoUDT;

@Slf4j
@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustomTrace {

    private final CqlSession session;
    private PreparedStatement saveStatement;

    public OrderRepositoryImpl(CqlSession session) {
        this.session = session;
    }

    // public void postConstruct() {
    //     this.saveStatement = session.prepare("INSERT INTO orders (id, deliveryname, deliverystreet, deliverycity, " +
    //                     "deliverystate, deliveryzip, ccnumber, ccexpiration, cccvv, placedat, tacos)" +
    //                     "values (?,?,?,?,?,?,?,?,?,?,?)");
    // }

    @Override
    public ResultSet saveWithQueryTrace(TacoOrder order) {
        BoundStatement statement = this.saveStatement.bind(order.getId(), order.getDeliveryName(), order.getDeliveryStreet(),
                order.getDeliveryCity(), order.getDeliveryState(), order.getDeliveryZip(),
                order.getCcNumber(), order.getCcExpiration(), order.getCcCVV(),
                order.getPlacedAt(), order.getTacos()).setTracing(true);
        return session.execute(statement);
    }

}
