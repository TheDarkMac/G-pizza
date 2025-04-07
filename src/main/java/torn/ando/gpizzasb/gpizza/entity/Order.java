package torn.ando.gpizzasb.gpizza.entity;


import torn.ando.gpizzasb.gpizza.enums.OrderStatusType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@Component
public class Order {
    private long id;
    private String reference;
    private List<OrderDish> orderDishList;
    private List<OrderStatus> orderStatusList;
    private LocalDateTime orderDate;

    public Order(){
        orderStatusList = new ArrayList<>();
    }

    public OrderStatusType getActualStatus(){
        return orderStatusList.stream()
                .max(Comparator.comparing(OrderStatus::getDatetime))
                .map(OrderStatus::getOrderStatus)
                .orElse(null);
    }

    public Double getTotalPrice(){
       return orderDishList.stream()
               .map(orderDish ->{
                   return orderDish.getDish().getPrice()*orderDish.getQuantity();
               }).reduce(0.0, Double::sum);
    }

    public Boolean verifyQuantity(){
        orderDishList.forEach(orderDish -> {
            if(orderDish.getDish().getAvailableQuantity() - orderDish.getQuantity() < 0){
                throw new RuntimeException("not enough ingredient");
            }

        });
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setReferenceOrder(reference);
        orderStatus.setOrderStatus(OrderStatusType.CREATED);
        orderStatus.setDatetime(LocalDateTime.now());
        if (orderDishList.isEmpty()){
            orderStatusList.add(orderStatus);
            orderDishList = orderDishList.stream()
                    .map(orderDish -> {
                        OrderDishStatus orderDishStatus = new OrderDishStatus();
                        orderDishStatus.setUpdateAt(LocalDateTime.now());
                        orderDishStatus.setOrderDish(orderDish);
                        orderDishStatus.setOrderStatus(OrderStatusType.CREATED);
                        orderDish.setOrderStatusList(List.of(orderDishStatus));
                        return orderDish;
                    }).toList();
        }
        return true;
    }
}
