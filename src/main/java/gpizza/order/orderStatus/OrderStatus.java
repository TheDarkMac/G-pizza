package gpizza.order.orderStatus;

import gpizza.enums.OrderStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderStatus implements Serializable {
    private long idOrderStatus;
    private String reference_order;
    private OrderStatusType orderStatus;
    private LocalDateTime updateAt;
}
