package torn.ando.gpizzasb.gpizza.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import torn.ando.gpizzasb.gpizza.entityRest.BestProcessingTimeRest;

@Getter
@Setter
public class BestProcessingTimeResponse {
    private Instant updatedAt;
    private List<BestProcessingTimeRest> bestProcessingTimes;
}
