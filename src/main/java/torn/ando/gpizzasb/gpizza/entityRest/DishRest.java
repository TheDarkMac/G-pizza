package torn.ando.gpizzasb.gpizza.entityRest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Component
public class DishRest {
    private Long id;
    private String name;
    private List<DishIngredientRest> dishIngredientRestList;
    private double price;
}
