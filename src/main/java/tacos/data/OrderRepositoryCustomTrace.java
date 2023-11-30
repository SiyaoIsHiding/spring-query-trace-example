package tacos.data;

import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;

import com.datastax.oss.driver.api.core.cql.ResultSet;

import tacos.domain.TacoOrder;

public interface OrderRepositoryCustomTrace{

    public ResultSet saveWithQueryTrace(TacoOrder order);

}
