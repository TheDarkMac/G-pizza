package torn.ando.gpizzasb.gpizza.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Component
public class Dish {
    private long id;
    private String name;
    private double price;
    @JsonIgnore
    private List<DishIngredient> dishIngredientList = new ArrayList<>();

    public Integer getAvailableQuantity(){
        if(dishIngredientList.isEmpty()){
            return 0;
        }
        List<Integer> numberOfDish = new ArrayList<>();
        dishIngredientList.forEach(dishIngredient -> {
            Integer dishmake = (int) (dishIngredient.getIngredient().getAvailableQuantity()/dishIngredient.getRequiredQuantity());
            numberOfDish.add(dishmake);
        });

        return numberOfDish.stream().min(Integer::compareTo).get();
    }

    public Double getGrossMargin(){
        if(dishIngredientList.isEmpty() || dishIngredientList == null){
            return price;
        }
        return price - dishIngredientList.stream()
                .map(dishIngredient -> {
                    return dishIngredient.getRequiredQuantity()*dishIngredient.getIngredient().getActualPrice();})
                .reduce(Double::sum)
                .get();
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
