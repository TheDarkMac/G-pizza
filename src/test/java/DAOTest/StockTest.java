package DAOTest;

import gpizza.dish.Dish;
import gpizza.dish.DishDAO;
import gpizza.dish.dishIngredient.DishIngredient;
import gpizza.dish.dishIngredient.DishIngredientDAO;
import gpizza.ingredient.IngredientDAO;
import gpizza.order.Order;
import gpizza.order.OrderDAO;
import gpizza.order.orderDish.OrderDish;
import gpizza.order.orderService.OrderService;
import gpizza.stock.Stock;
import gpizza.stock.StockDAO;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StockTest {
    @Test
    public void findStock(){
        OrderService orderService = new OrderService();
        Order order = orderService.findByReference("ffff");
        System.out.println(order);
    }
}
