package torn.ando.gpizzasb.gpizza.entity;

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
    private Dish dish;
    private Ingredient ingredient;
    private Unit unit;
    private double requiredQuantity;
}
