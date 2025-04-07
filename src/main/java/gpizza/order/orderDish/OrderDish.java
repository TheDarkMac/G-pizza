package gpizza.order.orderDish;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gpizza.dish.dishIngredient.DishIngredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import gpizza.order.Order;
import gpizza.order.orderDish.orderDishStatus.OrderDishStatus;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDish implements Serializable {
    private long id_order_dish;
    private Order order;
    private DishIngredient dishIngredient;
    private double quantity;
    private List<OrderDishStatus> statusList;

    @Override
    public String toString() {
        return "OrderDish{" +
                "id_order_dish=" + id_order_dish +
                ", order=" + order.getReference() +
                ", dishIngredient=" + dishIngredient +
                ", quantity=" + quantity +
                ", statusList=" + statusList +
                '}';
    }
}
