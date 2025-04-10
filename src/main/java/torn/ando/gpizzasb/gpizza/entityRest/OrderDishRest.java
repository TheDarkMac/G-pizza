package torn.ando.gpizzasb.gpizza.entityRest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import torn.ando.gpizzasb.gpizza.entity.Dish;
import torn.ando.gpizzasb.gpizza.entity.Order;
import torn.ando.gpizzasb.gpizza.entity.OrderDishStatus;
import torn.ando.gpizzasb.gpizza.mapper.Rest;

import java.util.List;

@Getter
@Setter
@Component
public class OrderDishRest implements Rest {
    private Long id;
    private Order order;
    private String name;
    private Double quantity;
}
