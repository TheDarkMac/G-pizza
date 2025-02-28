package entityTest;

import ingredient.Ingredient;
import ingredient.IngredientDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stock.Stock;
import stock.StockDAO;
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
        List<Ingredient> ingredients = IngredientDAO.findAll(strings);
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
        List<Ingredient> ingredients = IngredientDAO.findAll(strings);
        Assertions.assertEquals(expected.length,ingredients.size());
        Assertions.assertEquals(expected[0],ingredients.get(1).getName());
        Assertions.assertEquals(expected[1],ingredients.get(0).getName());
    }

    @Test
    void verify_available_quantity_of_ingredient(){
        IngredientDAO ingredientDAO = new IngredientDAO();
        List<Ingredient> ingredients = ingredientDAO.findAll(new HashMap<>());
        StockDAO stockDAO = new StockDAO();
        Map<String, Object> criteria = new HashMap<>();
        List<Stock> stock = stockDAO.getStockOf(criteria);
        Optional<Stock> stockHuile = stock.stream().filter(stock1 -> {
            return stock1.getIngredients().getName().equals("Huile");
        }).findFirst();
        Optional<Ingredient> huile = ingredients.stream().filter(ingredient -> ingredient.getName().equals("Huile")).findFirst();
        Optional<Ingredient> saucisse = ingredients.stream().filter(ingredient -> ingredient.getName().equals("Saucisse")).findFirst();

        Assertions.assertTrue(huile.isPresent());
        Assertions.assertEquals(20.0, huile.get().getAvailableQuantity());
        Assertions.assertEquals(10000, saucisse.get().getAvailableQuantity());
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
    void first_insertion(){
        IngredientDAO ingredientDAO = new IngredientDAO();
        Ingredient riz = new Ingredient();
        riz.setIngredientCost(3.5);
        riz.setName("riz");
        riz.setUnit(Unit.G);
        boolean r = ingredientDAO.create(riz);
        Ingredient sel = new Ingredient("sel",2.5,Unit.G);
        boolean s = ingredientDAO.create(sel);
        Assertions.assertTrue(r);
        Assertions.assertTrue(s);
    }

    @Test
    void feind_by_name(){
        IngredientDAO ingredientDAO = new IngredientDAO();
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("ingredient_name", "ri");
//        criteria.put("fields",Arrays.asList("ingredient.name AS ingredient_name",
//                "unit",
//                "ingredient.id_ingredient"));

        Ingredient riz = ingredientDAO.findByName(criteria);
        Assertions.assertNotNull(riz);
        System.out.println(riz);
        Assertions.assertEquals("riz",riz.getName());
    }

    @Test
    void reinsertion(){
        IngredientDAO ingredientDAO = new IngredientDAO();
        Ingredient riz = new Ingredient();
        riz.setIngredientCost(3.5);
        riz.setName("riz");
        riz.setUnit(Unit.G);
        Assertions.assertThrows(RuntimeException.class,()->ingredientDAO.create(riz));
        Ingredient sel = new Ingredient("sel",2.5,Unit.G);
        Assertions.assertThrows(RuntimeException.class,()->ingredientDAO.create(sel));
    }
}
