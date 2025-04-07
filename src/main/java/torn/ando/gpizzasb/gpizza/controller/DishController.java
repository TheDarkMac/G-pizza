package torn.ando.gpizzasb.gpizza.controller;

import lombok.AllArgsConstructor;
import torn.ando.gpizzasb.gpizza.dao.DishDAO;
import torn.ando.gpizzasb.gpizza.entity.Dish;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import torn.ando.gpizzasb.gpizza.service.DishService;

import java.util.List;

@RestController
@RequestMapping("/dish")
@AllArgsConstructor
public class DishController {

    private DishService dishService;

    @GetMapping
    public ResponseEntity<List<Dish>> findAll() {
        List<Dish> dishes = dishService.findAll();
        return new ResponseEntity<>(dishes, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Dish> findDishById(@PathVariable("id") Long id){
        Dish dish = dishService.findById(id);
        if(dish == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dish, HttpStatus.OK);
    }
}
