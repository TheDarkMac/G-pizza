package torn.ando.gpizzasb.gpizza.dao;

import lombok.AllArgsConstructor;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaSELECT;
import torn.ando.gpizzasb.gpizza.dataSource.DataSource;
import torn.ando.gpizzasb.gpizza.entity.Dish;
import torn.ando.gpizzasb.gpizza.entity.DishIngredient;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class DishDAO implements DAOSchema{
    private DataSource dataSource;
    private DishIngredientDAO dishIngredientDAO;

    @Override
    public <T> List<T> saveAll(List<T> object) {
        return List.of();
    }

    @Override
    public <T> List<T> findAll() {
        List<Dish> dishList = new ArrayList<>();
        CriteriaSELECT criteriaSELECT =  new CriteriaSELECT("dish");
        criteriaSELECT.select("id_dish","name","unit_price");
        String query = criteriaSELECT.build();
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Dish dish = mapFromResultSet(resultSet);
                dishList.add(dish);
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return (List<T>) dishList;
    }

    @Override
    public <T> T findById(double id) {
        Dish dish = null;
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("dish");
        criteriaSELECT.select("id_dish","name","unit_price" //about dish
        );
        criteriaSELECT.and("id_dish");
        String query = criteriaSELECT.build();
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                dish = mapFromResultSet(resultSet);
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }

        return (T) dish;
    }

    @Override
    public <T> T deleteById(double id) {
        return null;
    }

    @Override
    public <T> List<T> deleteAll(List<T> list) {
        return List.of();
    }

    @Override
    public <T> List<T> updateAll(List<T> object) {
        return List.of();
    }

    public Dish mapFromResultSet(ResultSet resultSet) throws SQLException {
        List<DishIngredient> dishIngredient = dishIngredientDAO.findByIdDish(resultSet.getLong("id_dish"));
        Dish dish = new Dish();
        dish.setId(resultSet.getLong("id_dish"));
        dish.setName(resultSet.getString("name"));
        dish.setPrice(resultSet.getDouble("unit_price"));
        dish.setDishIngredientList(dishIngredient);
        return dish;
    }
}
