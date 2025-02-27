package ingredient;

import criteria.Criteria;
import datasource.DAOSchema;
import datasource.DataSource;
import org.jetbrains.annotations.Nullable;
import unit.Unit;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class IngredientDAO implements DAOSchema{
    private static DataSource ds = new DataSource();

    @Override
    public <T> boolean create(T object) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public <T> T findByName(String name, @Nullable Optional<LocalDateTime> date) {
        return null;
    }

    public static List<Ingredient> findAll(Map<String, Object> criterias, int limit, int page) {
        List<Ingredient> ingredients = new ArrayList<>();
        Criteria criteria = new Criteria("ingredient");
        criteria.select(
                "ingredient.id_ingredient AS id_ingredient",
                "ingredient.name AS ingredient_name",
                "ingredient.unit AS unit",
                "ingredient_price_history.date_price AS updatedAt",
                "ingredient_price_history.unit_price AS unit_price")
                .join("INNER","ingredient_price_history","ingredient_price_history.id_ingredient = ingredient.id_ingredient")
                .limit(limit)
                .offset( (page - 1) * limit)
        ;
        if (criterias.containsKey("ingredient_name")){
            criteria.and("ingredient.name ILIKE ? ", criterias.get("ingredient_name"));
        }

        String query = criteria.build();
        System.out.println(query);
        try(Connection connection = ds.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            List<Object> params = criteria.getParameters();
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
