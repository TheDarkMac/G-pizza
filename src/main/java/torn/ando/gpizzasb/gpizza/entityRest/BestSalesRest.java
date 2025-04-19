package torn.ando.gpizzasb.gpizza.entityRest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Component
public class BestSalesRest {
    private Integer size;
    private String reference;
    private String dishName;
    private Long dishId;
    private Long quantity;
    private LocalDate from;
    private LocalDate to;
}
