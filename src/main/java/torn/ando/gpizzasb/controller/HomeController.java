package torn.ando.gpizzasb.controller;

import gpizza.dish.Dish;
import gpizza.dish.DishDAO;
import gpizza.dish.dishIngredient.DishIngredient;
import gpizza.dish.dishIngredient.DishIngredientDAO;
import gpizza.order.Order;
import gpizza.order.orderDish.OrderDish;
import gpizza.order.orderService.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class HomeController {

    @GetMapping("/")
    public String index(){
        OrderService orderService = new OrderService();
        Order order = orderService.findByReference("ffff");
        return order.toString();
    }

    @GetMapping("/150")
    public String isAvailable(){
        OrderService orderService = new OrderService();
        Order order = new Order();
        order.setId_order(1);
        order.setReference("kfb");
        order.setOrder_datetime(LocalDate.now().atStartOfDay());
        OrderDish orderDish = new OrderDish();
        DishDAO dishDAO = new DishDAO();
        Dish dish = dishDAO.findById(1);
        DishIngredientDAO dishIngredientDAO = new DishIngredientDAO();
        DishIngredient dishIngredient =  dishIngredientDAO.findByDish((int) dish.getId_dish()).get(0);
        orderDish.setQuantity(3);
        orderDish.setDishIngredient(dishIngredient);
        order.setOrderDishList(List.of(orderDish));
        int number = orderService.verify(order);
        return String.valueOf(number);
    }
}
