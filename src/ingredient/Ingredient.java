package ingredient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stock.Stock;
import stock.StockDAO;
import unit.Unit;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Ingredient {
    private int id;
    private String name;
    private LocalDateTime lastModification;
    private double ingredientCost;
    private Unit unit;
    private HashMap<Timestamp, Double> price_at;
    private IngredientStock stock = new IngredientStock(this);

    public Ingredient(String name,Unit unit){
        this.name = name;
        this.unit = unit;
    }

    public Ingredient(String name, double ingredientCost, Unit unit) {
        this.name = name;
        this.ingredientCost = ingredientCost;
        this.unit = unit;
    }

    public Ingredient(int id, String name, LocalDateTime lastModification, double ingredientCost, Unit unit) {
        this.id = id;
        this.name = name;
        this.lastModification = lastModification;
        this.ingredientCost = ingredientCost;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Ingredient that = (Ingredient) object;
        return id == that.id && Double.compare(ingredientCost, that.ingredientCost) == 0 && Objects.equals(name, that.name) && Objects.equals(lastModification, that.lastModification) && unit == that.unit;
    }

    public Double getAvailableQuantity(){
        return stock.getQuantity();
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
                ", price at="+ price_at +
                '}';
    }
}
