package tacos.data;

import java.sql.Types;
import java.util.List;

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
import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.metadata.schema.KeyspaceMetadata;
import com.datastax.oss.driver.api.core.type.UserDefinedType;

import lombok.extern.slf4j.Slf4j;

import tacos.TacoOrder;
import tacos.TacoUDT;

@Slf4j
@Repository
public class QueryTraceOrderRepository implements OrderRepository {

    private final CqlSession session;
    private final PreparedStatement saveStatement;
    private final UserDefinedType tacoudt;
    private final UserDefinedType ingredientudt;

    public QueryTraceOrderRepository(CqlSession session) {
        this.session = session;
        this.saveStatement = session.prepare("INSERT INTO orders (id, deliveryname, deliverystreet, deliverycity, " +
                        "deliverystate, deliveryzip, ccnumber, ccexpiration, cccvv, placedat, tacos)" +
                        "values (?,?,?,?,?,?,?,?,?,?,?)");
        this.tacoudt = session.getMetadata().getKeyspace("taco_cloud").get().getUserDefinedType("taco").get();
        this.ingredientudt = session.getMetadata().getKeyspace("taco_cloud").get().getUserDefinedType("ingredient").get();
    }

    @Override
    public TacoOrder save(TacoOrder order) {
        List<UdtValue> tacoUdtValues = order.getTacos().stream().map(taco -> {
            List<UdtValue> ingredienUdtValues = taco.getIngredients().stream().map(ingredient -> {
                UdtValue ingredientUdtValue = ingredientudt.newValue();
                ingredientUdtValue.setString("name", ingredient.getName());
                ingredientUdtValue.setString("type", ingredient.getType().toString());
                return ingredientUdtValue;
            }).collect(java.util.stream.Collectors.toList());
            UdtValue udtValue = tacoudt.newValue();
            udtValue.setString("name", taco.getName());
            udtValue.setList("ingredients", ingredienUdtValues, UdtValue.class);
            return udtValue;
        }).collect(java.util.stream.Collectors.toList());

        BoundStatement statement = this.saveStatement.bind(order.getId(), order.getDeliveryName(), order.getDeliveryStreet(),
                order.getDeliveryCity(), order.getDeliveryState(), order.getDeliveryZip(),
                order.getCcNumber(), order.getCcExpiration(), order.getCcCVV(),
                order.getPlacedAt(), tacoUdtValues).setTracing(true);
        ResultSet rs = session.execute(statement);
        ExecutionInfo info = rs.getExecutionInfo();
        QueryTrace queryTrace = info.getQueryTrace();
        log.info("QueryTraceDurationMicros: {}", queryTrace.getDurationMicros());
        return order;
    }

}
