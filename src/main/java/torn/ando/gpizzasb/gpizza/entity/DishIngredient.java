package torn.ando.gpizzasb.gpizza.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class DishIngredient {
    private long id;
    private Dish dish;
    private Ingredient ingredient;
    private double requiredQuantity;

    @Override
    public String toString() {
        return "DishIngredient{" +
                "id=" + id +
                ", ingredient=" + ingredient +
                ", requiredQuantity=" + requiredQuantity +
                '}';
    }
}
