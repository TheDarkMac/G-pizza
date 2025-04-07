package torn.ando.gpizzasb.gpizza.entityRest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import torn.ando.gpizzasb.gpizza.entity.Ingredient;
import torn.ando.gpizzasb.gpizza.enums.MovementType;
import torn.ando.gpizzasb.gpizza.mapper.Rest;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockRest implements Rest {
    private Long id;
    private Double quantityINOUT;
    private MovementType movementType;
    private LocalDateTime dateOfMovement;
    private Ingredient ingredient;

    @Override
    public String toString() {
        return "StockRest{" +
                "id=" + id +
                ", quantityINOUT=" + quantityINOUT +
                ", movementType=" + movementType +
                ", dateOfMovement=" + dateOfMovement +
                ", ingredient=" + ingredient +
                '}';
    }
}
