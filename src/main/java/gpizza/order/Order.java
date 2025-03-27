package gpizza.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import gpizza.order.orderDish.OrderDish;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order implements Serializable {
    private long id_order;
    private String reference;
    private LocalDateTime order_datetime;
    private List<OrderDish> orderDishList;

    @Override
    public String toString() {
        return "Order{" +
                "id_order=" + id_order +
                ", reference='" + reference + '\'' +
                ", order_datetime=" + order_datetime +
                ", orderDishList=" + orderDishList +
                '}';
    }
}
