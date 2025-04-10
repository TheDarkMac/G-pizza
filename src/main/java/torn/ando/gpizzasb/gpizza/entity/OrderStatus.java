package torn.ando.gpizzasb.gpizza.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import torn.ando.gpizzasb.gpizza.enums.OrderStatusType;

import java.time.LocalDateTime;


@Getter
@Setter
@Component
public class OrderStatus {
    private String referenceOrder;
    private OrderStatusType orderStatus;
    private LocalDateTime datetime;

    @Override
    public String toString() {
        return "OrderStatus{" +
                ", referenceOrder='" + referenceOrder + '\'' +
                ", orderStatus=" + orderStatus +
                ", datetime=" + datetime +
                '}';
    }
}
