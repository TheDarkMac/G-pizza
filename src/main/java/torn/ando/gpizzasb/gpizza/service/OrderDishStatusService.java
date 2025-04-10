package torn.ando.gpizzasb.gpizza.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import torn.ando.gpizzasb.gpizza.dao.OrderDishStatusDAO;
import torn.ando.gpizzasb.gpizza.entity.OrderDishStatus;
import torn.ando.gpizzasb.gpizza.enums.OrderStatusType;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderDishStatusService {

    private OrderDishStatusDAO orderDishStatusDAO;

    public OrderDishStatus saveAll(List<OrderDishStatus> orderDishStatusList) {
        System.out.println(orderDishStatusList);
        orderDishStatusList.forEach(orderDishStatus -> {
            if(orderDishStatus.getOrderDish().getActualStatus() == OrderStatusType.CONFIRMED
            && orderDishStatus.getOrderStatus() == OrderStatusType.IN_PREPARATION){
                orderDishStatusDAO.saveAll(List.of(orderDishStatus));
            }else if(orderDishStatus.getOrderDish().getActualStatus() == OrderStatusType.IN_PREPARATION
                    && orderDishStatus.getOrderStatus() == OrderStatusType.DONE){
                orderDishStatusDAO.saveAll(List.of(orderDishStatus));
            }else if(orderDishStatus.getOrderDish().getActualStatus() == OrderStatusType.DONE
                    && orderDishStatus.getOrderStatus() == OrderStatusType.SERVED){
                orderDishStatusDAO.saveAll(List.of(orderDishStatus));
            }
            else {
                System.out.println("need to respect the order, chaos is not in our philosophy");
            }
        });
        return null;
    }
}
