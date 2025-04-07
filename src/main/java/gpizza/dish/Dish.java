package gpizza.dish;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Dish implements Serializable {
    private long id_dish;
    private String name;
    private double unit_price;

    @Override
    public String toString() {
        return "Dish{" +
                "id_dish=" + id_dish +
                ", name='" + name + '\'' +
                ", unit_price=" + unit_price +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Dish dish = (Dish) object;
        return id_dish == dish.id_dish && Double.compare(unit_price, dish.unit_price) == 0 && Objects.equals(name, dish.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_dish, name, unit_price);
    }
}
