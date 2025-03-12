package ingredient;

import criteria.CriteriaINSERT;
import criteria.CriteriaSELECT;
import datasource.DAOSchema;
import datasource.DataSource;
import unit.Unit;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class IngredientDAO implements DAOSchema{
    private static DataSource ds = new DataSource();

    @Override
    public <T> boolean create(T object) {
        Ingredient ingredient = (Ingredient) object;
        CriteriaINSERT criteriaIngredient = new CriteriaINSERT("ingredient");
        criteriaIngredient.insert("name"," unit").values(ingredient.getName(),ingredient.getUnit());
        String query = criteriaIngredient.build();
        try (Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            List<Object> params = criteriaIngredient.getParameters();
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i),Types.OTHER);
            }
            boolean resultSet = preparedStatement.execute();
            if (!resultSet) {
                CriteriaINSERT criteriaInsertPrice = new CriteriaINSERT("ingredient_price_history");
                Map<String, Object> criterias_1 = new HashMap<>();
                criterias_1.put("ingredient_name", ingredient.getName());
                criterias_1.put("fields", Arrays.asList("ingredient.id_ingredient AS id_ingredient",
                        "ingredient.name AS ingredient_name",
                        "ingredient.unit AS unit"));
                Ingredient ingredient1 = findByName(criterias_1);
                criteriaInsertPrice.insert("id_ingredient","unit_price")
                        .values(ingredient1.getId(),ingredient.getIngredientCost());
                String query1 = criteriaInsertPrice.build();
                try (Connection connection1 = ds.getConnection()){
                    PreparedStatement preparedStatement1 = connection1.prepareStatement(query1);
                    List<Object> params1 = criteriaInsertPrice.getParameters();
                    for (int i = 0; i < params.size(); i++) {
                        preparedStatement1.setObject(i + 1, params1.get(i),Types.OTHER);
                    }
                    Boolean resultSet1 = preparedStatement1.execute();
                    if (!resultSet1) {
                        return true;
                    }
                    throw new RuntimeException("ingredient_price_history was not affected");
                }
            }
            throw new RuntimeException("Could not insert ingredient");
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
    public <T> T findByName(Map<String, Object> criterias) {
        List<Ingredient> ingredients = new ArrayList<>();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("ingredient");
        if (criterias.containsKey("fields")){
            List<String> fields = (List<String>) criterias.get("fields");
            String selectQuery = String.join(",",fields);
            criteriaSELECT.select(selectQuery);
        }else {
            criteriaSELECT.select("ingredient.id_ingredient AS id_ingredient",
                    "ingredient.name AS ingredient_name",
                    "ingredient.unit AS unit",
                    "ingredient_price_history.date_price AS updatedAt",
                    "ingredient_price_history.unit_price AS unit_price")
                    .join("INNER", "ingredient_price_history", "ingredient_price_history.id_ingredient = ingredient.id_ingredient");
        }
        if (criterias.containsKey("ingredient_name")) {
            criteriaSELECT.and("ingredient.name ILIKE ? ", "%"+criterias.get("ingredient_name")+"%");
        }else {
            throw new RuntimeException("the name of ingredient does not provide");
        }
        String query = criteriaSELECT.build();
        try (Connection connection = ds.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            List<Object> params = criteriaSELECT.getParameters();
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i),Types.OTHER);
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            Ingredient ingredient = new Ingredient();
            while (resultSet.next()) {
                ingredient.setId(resultSet.getInt("id_ingredient"));
                ingredient.setName(resultSet.getString("ingredient_name"));
                ingredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
                if(!criterias.containsKey("fields")){
                    ingredient.setIngredientCost(resultSet.getDouble("unit_price"));
                    ingredient.setLastModification(resultSet.getTimestamp("updatedAt").toLocalDateTime());
                }
                ingredients.add(ingredient);
            }
            return (T) ingredient;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T findById(Map<String, Object> criterias) {
        List<Ingredient> ingredients = new ArrayList<>();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("ingredient");
        criteriaSELECT.select("ingredient.id_ingredient AS id_ingredient",
                        "ingredient.name AS ingredient_name",
                        "ingredient.unit AS unit",
                        "ingredient_price_history.date_price AS updatedAt",
                        "ingredient_price_history.unit_price AS unit_price")
                .join("INNER", "ingredient_price_history", "ingredient_price_history.id_ingredient = ingredient.id_ingredient");
        if (criterias.containsKey("id_ingredient")) {
            criteriaSELECT.and("ingredient.name ILIKE ? ", "%"+criterias.get("ingredient_name")+"%");
        }else {
            throw new RuntimeException("the id to search is not present");
        }
        String query = criteriaSELECT.build();
        try (Connection connection = ds.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            List<Object> params = criteriaSELECT.getParameters();
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i),Types.OTHER);
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            Ingredient ingredient = new Ingredient();
            while (resultSet.next()) {
                ingredient.setId(resultSet.getInt("id_ingredient"));
                ingredient.setName(resultSet.getString("ingredient_name"));
                ingredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
                ingredient.setIngredientCost(resultSet.getDouble("unit_price"));
                ingredient.setLastModification(resultSet.getTimestamp("updatedAt").toLocalDateTime());
                ingredients.add(ingredient);
            }
            return (T) ingredient;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            criteriaSELECT.orderBy("name", criterias.get("orderBy_name")=="ASC"? true:false);
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
                preparedStatement.setObject(i + 1, params.get(i),Types.OTHER);
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
