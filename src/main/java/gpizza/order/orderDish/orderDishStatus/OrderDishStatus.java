package gpizza.order.orderDish.orderDishStatus;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import gpizza.enums.OrderStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import gpizza.order.Order;
import gpizza.order.orderDish.OrderDish;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDishStatus implements Serializable {
    private long id;
    private Order order;
    @JsonIgnore
    @JsonBackReference
    private OrderDish orderDish;
    private OrderStatusType orderStatusType;
    private LocalDateTime updateAt;

    @Override
    public String toString() {
        return "OrderDishStatus{" +
                "id=" + id +
                ", order=" + order +
                ", orderDish=" + orderDish.getDishIngredient() +
                ", orderStatusType=" + orderStatusType +
                ", updateAt=" + updateAt +
                '}';
    }
}
