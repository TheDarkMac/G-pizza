package gpizza.order.orderService;

import gpizza.availableQuantity.AvailableQuantity;
import gpizza.availableQuantity.AvailableQuantityDAO;
import gpizza.dataSource.DAOSchema;
import gpizza.dish.Dish;
import gpizza.dish.DishDAO;
import gpizza.dish.dishIngredient.DishIngredient;
import gpizza.dish.dishIngredient.DishIngredientDAO;
import gpizza.order.Order;
import gpizza.order.OrderDAO;
import gpizza.order.orderDish.OrderDish;
import gpizza.order.orderDish.OrderDishDAO;
import gpizza.order.orderDish.orderDishStatus.OrderDishStatus;
import gpizza.order.orderDish.orderDishStatus.OrderDishStatusDAO;
import gpizza.stock.StockDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class OrderService implements DAOSchema {

    OrderDAO orderDAO = new OrderDAO();
    OrderDishDAO orderDishDAO = new OrderDishDAO();
    OrderDishStatusDAO orderDishStatusDAO = new OrderDishStatusDAO();
    DishIngredientDAO dishIngredientDAO = new DishIngredientDAO();
    DishDAO dishDAO = new DishDAO();
    AvailableQuantityDAO availableQuantityDAO = new AvailableQuantityDAO();

    @Override
    public <T> List<T> saveAll(List<T> list) {
        List<Order> orderList = (List<Order>) list;
        orderList.forEach(order -> {

        });
        return List.of();
    }

    @Override
    public <T> List<T> findAll() {
        return List.of();
    }

    @Override
    public <T> T findById(double id) {
        return null;
    }

    @Override
    public <T> T deleteById(double id) {
        return null;
    }

    @Override
    public <T> List<T> deleteAll(List<T> list) {
        return List.of();
    }

    @Override
    public <T> List<T> update(List<T> t) {
        return List.of();
    }

    public Order findByReference(String referenceOrder){
        Order order = orderDAO.findByIReference(referenceOrder);
        if(order == null){
            throw new RuntimeException("this reference does not exist");
        }
        List<OrderDish> orderDishes = (orderDishDAO.findAllByReference(referenceOrder));

        orderDishes.stream().forEach(orderDish -> {
            orderDish.setStatusList(orderDishStatusDAO.findAllByOrderDish(orderDish.getId_order_dish()));
        });
        order.setOrderDishList(orderDishes);
        return order;
    }

    public int verify(Order order){
        List<Integer> everyQantity = new ArrayList<>();
        order.getOrderDishList().stream()
                .forEach(orderDish -> {
                    AvailableQuantity availableQuantity = (AvailableQuantity)(availableQuantityDAO.findById(orderDish.getDishIngredient().getIngredient().getId()));
                    double in_stock = availableQuantity.getQuantity();
                    double clientOrder = orderDish.getQuantity();
                    double requiredForOneDish = orderDish.getDishIngredient().getQuantity();
                    everyQantity.add((int) (in_stock - clientOrder*requiredForOneDish));
                });
        return everyQantity.stream().min(Integer::compareTo).get();
    }
}
