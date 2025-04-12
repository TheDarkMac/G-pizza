package torn.ando.gpizzasb.gpizza.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private List<OrderStatus> orderStatusList = new ArrayList<>();
    private LocalDateTime orderDate;

    public OrderStatusType getActualStatus(){
        if (orderStatusList == null || orderStatusList.isEmpty()) {
            System.out.println("il y ya une valeur null");
            return null;
        }
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
        return true;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", orderDishList=" + orderDishList +
                ", orderStatusList=" + orderStatusList +
                ", orderDate=" + orderDate +
                '}';
    }
}
