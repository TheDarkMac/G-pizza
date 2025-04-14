package torn.ando.gpizzasb.gpizza.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import torn.ando.gpizzasb.gpizza.dao.DishDAO;
import torn.ando.gpizzasb.gpizza.entity.Dish;
import torn.ando.gpizzasb.gpizza.entityRest.DishRest;

import java.util.List;

@Service
@AllArgsConstructor
public class DishService {
    private DishDAO dishDAO;

    public List<Dish> findAll() {
        return dishDAO.findAll();
    }

    public Dish findById(Long id) {
        return dishDAO.findById(id);
    }

    public List<Dish> saveAll(List<DishRest> dishesRest) {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
