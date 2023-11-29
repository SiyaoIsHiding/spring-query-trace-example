package tacos.data;

import com.datastax.oss.driver.api.core.cql.ResultSet;

import tacos.domain.TacoOrder;

public interface OrderRepository {
    public ResultSet save(TacoOrder order);
}
