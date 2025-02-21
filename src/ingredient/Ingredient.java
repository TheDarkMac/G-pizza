package ingredient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unit.Unit;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Ingredient {
    private int id;
    private String name;
    private Timestamp lastModification;
    private double ingredientCost;
    private Unit unit;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Ingredient that = (Ingredient) object;
        return id == that.id && Double.compare(ingredientCost, that.ingredientCost) == 0 && Objects.equals(name, that.name) && Objects.equals(lastModification, that.lastModification) && unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastModification, ingredientCost, unit);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastModification=" + lastModification +
                ", ingredientCost=" + ingredientCost +
                ", unit=" + unit +
                '}';
    }
}
