package torn.ando.gpizzasb.gpizza.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import torn.ando.gpizzasb.gpizza.enums.Unit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Component
public class Ingredient {
    private long id;
    private String name;
    private List<IngredientPrice> prices;
    @JsonIgnore
    private List<Stock> stockList;
    private Double availableQuantity;

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prices=" + prices +
                ", stockList=" + stockList +
                '}';
    }

    public Double getActualPrice() {
        if (prices == null || prices.isEmpty()) {
            return 0.0;
        }
        return prices.stream()
                .max(Comparator.comparing(IngredientPrice::getDateValue))
                .map(IngredientPrice::getPrice)
                .orElse(0.0);
    }

}
