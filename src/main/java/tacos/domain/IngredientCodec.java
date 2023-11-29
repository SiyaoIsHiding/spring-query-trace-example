package tacos.domain;

import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

public class IngredientCodec extends MappingCodec<UdtValue, IngredientUDT>{

    private UserDefinedType ingredientUdt;

    public IngredientCodec(UserDefinedType ingredientUdt, TypeCodec<UdtValue> innerCodec) {
        super(innerCodec, GenericType.of(IngredientUDT.class));
        this.ingredientUdt = ingredientUdt;
    }

    @Override
    protected IngredientUDT innerToOuter(UdtValue value) {
        IngredientUDT ingredientUDT = new IngredientUDT(value.getString("name"), 
            Ingredient.Type.valueOf(value.getString("type")));
        return ingredientUDT;
    }

    @Override
    protected UdtValue outerToInner(IngredientUDT value) {
        UdtValue ingredientUdtValue = ingredientUdt.newValue();
        ingredientUdtValue.setString("name", value.getName());
        ingredientUdtValue.setString("type", value.getType().toString());
        return ingredientUdtValue;
    }

    
}
