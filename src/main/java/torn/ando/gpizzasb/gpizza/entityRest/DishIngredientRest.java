package torn.ando.gpizzasb.gpizza.entityRest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import torn.ando.gpizzasb.gpizza.entity.Dish;
import torn.ando.gpizzasb.gpizza.entity.Ingredient;
import torn.ando.gpizzasb.gpizza.enums.Unit;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DishIngredientRest {
    private Long id;
    private Long dishId;
    private String name;
    private Unit unit;
    private double requiredQuantity;

    @Override
    public String toString() {
        return "DishIngredientRest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unit=" + unit +
                ", requiredQuantity=" + requiredQuantity +
                '}';
    }
}
