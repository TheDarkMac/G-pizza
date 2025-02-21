package entityTest;

import dish.Dish;
import dish.DishDAO;
import ingredient.Ingredient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.Unit;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DishTest {

    @Test
    void verify_with_the_data_provided(){
        Ingredient saucisse = new Ingredient(1,"saucisse",Timestamp.valueOf(LocalDateTime.now()),20,Unit.G);
        Ingredient huile = new Ingredient(2,"huile",Timestamp.valueOf(LocalDateTime.now()),10000,Unit.L);
        Ingredient oeuf = new Ingredient(3,"oeuf",Timestamp.valueOf(LocalDateTime.now()),1000,Unit.U);
        Ingredient pain = new Ingredient(4,"pain",Timestamp.valueOf(LocalDateTime.now()),1000,Unit.U);
        Map<Ingredient,Double> hotdog_ingredients = new HashMap<>();
        hotdog_ingredients.put(saucisse,100.0);
        hotdog_ingredients.put(huile,0.15);
        hotdog_ingredients.put(oeuf,1.0);
        hotdog_ingredients.put(pain,1.0);

        Dish subject = new Dish(1,"hotDog",15000,hotdog_ingredients);
        Double expected = 5500.0;

        Assertions.assertEquals(expected,subject.productionPrice());
    }

    @Test
    void test_result_with_data_on_database(){
        Double expected = 5500.0;
        DishDAO dishDAO = new DishDAO();
        Dish hotdog = dishDAO.findByName("HOT");
        Assertions.assertEquals(expected,hotdog.productionPrice());
    }

}
