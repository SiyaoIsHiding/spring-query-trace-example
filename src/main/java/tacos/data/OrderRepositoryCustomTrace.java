package tacos.data;

import com.datastax.oss.driver.api.core.cql.ResultSet;

import tacos.domain.TacoOrder;

public interface OrderRepositoryCustomTrace{

    public ResultSet saveWithQueryTrace(TacoOrder order);
    public void init();

}
