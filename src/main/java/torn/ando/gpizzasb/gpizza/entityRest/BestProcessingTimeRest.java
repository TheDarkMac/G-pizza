package torn.ando.gpizzasb.gpizza.entityRest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BestProcessingTimeRest {
    private String dish;
    private double preparationDuration;
    private String durationUnit;
}
