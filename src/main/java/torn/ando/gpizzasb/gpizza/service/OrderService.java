package torn.ando.gpizzasb.gpizza.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import torn.ando.gpizzasb.gpizza.dao.OrderDao;
import torn.ando.gpizzasb.gpizza.dao.OrderDishDAO;
import torn.ando.gpizzasb.gpizza.dao.OrderDishStatusDAO;
import torn.ando.gpizzasb.gpizza.dao.OrderStatusDAO;
import torn.ando.gpizzasb.gpizza.entity.Order;
import torn.ando.gpizzasb.gpizza.entity.OrderDish;
import torn.ando.gpizzasb.gpizza.entity.OrderStatus;
import torn.ando.gpizzasb.gpizza.mapper.RestMapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OrderService {

    private final OrderDishDAO orderDishDAO;
    private OrderDao orderDao;
    private OrderStatusDAO orderStatusDao;
    private OrderDishStatusDAO orderDishStatusDAO;
    private RestMapper restMapper;

    public Order findByReference(String reference){
        Order order = orderDao.findByReference(reference);
        List<OrderDish> orderDishes = orderDishDAO.findByOrderReference(reference);
        order.setOrderDishList(orderDishes);
        return order;
    }

    public List<Order> createOrder(List<Order> orders) {
        orders.forEach(order -> {
            List<OrderDish> dishes = order.getOrderDishList();
            List<OrderStatus> statuses = order.getOrderStatusList();

            if ((dishes != null && !dishes.isEmpty())) {
                dishes.forEach(dish -> {
                });
                orderDishDAO.saveAll(dishes);
            }
            if((statuses != null && !statuses.isEmpty())){
                orderDishStatusDAO.saveAll(statuses);
            }
        });
        return orderDao.saveAll(orders);
    }


    public void deleteOrderByReference(String reference){
        orderDao.deleteByReference(reference);
    }

    public void changeOrderStatus(String reference){
        Order order = orderDao.findByReference(reference);
        Integer orderActualStatus = restMapper.mapToInteger(order.getActualStatus());
        Integer min = order.getOrderDishList()
                .stream()
                .map(OrderDish::getActualStatus)
                .map(orderStatusType -> restMapper.mapToInteger(orderStatusType))
                .min(Integer::compareTo)
                .orElse(0);
        if(orderActualStatus < min){
            if(orderActualStatus + 1 == min){
                System.out.println("Order status changed to " + orderActualStatus);
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setReferenceOrder(reference);
                orderStatus.setOrderStatus(restMapper.mapToOrderStatusType(min));
                orderStatus.setDatetime(LocalDateTime.now());
                orderStatusDao.saveAll(List.of(orderStatus));
            }else {
                System.out.println("something strange in the air");
            }
        }else {
            System.out.println("Order status didn't changed " + orderActualStatus);
        }
    }
}
