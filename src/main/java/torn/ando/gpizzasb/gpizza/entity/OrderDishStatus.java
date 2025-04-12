package torn.ando.gpizzasb.gpizza.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private OrderDish orderDish;
    private OrderStatusType orderStatus;
    private LocalDateTime updateAt;

    @Override
    public String toString() {
        return "OrderDishStatus{" +
                ", orderDish=" + orderDish +
                ", orderStatus=" + orderStatus +
                ", updateAt=" + updateAt +
                '}';
    }
}
