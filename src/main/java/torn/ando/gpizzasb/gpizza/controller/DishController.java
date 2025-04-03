package torn.ando.gpizzasb.gpizza.controller;

import torn.ando.gpizzasb.gpizza.dao.DishDAO;
import torn.ando.gpizzasb.gpizza.entity.Dish;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dish")
public class DishController {

    DishDAO dishDAO;

    @GetMapping("{id}")
    public ResponseEntity<Dish> findDishById(@PathVariable("id") int id){

        Dish dish = dishDAO.findById(id);
        if(dish == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dish, HttpStatus.OK);
    }
}
