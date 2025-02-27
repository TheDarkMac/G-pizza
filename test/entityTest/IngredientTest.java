package entityTest;

import ingredient.Ingredient;
import ingredient.IngredientDAO;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngredientTest {
    @Test
    void finallIngredient(){
        Map<String, Object> strings = new HashMap<>();
        strings.put("ingredient_name","huile");
        List<Ingredient> ingredients = IngredientDAO.findAll(strings,2,1);
        System.out.println(ingredients);
    }
}
