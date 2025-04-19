package torn.ando.gpizzasb.gpizza.entityRest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import torn.ando.gpizzasb.gpizza.entity.Order;
import torn.ando.gpizzasb.gpizza.enums.OrderStatusType;
import torn.ando.gpizzasb.gpizza.mapper.Rest;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Component
public class DishRest implements Rest {
    private Long id;
    private Order order;
    private String name;
    private Double quantityOrdered;
    private OrderStatusType actualOrderStatus;
    private Double price;
    private List<DishIngredientRest> dishIngredientRestList;
}
