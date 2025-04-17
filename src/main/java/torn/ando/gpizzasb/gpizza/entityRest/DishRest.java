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
    private double price;
    private List<DishIngredientRest> dishIngredientRestList;

    @Override
    public String toString() {
        return "DishRest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishIngredientRestList=" + dishIngredientRestList +
                ", price=" + price +
                '}';
    }
}
