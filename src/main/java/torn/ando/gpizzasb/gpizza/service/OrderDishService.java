package torn.ando.gpizzasb.gpizza.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import torn.ando.gpizzasb.gpizza.dao.OrderDao;
import torn.ando.gpizzasb.gpizza.dao.OrderDishDAO;
import torn.ando.gpizzasb.gpizza.dao.OrderDishStatusDAO;
import torn.ando.gpizzasb.gpizza.dao.OrderStatusDAO;
import torn.ando.gpizzasb.gpizza.entity.Order;
import torn.ando.gpizzasb.gpizza.entity.OrderDish;
import torn.ando.gpizzasb.gpizza.entity.OrderDishStatus;
import torn.ando.gpizzasb.gpizza.entityRest.OrderDishRest;
import torn.ando.gpizzasb.gpizza.mapper.RestMapper;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OrderDishService {

    private OrderDao orderDao;
    private OrderStatusDAO orderStatusDao;
    private OrderDishDAO orderDishDAO;
    private RestMapper restMapper;
    private OrderDishStatusDAO orderDishStatusDAO;

    public List<OrderDish> saveAll(List<OrderDishRest> orderDishList) {
        orderDishList.forEach(orderDish -> {
            OrderDish od = restMapper.mapToOrderDish(orderDish);
            Order order = orderDao.findByReference(orderDish.getOrder().getReference());
            if(order.verifyQuantity()){
                orderDishDAO.saveAll(List.of(od));
                orderStatusDao.saveAll(order.getOrderStatusList());
                orderDishStatusDAO.saveAll(od.getOrderStatusList());
            }
        });
        return orderDishList
                .stream()
                .map(
                        orderDishRest -> restMapper
                                .mapToOrderDish(orderDishRest))
                .collect(Collectors.toList());
    }
}
