package torn.ando.gpizzasb.gpizza.service;

import org.springframework.stereotype.Service;
import torn.ando.gpizzasb.gpizza.dao.IngredientPriceDAO;
import torn.ando.gpizzasb.gpizza.entity.IngredientPrice;

import java.util.List;

@Service
public class IngredientPriceService {

    private IngredientPriceDAO ingredientPriceDAO;

    public IngredientPriceService(IngredientPriceDAO ingredientPriceDAO) {
        this.ingredientPriceDAO = ingredientPriceDAO;
    }

    public List<IngredientPrice> getByIngredientId(Long ingredientId){
        return ingredientPriceDAO.findByIdIngredient(ingredientId);
    }

    public IngredientPrice getById(Long id, Long idIngredient){
        return ingredientPriceDAO.findById(id, idIngredient);
    }
}
