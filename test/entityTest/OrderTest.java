package entityTest;

import dish.Dish;
import dish.DishDAO;
import order.Order;
import order.OrderDAO;
import order.OrderStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
        orderList.put(hotdog,31);
        order.setDishesOrder(orderList);
        order.makeOrder();
    }

    @Test
    void make_order(){
        Order order = new Order();
        Map<Dish, Integer>  orderList = new HashMap<>();
        DishDAO dishDAO = new DishDAO();
        Map<String,Object> hotcriteria = new HashMap<>();
        hotcriteria.put("dish_name","HOT dog");
        Dish hotdog = dishDAO.findByName(hotcriteria);
        orderList.put(hotdog,3);
        order.setDishesOrder(orderList);
        order.setOrderStatus(OrderStatus.CREATE);
        order.makeOrder();
        OrderDAO orderDAO = new OrderDAO();
        Order result = orderDAO.createOrder(order);
        Assertions.assertEquals(order.getOrderStatus(),result.getOrderStatus());
    }

    @Test
    void get_specific_order(){
        OrderDAO orderDAO = new OrderDAO();
        LocalDateTime dateTime = Timestamp.valueOf("2025-03-14 23:37:24.507000").toLocalDateTime();
        Order order = orderDAO.findOrderByDateTime(dateTime);
        System.out.println(order);
    }
}
