package DAOTest;

import lombok.AllArgsConstructor;
import torn.ando.gpizzasb.gpizza.dao.IngredientDAO;
import torn.ando.gpizzasb.gpizza.dao.IngredientPriceDAO;
import torn.ando.gpizzasb.gpizza.entity.Ingredient;
import torn.ando.gpizzasb.gpizza.entity.IngredientPrice;
import torn.ando.gpizzasb.gpizza.enums.Unit;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

public class IngredientTest {

    private IngredientDAO ingredientDAO;

    private IngredientPriceDAO ingredientPriceDAO;

    @Test
    public void addIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(20);
        ingredient.setName("voanjo");

        List<Ingredient> i = ingredientDAO.saveAll(List.of(ingredient));
        System.out.println(i);
    }

    @Test
    public void addPrice(){
        Ingredient ingredient = ingredientDAO.findById(1);
        IngredientPrice ingredientPrice = new IngredientPrice();
        ingredientPrice.setId(7);
        ingredientPrice.setPrice(35.6);
        ingredientPrice.setDateValue(LocalDateTime.now());
        ingredientPrice.setIngredient(ingredient);
        ingredientPriceDAO.saveAll(List.of(ingredientPrice));
        System.out.println(ingredient);
    }

    @Test
    public void saucisse(){
        Ingredient ingredient = ingredientDAO.findById(1);
        System.out.println(ingredient.getPrices());
    }
}
