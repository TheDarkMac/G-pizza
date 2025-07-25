package dish;

import criteria.CriteriaSELECT;
import datasource.DAOSchema;
import datasource.DataSource;
import ingredient.Ingredient;
import ingredientdish.IngredientDish;
import unit.Unit;

import java.sql.*;
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
            List<IngredientDish> ingredientDishes = new ArrayList<>();
            while (resultSet.next()){
                IngredientDish ingredientDish = new IngredientDish();
                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("id_ingredient"));
                ingredient.setName(resultSet.getString("ingredient_name"));
                ingredient.setIngredientCost(resultSet.getDouble("unit_price"));
                ingredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
                ingredientDish.setIngredients(ingredient);
                ingredientDish.setRequiredQuantity(resultSet.getDouble("quantity"));
                ingredientDishes.add(ingredientDish);
                dish.setId(resultSet.getInt("id_dish"));
                dish.setName(resultSet.getString("dish_name"));
                ingredientDish.setDish(dish);
            }
            dish.setIngredients(ingredientDishes);
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return (List<T>) dishes;
    }

    @Override
    public <T> T findByName(Map<String, Object> criterias) {
        Dish dish = new Dish();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("dish_ingredient");
        criteriaSELECT.select("ingredient.unit AS unit",
                        "dish.id_dish",
                        "dish.name AS dish_name",
                        "dish.unit_price AS selling_price",
                        "ingredient.id_ingredient",
                        "ingredient.name AS ingredient_name",
                        "dish_ingredient.quantity AS quantity",
                        "ingredient_price_history.date_price AS date",
                        "ingredient_price_history.unit_price AS unit_price")
                .join("INNER", "dish", "dish_ingredient.id_dish = dish.id_dish")
                .join("INNER", "ingredient", "dish_ingredient.id_ingredient = ingredient.id_ingredient")
                .join("INNER", "ingredient_price_history", "ingredient_price_history.id_ingredient = ingredient.id_ingredient");

        if (!criterias.containsKey("date")) {
            CriteriaSELECT sub = new CriteriaSELECT("ingredient_price_history");
            sub.select("MAX(date_price)")
                    .and("ingredient_price_history.id_ingredient = ingredient.id_ingredient");
            criteriaSELECT.and("dish.name ILIKE ?", "%"+criterias.get("dish_name")+"%");
        } else {
            List< LocalDateTime > dateTimeList = (List<LocalDateTime>) criterias.get("date");
            criteriaSELECT.and("dish.name ILIKE ?", "%"+criterias.get("dish_name")+"%")
                    .andBetween("ingredient_price_history.date_price", dateTimeList.get(0), dateTimeList.get(1));
        }

        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            List<Object> params = criteriaSELECT.getParameters();
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            List<IngredientDish> ingredients = new ArrayList<>();
            while (resultSet.next()){
                IngredientDish ingredientDish = new IngredientDish();
                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("id_ingredient"));
                ingredient.setName(resultSet.getString("ingredient_name"));
                ingredient.setIngredientCost(resultSet.getDouble("unit_price"));
                ingredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
                ingredient.setLastModification(resultSet.getTimestamp("date").toLocalDateTime());
                ingredientDish.setRequiredQuantity(resultSet.getDouble("quantity"));
                ingredientDish.setIngredients(ingredient);
                dish.setId(resultSet.getInt("id_dish"));
                dish.setName(resultSet.getString("dish_name"));
                dish.setSelling_price(resultSet.getDouble("selling_price"));
                ingredientDish.setDish(dish);
                ingredients.add(ingredientDish);
            }
            dish.setIngredients(ingredients);
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T) dish;
    }

    @Override
    public <T> T findById(Map<String, Object> criterias) {
        return null;
    }

}
