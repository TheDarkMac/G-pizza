package torn.ando.gpizzasb.gpizza.dao;

import lombok.AllArgsConstructor;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaINSERT;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaSELECT;
import torn.ando.gpizzasb.gpizza.dataSource.DataSource;
import torn.ando.gpizzasb.gpizza.entity.DishIngredient;
import torn.ando.gpizzasb.gpizza.entity.Ingredient;
import org.springframework.stereotype.Repository;
import torn.ando.gpizzasb.gpizza.enums.Unit;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class DishIngredientDAO implements DAOSchema{
    private DataSource dataSource;
    private IngredientDAO ingredientDAO;


    @Override
    public <T> List<T> saveAll(List<T> object) {
        List<DishIngredient> dishIngredientList = new ArrayList<>();
        List<DishIngredient> dishIngredients = (List<DishIngredient>) object;
        dishIngredients.forEach(dishIngredient -> {
            CriteriaINSERT criteriaINSERT = new CriteriaINSERT("dish_ingredient");
            criteriaINSERT.insert("id_dish","id_ingredient","quantity","unit")
                    .values("?","?","?","?")
                    .returning("id_dish","id_ingredient","quantity","unit");
            String query = criteriaINSERT.build();
            try(Connection connection = dataSource.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1,dishIngredient.getDish().getId());
                preparedStatement.setLong(2, dishIngredient.getIngredient().getId());
                preparedStatement.setDouble(3,dishIngredient.getRequiredQuantity());
                preparedStatement.setObject(4,dishIngredient.getUnit(), Types.OTHER);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    DishIngredient dishIngredient1 = mapFromResultSet(resultSet);
                    dishIngredientList.add(dishIngredient1);
                }

            } catch (RuntimeException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return (List<T>) dishIngredientList;
    }

    @Override
    public <T> List<T> findAll() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public <T> T findById(double id) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public <T> T deleteById(double id) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public <T> List<T> deleteAll(List<T> list) {
       throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public <T> List<T> updateAll(List<T> object) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public List<DishIngredient> findByIdDish(long id) {
        List<DishIngredient> dishIngredients = new ArrayList<>();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("dish_ingredient");
        criteriaSELECT.select("id_dish","id_ingredient","quantity","unit");
        criteriaSELECT.and("id_dish");
        String query = criteriaSELECT.build();

        try (Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                DishIngredient dishIngredient = mapFromResultSet(resultSet);
                dishIngredients.add(dishIngredient);
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
        return dishIngredients;
    }

    public DishIngredient mapFromResultSet(ResultSet resultSet) throws SQLException {
        Ingredient ingredient = ingredientDAO.findById(resultSet.getLong("id_ingredient"));
        DishIngredient dishIngredient = new DishIngredient();
        dishIngredient.setIngredient(ingredient);
        dishIngredient.setRequiredQuantity(resultSet.getDouble("quantity"));
        dishIngredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
        return dishIngredient;
    }
}
