package entityTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stock.Stock;
import stock.StockDAO;

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
    }
}
