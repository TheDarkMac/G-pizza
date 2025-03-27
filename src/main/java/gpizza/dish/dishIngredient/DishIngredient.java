package gpizza.dish.dishIngredient;

import gpizza.dish.Dish;
import gpizza.ingredient.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DishIngredient implements Serializable {
    private long id;
    private Dish dish;
    private Ingredient ingredient;
    private double quantity;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        DishIngredient that = (DishIngredient) object;
        return id == that.id && Double.compare(quantity, that.quantity) == 0 && Objects.equals(dish, that.dish) && Objects.equals(ingredient, that.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dish, ingredient, quantity);
    }

    @Override
    public String toString() {
        return "DishIngredient{" +
                "id=" + id +
                ", dish=" + dish +
                ", ingredient=" + ingredient+
                ", quantity=" + quantity +
                '}';
    }
}
