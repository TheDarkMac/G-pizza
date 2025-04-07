package torn.ando.gpizzasb.gpizza.controller;

import lombok.AllArgsConstructor;
import torn.ando.gpizzasb.gpizza.entity.Ingredient;
import torn.ando.gpizzasb.gpizza.entity.IngredientPrice;
import torn.ando.gpizzasb.gpizza.entity.Stock;
import torn.ando.gpizzasb.gpizza.entityRest.IngredientPriceRest;
import torn.ando.gpizzasb.gpizza.entityRest.IngredientRest;
import torn.ando.gpizzasb.gpizza.entityRest.StockRest;
import torn.ando.gpizzasb.gpizza.mapper.RestMapper;
import torn.ando.gpizzasb.gpizza.service.IngredientPriceService;
import torn.ando.gpizzasb.gpizza.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import torn.ando.gpizzasb.gpizza.service.StockService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ingredients")
@AllArgsConstructor
public class IngredientController {

    private final StockService stockService;
    private IngredientService ingredientService;
    private IngredientPriceService ingredientPriceService;
    private RestMapper restMapper;

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAll(){
        return ResponseEntity.ok(ingredientService.findAll());
    }

    @PostMapping
    public ResponseEntity<List<Ingredient>> create(@RequestBody List<IngredientRest> ingredient){
        List<Ingredient> ingredients = ingredientService.saveAll(ingredient);
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredients);
    }

    @PutMapping
    public ResponseEntity<List<Ingredient>> update(@RequestBody List<IngredientRest> ingredient){
        return ResponseEntity.status(HttpStatus.OK).body(ingredientService.updateAll(ingredient));
    }

    @GetMapping("{id}")
    public ResponseEntity<Ingredient> getById(@PathVariable("id") Long id){
        Ingredient ingredient = ingredientService.findById(id);
        if(ingredient == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ingredient);
        }
        return ResponseEntity.ok(ingredient);
    }

    @PutMapping("{id}")
    public ResponseEntity<Ingredient> update(@PathVariable("id") Long id, @RequestBody IngredientRest ingredient){
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(ingredientService.updateAll(List.of(ingredient)).get(0));
    }

    @GetMapping("{id}/prices")
    public ResponseEntity<Object> getPricesList(@PathVariable("id") Long id){
        List<IngredientPrice> ingredientPriceList = ingredientPriceService.getByIngredientId(id);
        return ResponseEntity.ok(ingredientPriceList);
    }

    @PutMapping("{id}/prices")
    public ResponseEntity<Ingredient> createPrice(@PathVariable("id") Long id,@RequestBody IngredientPriceRest ingredientPriceRest){
        System.out.println(ingredientPriceRest);
        IngredientPrice ingredientPrice = restMapper.mapToIngredientPrice(ingredientPriceRest);
        Ingredient ingredient = ingredientService.findById(id);
        ingredientPrice.setIngredient(ingredient);
        ingredientPriceService.create(ingredientPrice);
        Ingredient updateIngredient = ingredientService.findById(id);

        return ResponseEntity.ok(updateIngredient);
    }

    @PutMapping("{id}/stockMovements")
    public ResponseEntity<Ingredient> addStockMovements(@PathVariable("id") Long id, @RequestBody List<StockRest> stockListRest){
        Ingredient ingredient = ingredientService.findById(id);
        if(ingredient == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<StockRest> stockRestWithIngredient = stockListRest.stream().map(
                stockRest -> {
                    StockRest sr = stockRest;
                    sr.setIngredient(ingredient);
                    return sr;
                }).toList();
        stockService.saveAll(stockRestWithIngredient);
        Ingredient updatedIngredient = ingredientService.findById(id);
        return ResponseEntity.ok(updatedIngredient);
    }
}
