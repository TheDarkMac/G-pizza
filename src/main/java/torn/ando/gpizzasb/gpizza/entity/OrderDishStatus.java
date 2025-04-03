package torn.ando.gpizzasb.gpizza.entity;

import torn.ando.gpizzasb.gpizza.enums.OrderStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Component
public class OrderDishStatus {
    private long id;
    private OrderDish orderDish;
    private OrderStatusType orderStatus;
    private LocalDateTime updateAt;

    @Override
    public String toString() {
        return "OrderDishStatus{" +
                "id=" + id +
                ", orderDish=" + orderDish +
                ", orderStatus=" + orderStatus +
                ", updateAt=" + updateAt +
                '}';
    }
}
