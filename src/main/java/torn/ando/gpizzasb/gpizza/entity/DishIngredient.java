package torn.ando.gpizzasb.gpizza.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import torn.ando.gpizzasb.gpizza.enums.Unit;

@Getter
@Setter
@NoArgsConstructor
@Component
public class DishIngredient {
    @JsonIgnore
    private Dish dish;
    private Ingredient ingredient;
    private Unit unit;
    private double requiredQuantity;

    @Override
    public String toString() {
        return "DishIngredient{" +
                "dish=" + dish +
                ", ingredient=" + ingredient +
                ", unit=" + unit +
                ", requiredQuantity=" + requiredQuantity +
                '}';
    }
}
