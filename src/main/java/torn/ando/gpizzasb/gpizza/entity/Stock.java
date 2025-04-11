package torn.ando.gpizzasb.gpizza.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import torn.ando.gpizzasb.gpizza.enums.MovementType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Component
public class Stock {
    private long id;
    @JsonIgnore
    private Ingredient ingredient;
    private double quantityINOUT;
    private MovementType movementType;
    private LocalDateTime dateOfMovement;

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", ingredient=" + ingredient +
                ", quantityINOUT=" + quantityINOUT +
                ", movementType=" + movementType +
                ", dateOfMovement=" + dateOfMovement +
                '}';
    }
}
