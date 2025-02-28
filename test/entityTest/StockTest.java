package entityTest;

import ingredient.Ingredient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stock.Stock;
import stock.StockDAO;
import unit.MovementType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockTest {
    @Test
    public void testStock() {
        StockDAO stockDAO = new StockDAO();
        Map<String, Object> params = new HashMap<>();
        params.put("ingredient_name", "hui");
        List<Stock> stocks = stockDAO.getStockOf(params);
        Assertions.assertNotNull(stocks);
        Assertions.assertEquals(1, stocks.size());
        Assertions.assertEquals(20.0,stocks.get(0).getQuantity());
    }

    @Test
    void insert_stock(){
        StockDAO stockDAO = new StockDAO();
        Map<String, Object> params = new HashMap<>();
        params.put("ingredient_name", "hui");
        List<Stock> stocks = stockDAO.getStockOf(params);
        Ingredient ingredient = stocks.get(0).getIngredients();
        System.out.println(ingredient);
        Stock stock = new Stock();
        stock.setIngredients(ingredient);
        stock.setQuantity(28.0);
        stock.setMovement_type(MovementType.IN);
        stockDAO.addStock(stock,new HashMap<>());
    }
}
