package torn.ando.gpizzasb.gpizza.entityRest;

import lombok.Getter;
import lombok.Setter;
import torn.ando.gpizzasb.gpizza.entity.IngredientPrice;
import torn.ando.gpizzasb.gpizza.entity.Stock;
import torn.ando.gpizzasb.gpizza.enums.Unit;
import torn.ando.gpizzasb.gpizza.mapper.Rest;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class IngredientRest implements Rest {
    private Long id;
    private String name;
    private Double unitPrice;
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "IngredientRest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
