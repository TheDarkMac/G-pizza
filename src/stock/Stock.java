package stock;

import ingredient.Ingredient;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    Map<Ingredient, Double> stock = new HashMap<>();
}
