package entityTest;

import ingredient.Ingredient;
import ingredient.IngredientDAO;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
        List<Ingredient> ingredients = IngredientDAO.findAll(strings);
        Assertions.assertEquals(expected,ingredients.get(0).getName());
        Assertions.assertEquals(1,ingredients.size());
    }

    @Test
    void search_for_all_unit_gram(){
        Map<String, Object> strings = new HashMap<>();
        String[] expected = Arrays.asList("Oeuf", "Pain").toArray(new String[0]);
        strings.put("unit", "U");
        strings.put("orderBy_name",false);
        List<Ingredient> ingredients = IngredientDAO.findAll(strings);
        Assertions.assertEquals(expected.length,ingredients.size());
        Assertions.assertEquals(expected[0],ingredients.get(1).getName());
        Assertions.assertEquals(expected[1],ingredients.get(0).getName());
    }

    @Test
    void verify_available_quantity_of_ingredient(){
        IngredientDAO ingredientDAO = new IngredientDAO();
        List<Ingredient> ingredients = ingredientDAO.findAll(new HashMap<>());
        Optional<Ingredient> huile = ingredients.stream().filter(ingredient -> ingredient.getName().equals("Huile")).findFirst();
        Optional<Ingredient> saucisse = ingredients.stream().filter(ingredient -> ingredient.getName().equals("Saucisse")).findFirst();
        Optional<Ingredient> pain = ingredients.stream().filter(ingredient -> ingredient.getName().equals("Pain")).findFirst();
        Optional<Ingredient> oeuf = ingredients.stream().filter(ingredient -> ingredient.getName().equals("Oeuf")).findFirst();

        Assertions.assertEquals(20.0, huile.get().getAvailableQuantity());
        Assertions.assertEquals(10000, saucisse.get().getAvailableQuantity());
        Assertions.assertEquals(30,pain.get().getAvailableQuantity());
        Assertions.assertEquals(80,oeuf.get().getAvailableQuantity());
    }

    @Test
    void test_available_quantity_of_ingredient_without_call_stockDAO(){
        IngredientDAO ingredientDAO = new IngredientDAO();
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("ingredient_name", "Huile");
        List<Ingredient> ingredients = ingredientDAO.findAll(criteria);
        Assertions.assertNotNull(ingredients);
        Assertions.assertEquals(1,ingredients.size());
        Assertions.assertEquals("Huile",ingredients.get(0).getName());
        Assertions.assertEquals(20.0,ingredients.get(0).getAvailableQuantity());
    }


    @Test
    void find_by_name(){
        String expected = "Riz";
        IngredientDAO ingredientDAO = new IngredientDAO();
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("ingredient_name", "ri");

        Ingredient riz = ingredientDAO.findByName(criteria);
        Assertions.assertNotNull(riz);
        System.out.println(riz);
        Assertions.assertEquals(expected,riz.getName());

        Assertions.assertEquals(0,riz.getAvailableQuantity());
    }

}
