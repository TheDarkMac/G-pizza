package gpizza.stock;

import gpizza.enums.MovementType;
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
public class Stock implements Serializable {
    private long id_stock;
    private Ingredient ingredient;
    private double quantity;
    private LocalDateTime dateOfMovement;
    private MovementType movementType;

    @Override
    public String toString() {
        return "Stock{" +
                "id_stock=" + id_stock +
                ", ingredient=" + ingredient.getName() +
                ", quantity=" + quantity +
                ", dateOfMovement=" + dateOfMovement +
                ", movementType=" + movementType +
                '}';
    }
}
