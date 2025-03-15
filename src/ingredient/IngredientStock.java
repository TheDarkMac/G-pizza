package ingredient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stock.Stock;
import stock.StockDAO;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IngredientStock {
    Ingredient ingredient;
    double quantity;

    public IngredientStock(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    private double getAvailableQuantity() {
        StockDAO stockDAO = new StockDAO();
        List<Stock> stockList = stockDAO.getStockOf(new HashMap<>());
        AtomicReference<Double> result = new AtomicReference<>(0.0);
        stockList.forEach(stock -> {
            if(stock.getIngredients().getName().equals(this.ingredient.getName())){
                result.set(stock.getQuantity());
            }
        });
        return result.get();
    }

    public double getQuantity(){
        return getAvailableQuantity();
    }

}
