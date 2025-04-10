package torn.ando.gpizzasb.gpizza.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import torn.ando.gpizzasb.gpizza.dao.OrderDao;
import torn.ando.gpizzasb.gpizza.dao.OrderDishDAO;
import torn.ando.gpizzasb.gpizza.dao.OrderDishStatusDAO;
import torn.ando.gpizzasb.gpizza.dao.OrderStatusDAO;
import torn.ando.gpizzasb.gpizza.entity.Order;
import torn.ando.gpizzasb.gpizza.entity.OrderDish;
import torn.ando.gpizzasb.gpizza.entity.OrderStatus;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {

    private final OrderDishDAO orderDishDAO;
    private OrderDao orderDao;
    private OrderStatusDAO orderStatusDao;
    private OrderDishStatusDAO orderDishStatusDAO;

    public Order findByReference(String reference){
        return orderDao.findByReference(reference);
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
}
