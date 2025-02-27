package dish;

import ingredient.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Dish {
    private int id;
    private String name;
    private double selling_price;
    private Map<Ingredient, Double> ingredients;

    public Dish(String name, double selling_price, Map<Ingredient, Double> ingredients) {
        this.name = name;
        this.selling_price = selling_price;
        this.ingredients = ingredients;
    }

    public double productionPrice(){
        return ingredients
                .entrySet()
                .stream()
                .mapToDouble(entry -> entry.getKey().getIngredientCost() * entry.getValue())
                .sum();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Dish dish = (Dish) object;
        return id == dish.id && Double.compare(selling_price, dish.selling_price) == 0 && Objects.equals(name, dish.name) && Objects.equals(ingredients, dish.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, selling_price, ingredients);
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", selling_price=" + selling_price +
                ", ingredients=" + ingredients +
                '}';
    }

    public Double getGrossMargin(){
        return this.selling_price - this.productionPrice();
    }
}
