package torn.ando.gpizzasb.gpizza.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import torn.ando.gpizzasb.gpizza.dao.DishIngredientDAO;
import torn.ando.gpizzasb.gpizza.entity.DishIngredient;
import torn.ando.gpizzasb.gpizza.entityRest.DishIngredientRest;
import torn.ando.gpizzasb.gpizza.mapper.RestMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DishIngredientService {

    private DishIngredientDAO dishIngredientDAO;
    private RestMapper restMapper;

    public List<DishIngredient> saveAll(List<DishIngredientRest> dishIngredientRestList){
        List<DishIngredient> dishIngredientList = dishIngredientRestList
                .stream()
                .map(dishIngredientRest -> {
                    DishIngredient dishIngredient = new DishIngredient();
                    dishIngredient.setUnit(dishIngredientRest.getUnit());
                    dishIngredient.setRequiredQuantity(dishIngredientRest.getRequiredQuantity());
                    dishIngredient.setIngredient(dishIngredientRest.getIngredient());
                    dishIngredient.setDish(dishIngredientRest.getDish());
                    return dishIngredient;
                }).toList();
        return dishIngredientDAO.saveAll(dishIngredientList);
    }
}
