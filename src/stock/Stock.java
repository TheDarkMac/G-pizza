package stock;

import ingredient.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unit.MovementType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Stock {
    Double id_stock;
    Ingredient ingredients;
    Double quantity;
    MovementType movement_type;
    LocalDateTime lastMovement;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Stock stock = (Stock) object;
        return Objects.equals(id_stock, stock.id_stock) && Objects.equals(ingredients, stock.ingredients) && Objects.equals(quantity, stock.quantity) && movement_type == stock.movement_type && Objects.equals(lastMovement, stock.lastMovement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_stock, ingredients, quantity, movement_type, lastMovement);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id_stock=" + id_stock +
                ", ingredients=" + ingredients +
                ", quantity=" + quantity +
                ", movement_type=" + movement_type +
                ", lastMovement=" + lastMovement +
                '}';
    }
}
