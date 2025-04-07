package gpizza.ingredient.price.history;

import gpizza.ingredient.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IngredienPriceHistory implements Serializable {
    private long id_price;
    private Ingredient ingredient;
    private LocalDate datePrice;
    private double unitPrice;

    @Override
    public String toString() {
        return "IngredienPriceHistory{" +
                "id_price=" + id_price +
                ", ingredient=" + ingredient.getName() +
                ", datePrice=" + datePrice +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
