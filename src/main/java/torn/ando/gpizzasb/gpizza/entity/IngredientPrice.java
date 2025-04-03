package torn.ando.gpizzasb.gpizza.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@Component
public class IngredientPrice {
    private long id;
    private double price;
    @JsonIgnore
    private Ingredient ingredient;
    private LocalDateTime dateValue;

    @Override
    public String toString() {
        return "IngredientPrice{" +
                "id=" + id +
                ", price=" + price +
                ", dateValue=" + dateValue +
                '}';
    }
}
