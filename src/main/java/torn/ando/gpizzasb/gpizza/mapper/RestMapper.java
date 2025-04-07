package torn.ando.gpizzasb.gpizza.mapper;

import org.springframework.stereotype.Component;
import torn.ando.gpizzasb.gpizza.entity.Ingredient;
import torn.ando.gpizzasb.gpizza.entity.IngredientPrice;
import torn.ando.gpizzasb.gpizza.entity.Stock;
import torn.ando.gpizzasb.gpizza.entityRest.IngredientPriceRest;
import torn.ando.gpizzasb.gpizza.entityRest.IngredientRest;
import torn.ando.gpizzasb.gpizza.entityRest.StockRest;

@Component
public class RestMapper {

    public Ingredient mapToIngredient(Rest rest) {
        IngredientRest ingredientRest = (IngredientRest) rest;
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientRest.getId());
        ingredient.setName(ingredientRest.getName());
        return ingredient;
    }

    public IngredientPrice mapToIngredientPrice(Rest rest) {
        IngredientPriceRest ingredientPriceRest = (IngredientPriceRest) rest;
        IngredientPrice ingredientPrice = new IngredientPrice();
        ingredientPrice.setPrice(ingredientPriceRest.getPrice());
        ingredientPrice.setDateValue(ingredientPriceRest.getDateValue());
        ingredientPrice.setIngredient(ingredientPrice.getIngredient());
        return ingredientPrice;
    }

    public Stock mapToStock(Rest rest) {
        StockRest stockRest = (StockRest) rest;
        Stock stock = new Stock();
        if(stockRest.getId() != null) {
            stock.setId(stockRest.getId());
        }
        if(stockRest.getIngredient() != null) {
            stock.setIngredient(stockRest.getIngredient());
        }
        stock.setMovementType(stockRest.getMovementType());
        stock.setQuantityINOUT(stockRest.getQuantityINOUT());
        stock.setDateOfMovement(stockRest.getDateOfMovement());
        return stock;
    }
}
