package torn.ando.gpizzasb.gpizza.dao;

import lombok.AllArgsConstructor;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaINSERT;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaSELECT;
import torn.ando.gpizzasb.gpizza.dataSource.DataSource;
import torn.ando.gpizzasb.gpizza.entity.DishIngredient;
import torn.ando.gpizzasb.gpizza.entity.Ingredient;
import org.springframework.stereotype.Repository;

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
            criteriaINSERT.insert("id_dish_ingredient","id_dish","id_ingredient","quantity")
                    .values("?","?","?","?")
                    .onConflict("id_dish_ingredient")
                    .doUpdate("id_dish",dishIngredient.getDish().getId())
                    .doUpdate("id_ingredient",dishIngredient.getIngredient().getId())
                    .doUpdate("quantity",dishIngredient.getRequiredQuantity())
                    .returning("id_dish_ingredient","id_dish","id_ingredient","quantity");

            String query = criteriaINSERT.build();
            try(Connection connection = dataSource.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1,dishIngredient.getId());
                preparedStatement.setLong(2,dishIngredient.getDish().getId());
                preparedStatement.setLong(3, dishIngredient.getIngredient().getId());
                preparedStatement.setDouble(4,dishIngredient.getRequiredQuantity());

                // en cas de conflit, on fait une mise a jour
                preparedStatement.setLong(5,dishIngredient.getDish().getId());
                preparedStatement.setLong(6, dishIngredient.getIngredient().getId());
                preparedStatement.setDouble(7,dishIngredient.getRequiredQuantity());


                System.out.println(preparedStatement);
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
        return List.of();
    }

    @Override
    public <T> T findById(double id) {
        return null;
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

    public List<DishIngredient> findByIdDish(long id) {
        List<DishIngredient> dishIngredients = new ArrayList<>();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("dish_ingredient");
        criteriaSELECT.select("id_dish_ingredient","id_dish","id_ingredient","quantity");
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
        dishIngredient.setId(resultSet.getLong("id_dish_ingredient"));
        dishIngredient.setIngredient(ingredient);
        dishIngredient.setRequiredQuantity(resultSet.getLong("quantity"));
        return dishIngredient;
    }
}
