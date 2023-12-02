package tacos.data;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import tacos.domain.TacoOrder;

public interface OrderRepository extends CrudRepository<TacoOrder, UUID>, OrderRepositoryCustomTrace {

}
