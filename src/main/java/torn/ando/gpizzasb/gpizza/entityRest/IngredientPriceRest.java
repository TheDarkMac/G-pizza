package torn.ando.gpizzasb.gpizza.entityRest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import torn.ando.gpizzasb.gpizza.mapper.Rest;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientPriceRest implements Rest {
    private Long id;
    private IngredientRest ingredientRest;
    private Double price;
    private LocalDateTime dateValue;

    @Override
    public String toString() {
        return "IngredientPriceRest{" +
                "id=" + id +
                ", ingredientRest=" + ingredientRest +
                ", price=" + price +
                ", updateAt=" + dateValue +
                '}';
    }
}
