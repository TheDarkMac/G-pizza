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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IngredientService {

    private IngredientDAO ingredientDAO;
    private IngredientPriceDAO ingredientPriceDAO;
    private RestMapper restMapper;

    public List<Ingredient> saveAll(List<IngredientRest> ingredientRests) {
        List<Ingredient> ingredients = (List<Ingredient>) ingredientRests
                .stream()
                .map(restMapper::mapToIngredient).toList();
        List<IngredientPrice> ingredientPriceList = ingredientRests
                .stream()
                .map(restMapper::mapToIngredientPrice)
                .toList();
        ingredientPriceDAO.saveAll(ingredientPriceList);
        ingredientDAO.saveAll(ingredients);

        List<Ingredient> savedIngredients = new ArrayList<>();
        ingredients.forEach(ingredient -> {
            Ingredient savedIngredient = ingredientDAO.findById(ingredient.getId());
            savedIngredients.add(savedIngredient);
        });

        return savedIngredients;
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
