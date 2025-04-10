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
import torn.ando.gpizzasb.gpizza.entity.OrderStatus;
import torn.ando.gpizzasb.gpizza.entityRest.OrderDishRest;
import torn.ando.gpizzasb.gpizza.enums.OrderStatusType;
import torn.ando.gpizzasb.gpizza.mapper.RestMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OrderDishService {

    private OrderDao orderDao;
    private OrderStatusDAO orderStatusDao;
    private OrderDishDAO orderDishDAO;
    private OrderDishStatusDAO orderDishStatusDAO;

    public List<OrderDish> saveAll(List<OrderDish> orderDishList) {
        orderDishList.forEach(orderDish -> {
            Order order = orderDao.findByReference(orderDish.getOrder().getReference());
            List<OrderDishStatus> orderDishStatusList = orderDishStatusDAO.findByReferenceAndDishId(order.getReference(),orderDish.getDish().getId());
            if(orderDishStatusList != null) {
                orderDish.setOrderStatusList(orderDishStatusList);
            }
            order.setOrderDishList(List.of(orderDish));
            if(order.verifyQuantity()){
                orderDishDAO.saveAll(order.getOrderDishList());
                if(order.getOrderStatusList().isEmpty()){
                    OrderStatus orderStatus = new OrderStatus();
                    orderStatus.setDish(orderDish.getDish());
                    orderStatus.setOrderStatus(OrderStatusType.CREATED);
                    orderStatus.setReferenceOrder(orderDish.getOrder().getReference());
                    orderStatus.setDatetime(LocalDateTime.now());
                    orderStatusDao.saveAll(List.of(orderStatus));
                }else{
                    if(order.getActualStatus() == OrderStatusType.CREATED){
                        OrderStatus orderStatus = new OrderStatus();
                        orderStatus.setDish(orderDish.getDish());
                        orderStatus.setOrderStatus(OrderStatusType.CONFIRMED);
                        orderStatus.setReferenceOrder(orderDish.getOrder().getReference());
                        orderStatus.setDatetime(LocalDateTime.now());
                        orderStatusDao.saveAll(List.of(orderStatus));
                    }
                }
                orderStatusDao.saveAll(order.getOrderStatusList());
            }
            if(orderDish.getOrderStatusList()==null){
                OrderDishStatus orderDishStatus = new OrderDishStatus();
                orderDishStatus.setOrderDish(orderDish);
                orderDishStatus.setOrderStatus(OrderStatusType.CREATED);
                orderDishStatus.setUpdateAt(LocalDateTime.now());
                orderDishStatusDAO.saveAll(List.of(orderDishStatus));
            }else {
                if(orderDish.getActualStatus() == OrderStatusType.CREATED){
                    OrderDishStatus orderDishStatus = new OrderDishStatus();
                    orderDishStatus.setOrderDish(orderDish);
                    orderDishStatus.setOrderStatus(OrderStatusType.CONFIRMED);
                    orderDishStatus.setUpdateAt(LocalDateTime.now());
                    orderDishStatusDAO.saveAll(List.of(orderDishStatus));
                }
            }
        });
        return orderDishList;
    }
}
