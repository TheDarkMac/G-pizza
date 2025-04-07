package torn.ando.gpizzasb.gpizza.entity;


import torn.ando.gpizzasb.gpizza.enums.OrderStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Component
public class Order {
    private long id;
    private String reference;
    private List<OrderDish> orderDishList;
    private List<OrderStatusType> orderStatusList;
    private LocalDateTime orderDate;
}
