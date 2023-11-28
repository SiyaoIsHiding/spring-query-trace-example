package tacos.data;

import tacos.TacoOrder;

public interface OrderRepository {
    public TacoOrder save(TacoOrder order);
}
