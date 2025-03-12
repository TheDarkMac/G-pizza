package entityTest;

import ingredient.Ingredient;
import ingredient.IngredientDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stock.Stock;
import stock.StockDAO;
import unit.MovementType;

import java.time.LocalDateTime;
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
        Assertions.assertEquals(20.0, stocks.get(0).getQuantity());
    }

    @Test
    public void insertRizAndSel(){
        IngredientDAO ingredientDAO = new IngredientDAO();
        Map<String, Object> params = new HashMap<>();
        params.put("ingredient_name", "sel");
        Ingredient sel = ingredientDAO.findByName(params);
        Map<String, Object> params2 = new HashMap<>();
        params2.put("ingredient_name","riz");
        Ingredient riz = ingredientDAO.findByName(params2);
        StockDAO stockDAO = new StockDAO();
        Stock stockriz = new Stock();
        stockriz.setIngredients(riz);
        stockriz.setQuantity(10000.0);
        stockriz.setMovement_type(MovementType.IN);
        //stockDAO.addStock(stockriz,new HashMap<>());

        Stock stocksel = new Stock();
        stocksel.setIngredients(sel);
        stocksel.setQuantity(1000.0);
        stocksel.setMovement_type(MovementType.IN);
      //  stockDAO.addStock(stocksel,new HashMap<>());

        Assertions.assertEquals(1000.0, sel.getAvailableQuantity());
        Assertions.assertEquals(10000.0, riz.getAvailableQuantity());
    }
}
//    @Test
//    void insert_stock(){
//        StockDAO stockDAO = new StockDAO();
//        Map<String, Object> params = new HashMap<>();
//        params.put("ingredient_name", "hui");
//        List<Stock> stocks = stockDAO.getStockOf(params);
//        Ingredient ingredient = stocks.get(0).getIngredients();
//        System.out.println(ingredient);
//        Stock stock = new Stock();
//        stock.setIngredients(ingredient);
//        stock.setQuantity(28.0);
//        stock.setMovement_type(MovementType.IN);
//        stockDAO.addStock(stock,new HashMap<>());
//    }



//    @Test
//    void eggfarany() {
//        StockDAO stockDAO = new StockDAO();
//        IngredientDAO ingredientDAO = new IngredientDAO();
//        Map<String, Object> params = new HashMap<>();
//        params.put("ingredient_name", "oeuf");
//        Ingredient oeuf = ingredientDAO.findByName(params);
//        Stock stock = new Stock();
//        stock.setIngredients(oeuf);
//        stock.setQuantity(80.0);
//        stock.setMovement_type(MovementType.OUT);
//        stock.setLastMovement(LocalDateTime.of(2025, 2, 24, 15, 0));
//        Map<String, Object> date = new HashMap<>();
//        date.put("date_of_movement",null);
//        stockDAO.addStock(stock, date);
//    }
//
//    @Test
//    void painfarany() {
//        StockDAO stockDAO = new StockDAO();
//        IngredientDAO ingredientDAO = new IngredientDAO();
//        Map<String, Object> params = new HashMap<>();
//        params.put("ingredient_name", "pain");
//        Ingredient pain = ingredientDAO.findByName(params);
//        Stock stock = new Stock();
//        stock.setIngredients(pain);
//        stock.setQuantity(80.0);
//        stock.setMovement_type(MovementType.OUT);
//        stock.setLastMovement(LocalDateTime.of(2025, 2, 5, 16, 0));
//        Map<String, Object> date = new HashMap<>();
//        date.put("date_of_movement", null);
//        stockDAO.addStock(stock, date);
//    }
//
//    @Test
//    void saucissefarany() {
//        StockDAO stockDAO = new StockDAO();
//        IngredientDAO ingredientDAO = new IngredientDAO();
//        Map<String, Object> params = new HashMap<>();
//        params.put("ingredient_name", "sauc");
//        Ingredient oeuf = ingredientDAO.findByName(params);
//        Stock stock = new Stock();
//        stock.setIngredients(oeuf);
//        stock.setQuantity(10000.0);
//        stock.setMovement_type(MovementType.OUT);
//        stock.setLastMovement(LocalDateTime.of(2025, 2, 24, 15, 0));
//        Map<String, Object> date = new HashMap<>();
//        date.put("date_of_movement",null);
//        stockDAO.addStock(stock, date);
//    }
//
//    @Test
//    void huilefarany() {
//        StockDAO stockDAO = new StockDAO();
//        IngredientDAO ingredientDAO = new IngredientDAO();
//        Map<String, Object> params = new HashMap<>();
//        params.put("ingredient_name", "hui");
//        Ingredient pain = ingredientDAO.findByName(params);
//        Stock stock = new Stock();
//        stock.setIngredients(pain);
//        stock.setQuantity(20.0);
//        stock.setMovement_type(MovementType.OUT);
//        stock.setLastMovement(LocalDateTime.of(2025, 2, 5, 24, 0));
//        Map<String, Object> date = new HashMap<>();
//        date.put("date_of_movement", null);
//        stockDAO.addStock(stock, date);
//    }
//    }
