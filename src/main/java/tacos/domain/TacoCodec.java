package tacos.domain;
import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

public class TacoCodec extends MappingCodec<UdtValue, TacoUDT>{

    private final UserDefinedType tacoUDT;

    public TacoCodec(UserDefinedType tacoUDT, TypeCodec<UdtValue> innerCodec) {
        super(innerCodec, GenericType.of(TacoUDT.class));
        this.tacoUDT = tacoUDT;
    }

    @Override
    protected TacoUDT innerToOuter(UdtValue value) {
        TacoUDT taco = new TacoUDT(value.getString("name"), value.getList("ingredients", IngredientUDT.class));
        return taco;
    }

    @Override
    protected UdtValue outerToInner(TacoUDT value) {
        UdtValue udtValue = tacoUDT.newValue();
        udtValue.setString("name", value.getName());
        udtValue.setList("ingredients", value.getIngredients(), IngredientUDT.class);
        return udtValue;
    }

}