package torn.ando.gpizzasb.gpizza.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import torn.ando.gpizzasb.gpizza.dao.BestTimeProcessingDAO;
import torn.ando.gpizzasb.gpizza.dao.DishDAO;
import torn.ando.gpizzasb.gpizza.dao.DishIngredientDAO;
import torn.ando.gpizzasb.gpizza.entity.BestProcessingTime;
import torn.ando.gpizzasb.gpizza.entity.BestProcessingTimeResponse;
import torn.ando.gpizzasb.gpizza.entity.Dish;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import torn.ando.gpizzasb.gpizza.entity.DishIngredient;
import torn.ando.gpizzasb.gpizza.entityRest.BestProcessingTimeRest;
import torn.ando.gpizzasb.gpizza.entityRest.DishIngredientRest;
import torn.ando.gpizzasb.gpizza.entityRest.DishRest;
import torn.ando.gpizzasb.gpizza.enums.DurationUnit;
import torn.ando.gpizzasb.gpizza.enums.StatisticType;
import torn.ando.gpizzasb.gpizza.mapper.RestMapper;
import torn.ando.gpizzasb.gpizza.service.DishIngredientService;
import torn.ando.gpizzasb.gpizza.service.DishService;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private BestTimeProcessingDAO bestTimeProcessingDAO;

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
/*
    @GetMapping("{id}/bestProcessingTime")
    public ResponseEntity<BestProcessingTimeResponse> getBestProcessingTimes() {
        List<BestProcessingTime> bestTimes = bestTimeProcessingDAO.findAll();

        List<BestProcessingTimeRest> restList = bestTimes.stream().map(time -> {
            BestProcessingTimeRest rest = new BestProcessingTimeRest();
            rest.setDish(time.getDish().getName());
            rest.setPreparationDuration(time.getPreparationDuration().toMillis() / 1000.0);
            rest.setDurationUnit(String.valueOf(DurationUnit.SECONDS));
            return rest;
        }).collect(Collectors.toList());

        BestProcessingTimeResponse response = new BestProcessingTimeResponse();
        response.setUpdatedAt(Instant.now());
        response.setBestProcessingTimes(restList);

        return ResponseEntity.ok(response);
    }*/

    @GetMapping("{id}/bestProcessingTime")
    /**
     * /bestProcessingTime - uses defaults (seconds and average)
     *
     * /bestProcessingTime?durationUnit=MINUTES - shows times in minutes, average
     *
     * /bestProcessingTime?statisticType=MAXIMUM - shows maximum times in seconds
     *
     * /bestProcessingTime?durationUnit=HOURS&statisticType=MINIMUM - shows minimum times in
     */
    public ResponseEntity<BestProcessingTimeResponse> getBestProcessingTimes(
            @RequestParam(required = false, defaultValue = "SECONDS") DurationUnit durationUnit,
            @RequestParam(required = false, defaultValue = "AVERAGE") StatisticType statisticType) {

        List<BestProcessingTime> bestTimes = bestTimeProcessingDAO.findAll();

        // Calculate the appropriate statistic
        Map<Dish, List<Duration>> durationsByDish = bestTimes.stream()
                .collect(Collectors.groupingBy(
                        BestProcessingTime::getDish,
                        Collectors.mapping(BestProcessingTime::getPreparationDuration, Collectors.toList())
                ));

        List<BestProcessingTimeRest> restList = durationsByDish.entrySet().stream().map(entry -> {
            double durationValue;
            switch (statisticType) {
                case MINIMUM:
                    durationValue = entry.getValue().stream()
                            .mapToLong(Duration::toNanos)
                            .min()
                            .orElse(0) / 1_000_000_000.0;
                    break;
                case MAXIMUM:
                    durationValue = entry.getValue().stream()
                            .mapToLong(Duration::toNanos)
                            .max()
                            .orElse(0) / 1_000_000_000.0;
                    break;
                case AVERAGE:
                default:
                    durationValue = entry.getValue().stream()
                            .mapToLong(Duration::toNanos)
                            .average()
                            .orElse(0) / 1_000_000_000.0;
            }

            switch (durationUnit) {
                case MINUTES:
                    durationValue /= 60;
                    break;
                case HOURS:
                    durationValue /= 3600;
                    break;
                case SECONDS:
                default:

            }

            BestProcessingTimeRest rest = new BestProcessingTimeRest();
            rest.setDish(entry.getKey().getName());
            rest.setPreparationDuration(durationValue);
            rest.setDurationUnit(durationUnit.name());
            return rest;
        }).collect(Collectors.toList());

        BestProcessingTimeResponse response = new BestProcessingTimeResponse();
        response.setUpdatedAt(Instant.now());
        response.setBestProcessingTimes(restList);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/processingTime")
    public ResponseEntity<?> getProcessingTime(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) DurationUnit timeUnit,
            @RequestParam(required = false) StatisticType statType) {

        try {
            double processingTime = dishService.getProcessingTime(
                    id, startDate, endDate, timeUnit, statType);

            Map<String, Object> response = new HashMap<>();
            response.put("dishId", id);
            response.put("processingTime", processingTime);
            response.put("timeUnit", timeUnit != null ? timeUnit : DurationUnit.SECONDS);
            response.put("statType", statType != null ? statType : StatisticType.AVERAGE);
            response.put("startDate", startDate);
            response.put("endDate", endDate);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error calculating processing time: " + e.getMessage());
        }
    }

}
/**
 @GetMapping("{id}/bestProcessingTime")
 public ResponseEntity<BestProcessingTimeResponse> getBestProcessingTimes(
 @RequestParam(name = "unit", required = false, defaultValue = "SECONDS") DurationUnit durationUnit,
 @RequestParam(name = "stat", required = false, defaultValue = "AVERAGE") StatisticType statisticType) {

 List<BestProcessingTime> allTimes = bestTimeProcessingDAO.findAll();

 // Group durations by dish
 Map<Dish, List<Duration>> dishDurations = allTimes.stream()
 .collect(Collectors.groupingBy(
 BestProcessingTime::getDish,
 Collectors.mapping(BestProcessingTime::getPreparationDuration, Collectors.toList())
 ));

 // Process each dish according to requirements
 List<BestProcessingTimeRest> result = dishDurations.entrySet().stream()
 .map(entry -> {
 double processedDuration = calculateDuration(entry.getValue(), statisticType);
 double convertedDuration = convertDurationUnit(processedDuration, durationUnit);

 return createResponseItem(entry.getKey(), convertedDuration, durationUnit);
 })
 .collect(Collectors.toList());

 return ResponseEntity.ok(
 new BestProcessingTimeResponse(Instant.now(), result)
 );
 }

 // Helper methods for better readability
 private double calculateDuration(List<Duration> durations, StatisticType statType) {
 return switch (statType) {
 case MINIMUM -> durations.stream()
 .mapToDouble(Duration::toMillis)
 .min()
 .orElse(0) / 1000.0;
 case MAXIMUM -> durations.stream()
 .mapToDouble(Duration::toMillis)
 .max()
 .orElse(0) / 1000.0;
 default -> durations.stream()
 .mapToDouble(Duration::toMillis)
 .average()
 .orElse(0) / 1000.0;
 };
 }

 private double convertDurationUnit(double durationInSeconds, DurationUnit unit) {
 return switch (unit) {
 case MINUTES -> durationInSeconds / 60;
 case HOURS -> durationInSeconds / 3600;
 default -> durationInSeconds;
 };
 }

 private BestProcessingTimeRest createResponseItem(Dish dish, double duration, DurationUnit unit) {
 BestProcessingTimeRest item = new BestProcessingTimeRest();
 item.setDish(dish.getName());
 item.setPreparationDuration(duration);
 item.setDurationUnit(unit.name());
 return item;
 }
 */
