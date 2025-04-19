package torn.ando.gpizzasb.gpizza.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import torn.ando.gpizzasb.gpizza.enums.DurationUnit;

import java.time.Duration;

@Getter
@Setter
@Component
public class BestProcessingTime {
    private Long id;
    private Dish dish;
    private Duration preparationDuration ;
    private DurationUnit durationUnit;

}
