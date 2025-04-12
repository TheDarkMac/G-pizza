package torn.ando.gpizzasb.gpizza.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import torn.ando.gpizzasb.gpizza.dao.DishDAO;
import torn.ando.gpizzasb.gpizza.dao.OrderDao;
import torn.ando.gpizzasb.gpizza.dao.OrderDishDAO;
import torn.ando.gpizzasb.gpizza.entity.*;
import torn.ando.gpizzasb.gpizza.entityRest.*;
import torn.ando.gpizzasb.gpizza.enums.OrderStatusType;

@AllArgsConstructor
@Service
public class RestMapper {

    private DishDAO dishDAO;
    private OrderDishDAO orderDishDAO;
    private OrderDao orderDao;

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

    public OrderDish mapToOrderDish(OrderDishRest rest){
        OrderDish orderDish = new OrderDish();
        Dish dish = dishDAO.findById(rest.getId());
        orderDish.setDish(dish);
        orderDish.setOrder(rest.getOrder());
        orderDish.setQuantity(rest.getQuantity());
        return orderDish;
    }

    public OrderDishStatus mapToOrderDishStatus(OrderDishStatusRest rest){
        OrderDishStatus orderDishStatus = new OrderDishStatus();
        orderDishStatus.setOrderStatus(rest.getOrderStatus());
        Order order = orderDao.findByReference(rest.getOrderReference());
        OrderDish orderDish = orderDishDAO.findByReferenceAndDishId(rest.getOrderReference(),rest.getDishId());
        orderDish.setOrder(order);
        orderDishStatus.setOrderDish(orderDish);
        orderDishStatus.setUpdateAt(rest.getUpdatedAt());
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
        }
        else if(orderStatusType == 4) {
            return OrderStatusType.DONE;
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
        o.setOrderDate(order.getDateOfOrder());
        return o;
    }
}
