package tacos.data;

import org.springframework.stereotype.Repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;

import tacos.domain.TacoOrder;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustomTrace {

    private final CqlSession session;
    private PreparedStatement saveStatement;

    public OrderRepositoryImpl(CqlSession session) {
        this.session = session;
    }

    @Override
    public void init() {
        this.saveStatement = session.prepare("INSERT INTO orders (id, deliveryname, deliverystreet, deliverycity, " +
                "deliverystate, deliveryzip, ccnumber, ccexpiration, cccvv, placedat, tacos)" +
                "values (?,?,?,?,?,?,?,?,?,?,?)");
    }

    @Override
    public ResultSet saveWithQueryTrace(TacoOrder order) {
        BoundStatement statement = this.saveStatement
                .bind(order.getId(), order.getDeliveryName(), order.getDeliveryStreet(),
                        order.getDeliveryCity(), order.getDeliveryState(), order.getDeliveryZip(),
                        order.getCcNumber(), order.getCcExpiration(), order.getCcCVV(),
                        order.getPlacedAt(), order.getTacos())
                .setTracing(true);
        return session.execute(statement);
    }

}
