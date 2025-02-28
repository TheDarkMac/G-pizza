package stock;

import criteria.CriteriaSELECT;
import datasource.DataSource;
import ingredient.Ingredient;
import unit.Unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockDAO {
    DataSource ds = new DataSource();

    CriteriaSELECT criteriaSELECT = new CriteriaSELECT("stock");

    public List<Stock> getStockOf(Map<String,Object> criterias) {
        List<Stock> stocks = new ArrayList<>();

        criteriaSELECT.select(
                "ingredient.id_ingredient",
                "ingredient.name AS ingredient_name",
                "ingredient.unit AS unit",
                "stock.id_stock AS id_stock",
                "stock.quantity AS quantity",
                "stock.date_of_movement AS date_of_movement"
        ).join("INNER","ingredient","ingredient.id_ingredient = stock.id_ingredient");

        if (criterias.containsKey("ingredient_name")){
            criteriaSELECT.and("ingredient.name ILIKE ?",criterias.get("ingredient_name")+"%");
        }

        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            List<Object> params = criteriaSELECT.getParameters();
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Stock stock = new Stock();
                stock.setId_stock(resultSet.getDouble("id_stock"));
                stock.setLastMovement(resultSet.getTimestamp("date_of_movement").toLocalDateTime());
                stock.setQuantity(resultSet.getDouble("quantity"));
                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("id_ingredient"));
                ingredient.setName(resultSet.getString("ingredient_name"));
                ingredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
                stock.setIngredients(ingredient);
                stocks.add(stock);
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return stocks;
    }
}
