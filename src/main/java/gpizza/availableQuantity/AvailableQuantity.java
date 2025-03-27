package gpizza.availableQuantity;

import gpizza.ingredient.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AvailableQuantity implements Serializable {
    private Ingredient ingredient;
    private double quantity;
    private LocalDateTime date_of_last_movement;

    @Override
    public String toString() {
        return "AvailableQuantity{" +
                "ingredient=" + ingredient.getName() +
                ", quantity=" + quantity +
                ", date_of_last_movement=" + date_of_last_movement +
                '}';
    }
}
