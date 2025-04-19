package torn.ando.gpizzasb.gpizza.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import torn.ando.gpizzasb.gpizza.dao.DishDAO;
import torn.ando.gpizzasb.gpizza.dao.IngredientDAO;
import torn.ando.gpizzasb.gpizza.dao.OrderDao;
import torn.ando.gpizzasb.gpizza.dao.OrderDishDAO;
import torn.ando.gpizzasb.gpizza.entity.*;
import torn.ando.gpizzasb.gpizza.entityRest.*;
import torn.ando.gpizzasb.gpizza.enums.OrderStatusType;

@AllArgsConstructor
@Service
public class RestMapper {

    private final IngredientDAO ingredientService;
    private DishDAO dishService;
    private OrderDishDAO orderDishService;
    private OrderDao orderService;

    public Ingredient mapToIngredient(IngredientRest rest) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(rest.getId());
        ingredient.setName(rest.getName());
        return ingredient;
    }

    public IngredientPrice mapToIngredientPrice(IngredientRest rest) {
        IngredientPrice ingredientPrice = new IngredientPrice();
        ingredientPrice.setPrice(rest.getUnitPrice());
        ingredientPrice.setDateValue(rest.getUpdatedAt());
        if(rest.getId() != null) {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(rest.getId());
            ingredient.setName(rest.getName());
            ingredientPrice.setIngredient(ingredient);
        }
        return ingredientPrice;
    }

    public Stock mapToStock(StockRest rest) {
        Stock stock = new Stock();
        if(rest.getId() != null) {
            stock.setId(rest.getId());
        }
        if(rest.getIngredient() != null) {
            stock.setIngredient(rest.getIngredient());
        }
        stock.setMovementType(rest.getMovementType());
        stock.setQuantityINOUT(rest.getQuantityINOUT());
        stock.setDateOfMovement(rest.getDateOfMovement());
        return stock;
    }

    public OrderDish mapToOrderDish(DishRest rest){
        OrderDish orderDish = new OrderDish();
        Dish dish = dishService.findById(rest.getId());
        orderDish.setDish(dish);
        orderDish.setOrder(rest.getOrder());
        orderDish.setQuantity(rest.getQuantityOrdered());
        return orderDish;
    }

    public OrderDishStatus mapToOrderDishStatus(OrderDishStatusRest rest){
        OrderDishStatus orderDishStatus = new OrderDishStatus();
        orderDishStatus.setOrderStatus(rest.getOrderStatus());
        Order order = orderService.findByReference(rest.getOrderReference());
        OrderDish orderDish = orderDishService.findByReferenceAndDishId(rest.getOrderReference(),rest.getDishId());
        orderDish.setOrder(order);
        orderDishStatus.setOrderDish(orderDish);
        orderDishStatus.setUpdateAt(rest.getUpdatedAt());

        /**
         * Instanciation anle ProcessingTime
         *
         ProcessingTime processingTime = new ProcessingTime();
         processingTime.setIdProcessingTime(1);
         processingTime.setIdDish(orderDish.getDish().getId());
         processingTime.setReferenceOrder(orderDish.getOrder().getReference());
         */
        return orderDishStatus;
    }

    public Integer mapToInteger(OrderStatusType orderStatusType){
        if(orderStatusType == OrderStatusType.CREATED) {
            return 1;
        }
        else if(orderStatusType == OrderStatusType.CONFIRMED) {
            return 2;
        }
        else if(orderStatusType == OrderStatusType.IN_PREPARATION) {
            return 3;
        }
        else if(orderStatusType == OrderStatusType.DONE) {
            return 4;
        }
        else if(orderStatusType == OrderStatusType.SERVED) {
            return 5;
        }
        else {
            return 0;
        }
    }

    public OrderStatusType mapToOrderStatusType(Integer orderStatusType){
        if(orderStatusType == 1) {
            return OrderStatusType.CREATED;
        }
        else if(orderStatusType == 2) {
            return OrderStatusType.CONFIRMED;
        }
        else if(orderStatusType == 3) {
            return OrderStatusType.IN_PREPARATION;
            //recuperation anle date de debut de preparation
            //processingTime.setBeginningTime(rest.getUpdatedAt());
        }
        else if(orderStatusType == 4) {
            return OrderStatusType.DONE;
            /**
             * recuperation anle date de fin de preparation :
             * processingTime.setEndTime(orderDish.getUpdatedAt());
             *
             * calcule difference de temps entre les deux dates===preparationTime:
             * processingTime.calculatePreparationTime(this.beginningTime,this.endTime);
             */
        }
        else if(orderStatusType == 5) {
            return OrderStatusType.SERVED;
        }
        else {
            throw new IllegalArgumentException("Invalid orderStatusType: " + orderStatusType);
        }
    }

    public Order mapToOrder(OrderRest order) {
        Order o = new Order();
        o.setReference(order.getReference());
        o.setId(order.getId());
        return o;
    }

    public Dish mapToDish(DishRest dishRest) {
        Dish dish = new Dish();
        dish.setId(dishRest.getId());
        dish.setName(dishRest.getName());
        dish.setPrice(dishRest.getPrice());
        return dish;
    }

    public DishIngredient mapToDishIngredient(DishIngredientRest dishIngredientRest) {
       DishIngredient dishIngredient = new DishIngredient();
       Ingredient i = ingredientService.findById(dishIngredientRest.getId());
        if(i != null) {
            dishIngredient.setIngredient(i);
        }else {
            throw new IllegalArgumentException("Invalid ingredient: " + dishIngredientRest);
        }
        Dish dish = dishService.findById(dishIngredientRest.getDishId());
        if(dish != null) {
            dishIngredient.setDish(dish);
        }
       dishIngredient.setRequiredQuantity(dishIngredientRest.getRequiredQuantity());
       dishIngredient.setUnit(dishIngredientRest.getUnit());
       return dishIngredient;
    }

    public OrderRest mapToOrderRest(Order order){
        OrderRest orderRest = new OrderRest();
        orderRest.setId(order.getId());
        orderRest.setTotalAmount(order.getTotalPrice());
        orderRest.setActualStatus(order.getActualStatus());
        orderRest.setDishes(order.getOrderDishList().stream()
                .map(orderDish -> mapToOrderDishRest(orderDish))
                .toList());
        return orderRest;
    }

    public DishRest mapToOrderDishRest(OrderDish orderDish){
        DishRest dishRest = new DishRest();
        dishRest.setId(dishRest.getId());
        dishRest.setName(orderDish.getDish().getName());
        dishRest.setQuantityOrdered(orderDish.getQuantity());
        dishRest.setActualOrderStatus(orderDish.getActualStatus());
        return dishRest;
    }

    public BestSales mapToBestSales(BestSalesRest bestSalesRest){
        BestSales bestSales = new BestSales();
        bestSales.setDishId(bestSalesRest.getDishId());
        bestSales.setDishName(bestSalesRest.getDishName());
        bestSales.setReference(bestSalesRest.getReference());
        bestSales.setQuantity(Math.toIntExact(bestSalesRest.getQuantity()));
        bestSales.setFrom(bestSalesRest.getFrom());
        bestSales.setTo(bestSalesRest.getTo());
        return bestSales;
    }
}
