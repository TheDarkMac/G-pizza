package torn.ando.gpizzasb.gpizza.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    public Integer getAvailableQuantity(){
        List<Integer> numberOfDish = new ArrayList<>();
        dishIngredientList.forEach(dishIngredient -> {
            Integer dishmake = (int) (dishIngredient.getIngredient().getAvailableQuantity()/dishIngredient.getRequiredQuantity());
            numberOfDish.add(dishmake);
        });

        return numberOfDish.stream().min(Integer::compareTo).get();
    }

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
