package torn.ando.gpizzasb.gpizza.mapper;

import org.springframework.stereotype.Component;
import torn.ando.gpizzasb.gpizza.entity.Ingredient;
import torn.ando.gpizzasb.gpizza.entity.IngredientPrice;
import torn.ando.gpizzasb.gpizza.entityRest.IngredientRest;

@Component
public class RestMapper {

    public Ingredient mapToIngredient(Rest rest) {
        IngredientRest ingredientRest = (IngredientRest) rest;
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientRest.getId());
        ingredient.setName(ingredientRest.getName());
        ingredient.setUnit(ingredientRest.getUnit());
        return ingredient;
    }

    public IngredientPrice mapToIngredientPrice(Rest rest) {
        IngredientRest ingredientRest = (IngredientRest) rest;
        IngredientPrice ingredientPrice = new IngredientPrice();
        ingredientPrice.setPrice(ingredientRest.getUnitPrice());
        ingredientPrice.setDateValue(ingredientRest.getUpdatedAt());
        ingredientPrice.setIngredient(mapToIngredient(rest));
        return ingredientPrice;
    }
}
