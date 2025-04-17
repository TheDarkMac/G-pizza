package torn.ando.gpizzasb.gpizza.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import torn.ando.gpizzasb.gpizza.dao.DishDAO;
import torn.ando.gpizzasb.gpizza.dao.DishIngredientDAO;
import torn.ando.gpizzasb.gpizza.dao.IngredientDAO;
import torn.ando.gpizzasb.gpizza.entity.Dish;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import torn.ando.gpizzasb.gpizza.entity.DishIngredient;
import torn.ando.gpizzasb.gpizza.entity.Ingredient;
import torn.ando.gpizzasb.gpizza.entityRest.DishIngredientRest;
import torn.ando.gpizzasb.gpizza.entityRest.DishRest;
import torn.ando.gpizzasb.gpizza.mapper.RestMapper;
import torn.ando.gpizzasb.gpizza.service.DishIngredientService;
import torn.ando.gpizzasb.gpizza.service.DishService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dishes")
@AllArgsConstructor
public class DishController {

    private final RestMapper restMapper;
    private final DishRest dishRest;
    private final DishIngredientDAO dishIngredientDAO;
    private final DishDAO dishDAO;
    private DishService dishService;
    private DishIngredientService dishIngredientService;

    @GetMapping
    public ResponseEntity<List<Dish>> findAll() {
        List<Dish> dishes = dishService.findAll();
        return new ResponseEntity<>(dishes, HttpStatus.OK);
    }

    /*
    *to create Dish, at least need to provide 1 ingredient
    * */
    @PutMapping
    public ResponseEntity<List<Dish>> addDish(@RequestBody List<DishRest> dishRests){
        List<Dish> dishes = dishRests.stream()
                .map(restMapper::mapToDish)
                .toList();
        List<Dish> sd = dishService.saveAll(dishes);
        List<DishIngredient> dishIngredients = dishRests.stream()
                .flatMap(d -> d.getDishIngredientRestList().stream()
                        .peek(di -> di.setDishId(d.getId()))
                        .map(restMapper::mapToDishIngredient)
                )
                .collect(Collectors.toList());
        dishIngredientDAO.saveAll(dishIngredients);
        List<Dish> newDishes = new ArrayList<>();
        dishRests.forEach(d -> {
            Dish dish = dishDAO.findById(d.getId());
            newDishes.add(dish);
        });
        return new ResponseEntity<>(newDishes, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Dish> findDishById(@PathVariable("id") Long id){
        Dish dish = dishService.findById(id);
        if(dish == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dish, HttpStatus.OK);
    }

    @PutMapping("{id}/ingredients")
    public ResponseEntity<Dish> addIngredientsIntoDish(
            @PathVariable("id") Long id,
            @RequestBody List<DishIngredientRest> dishIngredientsRest){
        List<DishIngredient> dishIngredientList = dishIngredientsRest.stream()
                .peek(dishIngredientRest -> dishIngredientRest.setDishId(id))
                .map(restMapper::mapToDishIngredient)
                .toList();
        dishIngredientService.saveAll(dishIngredientList);
        Dish dish = dishService.findById(id);
        return ResponseEntity.ok(dish);
    }
}
