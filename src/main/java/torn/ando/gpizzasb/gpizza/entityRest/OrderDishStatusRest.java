package torn.ando.gpizzasb.gpizza.entityRest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import torn.ando.gpizzasb.gpizza.entity.OrderDish;
import torn.ando.gpizzasb.gpizza.enums.OrderStatusType;

import java.time.LocalDateTime;

@Setter
@Getter
@Component
public class OrderDishStatusRest {
    private String orderReference;
    private Long dishId;
    private OrderStatusType orderStatus;
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "OrderDishStatusRest{" +
                "orderReference='" + orderReference + '\'' +
                ", dishId=" + dishId +
                ", orderStatus=" + orderStatus +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
