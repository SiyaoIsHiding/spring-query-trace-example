package tacos.data;

import tacos.TacoOrder;

public interface OrderRepository {
    public void save(TacoOrder order);
}
