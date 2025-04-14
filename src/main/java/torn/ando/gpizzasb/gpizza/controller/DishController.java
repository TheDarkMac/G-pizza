package torn.ando.gpizzasb.gpizza.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import torn.ando.gpizzasb.gpizza.dao.IngredientDAO;
import torn.ando.gpizzasb.gpizza.entity.Dish;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import torn.ando.gpizzasb.gpizza.entity.Ingredient;
import torn.ando.gpizzasb.gpizza.entityRest.DishIngredientRest;
import torn.ando.gpizzasb.gpizza.entityRest.DishRest;
import torn.ando.gpizzasb.gpizza.mapper.RestMapper;
import torn.ando.gpizzasb.gpizza.service.DishIngredientService;
import torn.ando.gpizzasb.gpizza.service.DishService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dishes")
@AllArgsConstructor
public class DishController {

    private DishService dishService;
    private DishIngredientService dishIngredientService;
    private IngredientDAO ingredientDAO;
    private RestMapper restMapper;

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
        List<Dish> dishList = dishRests.stream()
                .map(dishRest -> restMapper.mapToDish(dishRest))
                .toList();
        return ResponseEntity.ok(dishList);
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
    public ResponseEntity<Dish> addIngredientsIntoDish(@PathVariable("id") Long id, @RequestBody List<DishIngredientRest> dishIngredientsRest){
        Dish dish = dishService.findById(id);
        if(dish == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        if (dishIngredientsRest == null || dishIngredientsRest.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        //not support
//        List<DishIngredientRest> diWithAllProperties =  dishIngredientsRest.stream()
//                        .map(dishIngredientRest -> {
//                            Ingredient ingredient =  ingredientDAO.findById(dishIngredientRest.getId());
//                           // dishIngredientRest.setDish(dish);
//                            dishIngredientRest.setIngredient(ingredient);
//                            return dishIngredientRest;
//                        }).toList();
//
//        dishIngredientService.saveAll(diWithAllProperties);
        return new ResponseEntity<>(dishService.findById(id), HttpStatus.OK);
    }
}
