package stock;

import criteria.CriteriaINSERT;
import criteria.CriteriaSELECT;
import datasource.DataSource;
import ingredient.Ingredient;
import unit.Unit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockDAO {
    DataSource ds = new DataSource();


    public boolean addStock(Stock stock,Map<String,Object> criteria) {
        CriteriaINSERT criteriaINSERT = new CriteriaINSERT("stock");
        if (criteria.containsKey("date_of_movement")){
            criteriaINSERT.insert("id_ingredient","quantity","movement_type","date_of_movement");
            criteriaINSERT.values(stock.getIngredients().getId(),stock.getQuantity(),stock.getMovement_type(),stock.getLastMovement() == null ? criteria.get("date_of_movement") : stock.getLastMovement());
        }else {
            criteriaINSERT.insert("id_ingredient","quantity","movement_type");
            criteriaINSERT.values(stock.getIngredients().getId(),stock.getQuantity(),stock.getMovement_type());

        }

        String query = criteriaINSERT.build();
        try (Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            List<Object> params = criteriaINSERT.getParameters();
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i+1,params.get(i),Types.OTHER);
            }
            boolean result = preparedStatement.execute();
            if (!result){
                return true;
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return true;
    }

    public List<Stock> getStockOf(Map<String,Object> criterias) {
        List<Stock> stocks = new ArrayList<>();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("stock");
        criteriaSELECT.select(
                "ingredient.id_ingredient",
                "ingredient.name AS ingredient_name",
                "ingredient.unit AS unit",
                "ingredient_price_history.unit_price AS unit_price",
                "ingredient_price_history.date_price AS date_price",
                "stock.id_stock AS id_stock",
                "stock.quantity AS quantity",
                "stock.date_of_movement AS date_of_movement"
        ).join("INNER","ingredient","ingredient.id_ingredient = stock.id_ingredient")
                .join("INNER","ingredient_price_history","ingredient_price_history.id_ingredient = ingredient.id_ingredient");

        if (criterias.containsKey("ingredient_name")){
            criteriaSELECT.and("ingredient.name ILIKE ?",criterias.get("ingredient_name")+"%");
        }

        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            List<Object> params = criteriaSELECT.getParameters();
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i), Types.OTHER);
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
                ingredient.setIngredientCost(resultSet.getDouble("unit_price"));
                ingredient.setLastModification(resultSet.getTimestamp("date_price").toLocalDateTime());
                stock.setIngredients(ingredient);
                stocks.add(stock);
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return stocks;
    }
}
