package torn.ando.gpizzasb.gpizza.controller;

import lombok.AllArgsConstructor;
import torn.ando.gpizzasb.gpizza.entity.Ingredient;
import torn.ando.gpizzasb.gpizza.entity.IngredientPrice;
import torn.ando.gpizzasb.gpizza.entityRest.IngredientPriceRest;
import torn.ando.gpizzasb.gpizza.entityRest.IngredientRest;
import torn.ando.gpizzasb.gpizza.service.IngredientPriceService;
import torn.ando.gpizzasb.gpizza.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private IngredientService ingredientService;
    private IngredientPriceService ingredientPriceService;

    public IngredientController(IngredientService ingredientService,IngredientPriceService ingredientPriceService) {
        this.ingredientService = ingredientService;
        this.ingredientPriceService = ingredientPriceService;
    }

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
    public ResponseEntity<Ingredient> createPrice(@RequestBody IngredientPriceRest ingredientPriceRest){
        throw new UnsupportedOperationException("not support yet");
    }

    @GetMapping("{id}/prices/{idPrice}")
    public ResponseEntity<Object> getPriceById(@PathVariable("id") Long id, @PathVariable("idPrice") Long idPrice){
        IngredientPrice ingredientPrice = ingredientPriceService.getById(idPrice,id);
        if(ingredientPrice == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ingredientPrice);
        }
        return ResponseEntity.ok(ingredientPrice);
    }


}
