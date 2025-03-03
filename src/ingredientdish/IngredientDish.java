package ingredientdish;

import dish.Dish;
import ingredient.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IngredientDish {
    Dish dish;
    Ingredient ingredients;
    Double requiredQuantity;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        IngredientDish that = (IngredientDish) object;
        return Objects.equals(requiredQuantity, that.requiredQuantity);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(requiredQuantity);
    }

    @Override
    public String toString() {
        return "IngredientDish{" +
                "requiredQuantity=" + requiredQuantity +
                '}';
    }
}
