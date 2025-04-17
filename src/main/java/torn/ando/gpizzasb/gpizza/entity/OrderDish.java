package torn.ando.gpizzasb.gpizza.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import torn.ando.gpizzasb.gpizza.enums.OrderStatusType;

import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Component
public class OrderDish {
    @JsonIgnore
    private Order order;
    private Dish dish;
    private Double quantity;
    @JsonIgnore
    private List<OrderDishStatus> orderStatusList;

    @Override
    public String toString() {
        return "OrderDish{" +
                ", dish=" + dish +
                ", quantity=" + quantity +
                ", orderStatusList=" + orderStatusList +
                '}';
    }

    public OrderStatusType getActualStatus(){
        if (orderStatusList == null || orderStatusList.isEmpty()) {
            return null;
        }
        return orderStatusList.stream()
                .max(Comparator.comparing(OrderDishStatus::getUpdateAt))
                .map(OrderDishStatus::getOrderStatus)
                .orElse(null);
    }
}
