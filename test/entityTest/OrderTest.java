package entityTest;

import dish.Dish;
import dish.DishDAO;
import order.Order;
import order.OrderStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class OrderTest {

    @Test
    void makeHotDogOrder(){
        Order order = new Order();
        order.setOrderStatus(OrderStatus.CREATE);
        Map<Dish, Integer>  orderList = new HashMap<>();
        DishDAO dishDAO = new DishDAO();
        Map<String,Object> hotcriteria = new HashMap<>();
        hotcriteria.put("dish_name","HOT dog");
        Dish hotdog = dishDAO.findByName(hotcriteria);
        orderList.put(hotdog,30);
        order.setDishes(orderList);
        order.makeOrder();
        Assertions.assertEquals(OrderStatus.CONFIRMED,order.getOrderStatus());
    }
}
