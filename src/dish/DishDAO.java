package dish;

import criteria.Criteria;
import datasource.DAOSchema;
import datasource.DataSource;
import ingredient.Ingredient;
import unit.Unit;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class DishDAO implements DAOSchema{
    DataSource ds = new DataSource();

    @Override
    public <T> boolean create(T object) {
        try(Connection connection = ds.getConnection()){

        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public <T> boolean update(T object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> boolean delete(int object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override //mbola mila dinihina
    public <T> List<T> findAll(int limit, int page) {
        List<Dish> dishes = new ArrayList<>();
        Dish dish = new Dish();
        String query = "SELECT\n" +
                "    dish.id_dish, " +
                "    ingredient.unit AS unit, " +
                "    ingredient.id_ingredient, " +
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
                "LIMIT ? OFFSET ?";
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, limit * (page - 1));
            ResultSet resultSet = preparedStatement.executeQuery();
            Map<Ingredient, Double> ingredients = new HashMap<>();
            while (resultSet.next()){
                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("id_ingredient"));
                ingredient.setName(resultSet.getString("ingredient_name"));
                ingredient.setIngredientCost(resultSet.getDouble("unit_price"));
                ingredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
                ingredients.put(ingredient, resultSet.getDouble("quantity"));
                dish.setId(resultSet.getInt("id_dish"));
                dish.setName(resultSet.getString("dish_name"));
            }
            dish.setIngredients(ingredients);
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return (List<T>) dishes;
    }

    @Override
    public <T> T findByName(String name, Optional<LocalDateTime> date) {
        Dish dish = new Dish();
        Criteria criteria = new Criteria("dish_ingredient");
        criteria.select("ingredient.unit AS unit",
                        "dish.id_dish",
                        "ingredient.id_ingredient",
                        "dish.name AS dish_name",
                        "ingredient.name AS ingredient_name",
                        "dish_ingredient.quantity AS quantity",
                        "ingredient_price_history.date_price AS date",
                        "ingredient_price_history.unit_price AS unit_price")
                .join("INNER", "dish", "dish_ingredient.id_dish = dish.id_dish")
                .join("INNER", "ingredient", "dish_ingredient.id_ingredient = ingredient.id_ingredient")
                .join("INNER", "ingredient_price_history", "ingredient_price_history.id_ingredient = ingredient.id_ingredient");

        if (!date.isPresent()) {
            Criteria sub = new Criteria("ingredient_price_history");
            sub.select("MAX(date_price)")
                    .and("ingredient_price_history.id_ingredient = ingredient.id_ingredient");
            criteria.and("dish.name ILIKE ?", "%"+name+"%");
        } else {
            criteria.and("dish.name ILIKE ?", "%"+name+"%")
                    .and("ingredient_price_history.date_price = ?", date.get());
        }

        String query = criteria.build();
        System.out.println(query);
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            List<Object> params = criteria.getParameters();
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));

            }

            ResultSet resultSet = preparedStatement.executeQuery();
            Map<Ingredient, Double> ingredients = new HashMap<>();
            while (resultSet.next()){
                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("id_ingredient"));
                ingredient.setName(resultSet.getString("ingredient_name"));
                ingredient.setIngredientCost(resultSet.getDouble("unit_price"));
                ingredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
                ingredient.setLastModification(resultSet.getTimestamp("date").toLocalDateTime());
                ingredients.put(ingredient, resultSet.getDouble("quantity"));
                dish.setId(resultSet.getInt("id_dish"));
                dish.setName(resultSet.getString("dish_name"));
            }
            dish.setIngredients(ingredients);
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T) dish;
    }
}
