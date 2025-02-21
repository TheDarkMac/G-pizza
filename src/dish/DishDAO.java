package dish;

import datasource.DAOSchema;
import datasource.DataSource;
import ingredient.Ingredient;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DishDAO implements DAOSchema{
    DataSource ds = new DataSource();

    @Override
    public <T> boolean create(T object) {
        return false;
    }

    @Override
    public <T> boolean update(T object) {
        return false;
    }

    @Override
    public <T> boolean delete(int object) {
        return false;
    }

    @Override
    public <T> List<T> findAll(int limit, int page) {
        return List.of();
    }

    @Override
    public <T> T findByName(String name) {
        Dish dish = new Dish();
        String query = "SELECT\n" +
                "    dish.id_dish,"+
                "    ingredient.id_ingredient,"+
                "    dish.name AS dish_name,\n" +
                "    ingredient.name AS ingredient_name,\n" +
                "    dish_ingredient.quantity AS quantity,\n" +
                "    ingredient_price_history.unit_price AS unit_price\n" +
                "FROM dish_ingredient\n" +
                "INNER JOIN dish ON dish_ingredient.id_dish = dish.id_dish\n" +
                "INNER JOIN ingredient ON dish_ingredient.id_ingredient = ingredient.id_ingredient\n" +
                "INNER JOIN ingredient_price_history ON ingredient_price_history.id_ingredient = ingredient.id_ingredient\n" +
                "    AND ingredient_price_history.date_price = (\n" +
                "        SELECT MAX(date_price)\n" +
                "        FROM ingredient_price_history\n" +
                "        WHERE id_ingredient = ingredient.id_ingredient\n" +
                "    )\n" +
                "WHERE dish.name ILIKE ? ";
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%"+name+"%");
            ResultSet resultSet = preparedStatement.executeQuery();
            Map<Ingredient, Double> ingredients = new HashMap<>();
            while (resultSet.next()){
                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("id_ingredient"));
                ingredient.setName(resultSet.getString("ingredient_name"));
                ingredient.setIngredientCost(resultSet.getDouble("unit_price"));
                ingredients.put(ingredient, resultSet.getDouble("quantity"));
                dish.setId(resultSet.getInt("id_dish"));
                dish.setName(resultSet.getString("dish_name"));
            }
            dish.setIngredients(ingredients);
            connection.close();
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T) dish;
    }
}
