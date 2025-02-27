package entityTest;

import ingredient.Ingredient;
import ingredient.IngredientDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import unit.Unit;

import java.time.LocalDateTime;
import java.util.*;

public class IngredientTest {
    @Test
    void search_for_egg(){
        Map<String, Object> strings = new HashMap<>();
        String expected = "Oeuf";
        strings.put("unit", "U");
        strings.put("ingredient_name", "oe");
        strings.put("updatedAt", Arrays.asList(LocalDateTime.of(2024,12,31,0,0,0),LocalDateTime.of(2025,1,5,0,0,0)));
        strings.put("unit_price", Arrays.asList(800,1200));
        strings.put("orderBy_name","DESC");
        List<Ingredient> ingredients = IngredientDAO.findAll(strings,2,1);
        ingredients.forEach(ingredient -> {
            System.out.println(ingredient);
        });
        Assertions.assertEquals(expected,ingredients.get(0).getName());
        Assertions.assertEquals(1,ingredients.size());
    }

    @Test
    void search_for_all_unit_gram(){
        Map<String, Object> strings = new HashMap<>();
        String[] expected = Arrays.asList("Oeuf", "Pain").toArray(new String[0]);
        strings.put("unit", "U");
        strings.put("orderBy_name",false);
        List<Ingredient> ingredients = IngredientDAO.findAll(strings,2,1);
        Assertions.assertEquals(expected.length,ingredients.size());
        Assertions.assertEquals(expected[0],ingredients.get(1).getName());
        Assertions.assertEquals(expected[1],ingredients.get(0).getName());
    }
}
