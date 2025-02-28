package ingredient;

import criteria.CriteriaINSERT;
import criteria.CriteriaSELECT;
import datasource.DAOSchema;
import datasource.DataSource;
import unit.Unit;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IngredientDAO implements DAOSchema{
    private static DataSource ds = new DataSource();

    @Override
    public <T> boolean create(T object) {
        Ingredient ingredient = (Ingredient) object;
        CriteriaINSERT criteriaINSERT = new CriteriaINSERT("ingredient");
        criteriaINSERT.insert("name"," unit").values(ingredient.getName(),ingredient.getUnit());
        String query = criteriaINSERT.build();
        try (Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            List<Object> params = criteriaINSERT.getParameters();
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i),Types.OTHER);
            }
            boolean resultSet = preparedStatement.execute();
            if (resultSet) {
                throw new RuntimeException("Could not insert ingredient");
            }
            return true;
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> boolean update(T object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> boolean delete(int object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> List<T> findAll(int limit, int page) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T findByName(String name, Map<String, Object> criterias) {
        return null;
    }

    public static List<Ingredient> findAll(Map<String, Object> criterias) {
        List<Ingredient> ingredients = new ArrayList<>();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("ingredient");
        criteriaSELECT.select(
                "ingredient.id_ingredient AS id_ingredient",
                "ingredient.name AS ingredient_name",
                "ingredient.unit AS unit",
                "ingredient_price_history.date_price AS updatedAt",
                "ingredient_price_history.unit_price AS unit_price")
                .join("INNER","ingredient_price_history","ingredient_price_history.id_ingredient = ingredient.id_ingredient")

        ;
        if (criterias.containsKey("ingredient_name")){
            criteriaSELECT.and("ingredient.name ILIKE ? ", criterias.get("ingredient_name")+"%");
        }
        if(criterias.containsKey("unit")){
            criteriaSELECT.and("unit::text = ? ", criterias.get("unit"));
        }
        if(criterias.containsKey("unit_price")){
            List<Double> priceList = (List<Double>) criterias.get("unit_price");
            criteriaSELECT.andBetween("ingredient_price_history.unit_price", priceList.get(0), priceList.get(1));
        }
        if(criterias.containsKey("updatedAt")){
            List<LocalDateTime> dateTimeList = (List<LocalDateTime>) criterias.get("updatedAt");
            criteriaSELECT.andBetween("ingredient_price_history.date_price ", dateTimeList.get(0),dateTimeList.get(1));
        }
        if(criterias.containsKey("orderBy_name")){
            criteriaSELECT.orderBy("name", (Boolean) criterias.get("orderBy_name"));
        }
        if (criterias.containsKey("limite")){
            criteriaSELECT.limit((Integer) criterias.get("limit"));
        }
        if (criterias.containsKey("page")){
            int page = (Integer) criterias.get("page");
            int limit = (Integer) criterias.get("limit");
            page = ( page - 1 ) * limit;
            criteriaSELECT.offset( page );
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
                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("id_ingredient"));
                ingredient.setName(resultSet.getString("ingredient_name"));
                ingredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
                ingredient.setIngredientCost(resultSet.getDouble("unit_price"));
                ingredient.setLastModification(resultSet.getTimestamp("updatedAt").toLocalDateTime());
                ingredients.add(ingredient);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredients;
    }
}
