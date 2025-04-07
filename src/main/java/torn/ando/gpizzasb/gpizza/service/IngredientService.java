package torn.ando.gpizzasb.gpizza.service;

import lombok.AllArgsConstructor;
import torn.ando.gpizzasb.gpizza.dao.IngredientDAO;
import torn.ando.gpizzasb.gpizza.dao.IngredientPriceDAO;
import torn.ando.gpizzasb.gpizza.entity.Ingredient;
import org.springframework.stereotype.Service;
import torn.ando.gpizzasb.gpizza.entity.IngredientPrice;
import torn.ando.gpizzasb.gpizza.entityRest.IngredientRest;
import torn.ando.gpizzasb.gpizza.mapper.RestMapper;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class IngredientService {

    private IngredientDAO ingredientDAO;
    private IngredientPriceDAO ingredientPriceDAO;
    private RestMapper restMapper;

    public List<Ingredient> saveAll(List<IngredientRest> ingredientRests) {
        List<Ingredient> ingredients = new ArrayList<>();
        List<IngredientPrice> ingredientPrices = new ArrayList<>();
        ingredientRests.forEach(ingredientRest -> {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(ingredientRest.getId());
            ingredient.setName(ingredientRest.getName());
            IngredientPrice ingredientPrice = new IngredientPrice();
            ingredientPrice.setIngredient(ingredient);
            ingredientPrice.setDateValue(ingredientRest.getUpdatedAt());
            ingredientPrice.setPrice(ingredientRest.getUnitPrice());
            ingredientPrices.add(ingredientPrice);
            ingredients.add(ingredient);
        });
        ingredientDAO.saveAll(ingredients);

        ingredientPriceDAO.saveAll(ingredientPrices);
        List<Ingredient> ingredientListResult = ingredientRests.stream()
                .map(ingredientRest -> {
                    Ingredient ingredient = ingredientDAO.findById(ingredientRest.getId());
                    return ingredient;
                }).toList();
        return ingredientListResult;
    }

    public Ingredient findById(long id) {
        return ingredientDAO.findById(id);
    }

    public List<Ingredient> findAll() {
        return ingredientDAO.findAll();
    }

    public List<Ingredient> updateAll(List<IngredientRest> ingredientRests) {
        List<Ingredient> ingredientList = ingredientRests
                .stream()
                .map(restMapper::mapToIngredient)
                .toList();
        ingredientDAO.updateAll(ingredientList);
        List<Ingredient> savedIngredients = new ArrayList<>();
        ingredientList.forEach(ingredient -> {
            Ingredient savedIngredient = ingredientDAO.findById(ingredient.getId());
            savedIngredients.add(savedIngredient);
        });
        return savedIngredients;
    }
}
