package torn.ando.gpizzasb.gpizza.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Component
public class BestSales {
    private String reference;
    private String dishName;
    private Integer quantity;
    private Long dishId;
    private LocalDate from;
    private LocalDate to;
    private Double benefice;

    @Override
    public String toString() {
        return "BestSales{" +
                "reference='" + reference + '\'' +
                ", dishName='" + dishName + '\'' +
                ", quantity=" + quantity +
                ", dishId=" + dishId +
                ", from=" + from +
                ", to=" + to +
                ", benefice=" + benefice +
                '}';
    }
}
