package torn.ando.gpizzasb.gpizza.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Component
public class Dish {
    private long id;
    private String name;
    private double price;
    private List<DishIngredient> dishIngredientList;

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", dishIngredientList=" + dishIngredientList +
                '}';
    }
}
