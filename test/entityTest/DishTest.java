package entityTest;

import dish.Dish;
import dish.DishDAO;
import ingredient.Ingredient;
import ingredientdish.IngredientDish;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import unit.Unit;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class DishTest {

//    @Test
//    void verify_with_data_i_provided(){
//        Ingredient saucisse = new Ingredient(1,"saucisse",(LocalDateTime.now()),20,Unit.G);
//        Ingredient huile = new Ingredient(2,"huile",(LocalDateTime.now()),10000,Unit.L);
//        Ingredient oeuf = new Ingredient(3,"oeuf",(LocalDateTime.now()),1000,Unit.U);
//        Ingredient pain = new Ingredient(4,"pain",(LocalDateTime.now()),1000,Unit.U);
//        Map<Timestamp, Double> huilepriceAt = new HashMap<>();
//        huilepriceAt.put(Timestamp.valueOf(LocalDateTime.now().plusDays(1)),10000.0);
//        huilepriceAt.put(Timestamp.valueOf(LocalDateTime.now().plusDays(2)),12000.0);
//        huilepriceAt.put(Timestamp.valueOf(LocalDateTime.now().plusDays(3)),11000.0);
//        huile.setPrice_at((HashMap<Timestamp, Double>) huilepriceAt);
//
//        Map<Timestamp, Double> oeufriceAt = new HashMap<>();
//        oeufriceAt.put(Timestamp.valueOf(LocalDateTime.now().plusDays(1)),1000.0);
//        oeufriceAt.put(Timestamp.valueOf(LocalDateTime.now().plusDays(2)),1500.0);
//        oeufriceAt.put(Timestamp.valueOf(LocalDateTime.now().plusDays(3)),1100.0);
//        oeuf.setPrice_at((HashMap<Timestamp, Double>) huilepriceAt);
//
//        Map<Timestamp, Double> saucisseAt = new HashMap<>();
//        saucisseAt.put(Timestamp.valueOf(LocalDateTime.now().plusDays(1)),300.0);
//        saucisseAt.put(Timestamp.valueOf(LocalDateTime.now().plusDays(2)),200.0);
//        saucisseAt.put(Timestamp.valueOf(LocalDateTime.now().plusDays(3)),320.0);
//        saucisse.setPrice_at((HashMap<Timestamp, Double>) huilepriceAt);
//
//        Map<Timestamp, Double> painAt = new HashMap<>();
//        painAt.put(Timestamp.valueOf(LocalDateTime.now().plusDays(1)),10000.0);
//        painAt.put(Timestamp.valueOf(LocalDateTime.now().plusDays(2)),12000.0);
//        painAt.put(Timestamp.valueOf(LocalDateTime.now().plusDays(3)),11000.0);
//        pain.setPrice_at((HashMap<Timestamp, Double>) huilepriceAt);
//
//       List<IngredientDish> hotdog_ingredients = new ArrayList<>();
//        hotdog_ingredients.put(saucisse,100.0);
//        hotdog_ingredients.put(huile,0.15);
//        hotdog_ingredients.put(oeuf,1.0);
//        hotdog_ingredients.put(pain,1.0);
//
//        Dish subject = new Dish(1,"hotDog",15000,hotdog_ingredients);
//        Double expected = 5500.0;
//
//
//        Assertions.assertEquals(expected,subject.productionPrice());
//    }

    @Test
    void test_if_result_match_with_the_database(){
        Double expected = 5500.0;
        DishDAO dishDAO = new DishDAO();
        Map<String,Object> options = new HashMap<>();
        options.put("date",Arrays.asList(
                LocalDateTime.of(2024,12,31,0,0),
                LocalDateTime.of(2025,1,1,0,0)
//                LocalDateTime.of(2022,12,31,0,0),
//                LocalDateTime.of(2023,1,1,0,0)
        ));
        options.put("dish_name","Hot dog");
        Dish hotdog = dishDAO.findByName(options);
        Map<String,Object> options2 = new HashMap<>();
        options2.put("dish_name","Hot dog");
        Dish hotdog2 = dishDAO.findByName(options2);
        System.out.println(hotdog2);
        Assertions.assertEquals(expected,hotdog.productionPrice());
        //Assertions.assertEquals(0.0,hotdog.getIngredientsCost());
       // Assertions.assertEquals(0.0,hotdog.getGrossMargin());
        Assertions.assertEquals(expected,hotdog2.productionPrice());
        Assertions.assertEquals(9500.0,hotdog.getGrossMargin());
    }

    @Test
    void availableQuantity(){
        Integer expected = 30;
        DishDAO dishDAO = new DishDAO();
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("dish_name","hot");
        Dish subject = dishDAO.findByName(criteria);
        Assertions.assertEquals(expected,subject.getAvailableQuantity());
    }

    @Test
    void numberOfDishWeCanDeliver(){
        DishDAO dishDAO = new DishDAO();
        Map<String,Object> criteria = new HashMap<>();
        criteria.put("dish_name","hot");
        Dish hotdog = dishDAO.findByName(criteria);
        Assertions.assertEquals("HOT dog",hotdog.getName());
        System.out.println(hotdog.getAvailableQuantity());
    }

}
