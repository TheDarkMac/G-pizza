package torn.ando.gpizzasb.gpizza.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Component
public class OrderDish {
    private long id;
    @JsonIgnore
    private Order order;
    private Dish dish;
    private Double quantity;
    private List<OrderDishStatus> orderStatusList;

    @Override
    public String toString() {
        return "OrderDish{" +
                "id=" + id +
                ", dish=" + dish +
                ", quantity=" + quantity +
                ", orderStatusList=" + orderStatusList +
                '}';
    }
}
