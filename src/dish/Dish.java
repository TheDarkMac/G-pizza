package dish;

import ingredient.Ingredient;
import ingredientdish.IngredientDish;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stock.Stock;
import stock.StockDAO;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Dish {
    private int id;
    private String name;
    private double selling_price;
    private List<IngredientDish> ingredients;

    public Dish(String name, double selling_price, List<IngredientDish> ingredients) {
        this.name = name;
        this.selling_price = selling_price;
        this.ingredients = ingredients;
    }

    public double productionPrice(){
        return ingredients
                .stream()
                .mapToDouble(entry -> entry.getIngredients().getIngredientCost() * entry.getRequiredQuantity())
                .sum();
    }

    public double getIngredientsCost(){
        throw new RuntimeException("Not implemented yet");
    }

    public Integer getAvailableQuantity(){
        List<Integer> makes_number = new ArrayList<>();
        ingredients.forEach(entry -> {
            double quantity = entry.getRequiredQuantity();
            String ingredient_name = entry.getIngredients().getName();
            StockDAO stockDAO = new StockDAO();
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("ingredient_name",ingredient_name);
            List<Stock> stock = stockDAO.getStockOf(criteria);
            System.out.println(stock);
            int make_number = (int) (stock.get(0).getQuantity() / quantity);
            makes_number.add(make_number);
        });
        Optional<Integer> result =  makes_number.stream().min(Integer::compareTo);
        return result.orElse(0);
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
