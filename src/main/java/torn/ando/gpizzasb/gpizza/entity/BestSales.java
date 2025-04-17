package torn.ando.gpizzasb.gpizza.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
public class BestSales {
    private String dishName;
    private Integer quantity;
    private Double benefice;
}
