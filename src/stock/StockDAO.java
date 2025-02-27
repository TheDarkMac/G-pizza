package stock;

import criteria.Criteria;
import datasource.DataSource;
import ingredient.Ingredient;

import java.util.Map;

public class StockDAO {
    DataSource ds = new DataSource();

    Criteria criteria = new Criteria("stock");

    public Map<Ingredient, Double> get(){
        throw new RuntimeException("not implemented yet");
    }
}
