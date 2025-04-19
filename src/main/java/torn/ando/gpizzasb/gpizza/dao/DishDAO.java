package torn.ando.gpizzasb.gpizza.dao;

import lombok.AllArgsConstructor;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaINSERT;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaSELECT;
import torn.ando.gpizzasb.gpizza.dataSource.DataSource;
import torn.ando.gpizzasb.gpizza.entity.Dish;
import torn.ando.gpizzasb.gpizza.entity.DishIngredient;
import org.springframework.stereotype.Repository;
import torn.ando.gpizzasb.gpizza.enums.DurationUnit;
import torn.ando.gpizzasb.gpizza.enums.StatisticType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class DishDAO implements DAOSchema{
    private DataSource dataSource;
    private DishIngredientDAO dishIngredientDAO;

    @Override
    public <T> List<T> saveAll(List<T> object) {
        List<Dish> dishList = (List<Dish>) object;
        List<Dish> dishes = new ArrayList<>();
        dishList.forEach(dish -> {
            CriteriaINSERT criteriaINSERT = new CriteriaINSERT("dish")
                    .insert("id_dish","name","unit_price")
                    .onConflict("id_dish")
                    .doUpdate("unit_price","?")
                    .doUpdate("name","?")
                    .values("?","?","?")
                    .returning("id_dish","name","unit_price");
            String query = criteriaINSERT.build();
            try(Connection connection = dataSource.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setDouble(1,dish.getId());
                preparedStatement.setString(2,dish.getName());
                preparedStatement.setDouble(3,dish.getPrice());
                preparedStatement.setDouble(4,dish.getPrice());
                preparedStatement.setString(5,dish.getName());
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    Dish d = mapFromResultSet(resultSet);
                    dishes.add(d);
                }
            }catch (SQLException | RuntimeException e){
                throw new RuntimeException(e);
            }
        });
        return (List<T>) dishes;
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
        throw new UnsupportedOperationException("not yet supported");
    }

    @Override
    public <T> List<T> deleteAll(List<T> list) {

        throw new UnsupportedOperationException("not yet supported");
    }

    @Override
    public <T> List<T> updateAll(List<T> object) {

        throw new UnsupportedOperationException("not yet supported");
    }

    public Dish mapFromResultSet(ResultSet resultSet) throws SQLException {
        List<DishIngredient> dishIngredient = dishIngredientDAO.findByIdDish(resultSet.getLong("id_dish"));
        Dish dish = new Dish();
        dish.setId(resultSet.getLong("id_dish"));
        dish.setName(resultSet.getString("name"));
        dish.setPrice(resultSet.getDouble("unit_price"));
        if (dishIngredient != null) {
            dish.setDishIngredientList(dishIngredient);
        }
        return dish;
    }

    public double calculateProcessingTime(Long dishId, LocalDate startDate, LocalDate endDate,
                                          DurationUnit timeUnit, StatisticType statType) {
        
        String statFunction;
        switch (statType) {
            case MINIMUM:
                statFunction = "MIN";
                break;
            case MAXIMUM:
                statFunction = "MAX";
                break;
            case AVERAGE:
            default:
                statFunction = "AVG";
                break;
        }
        
        String timeConversion;
        switch (timeUnit) {
            case MINUTES:
                timeConversion = " / 60.0";
                break;
            case HOURS:
                timeConversion = " / 3600.0";
                break;
            case SECONDS:
            default:
                timeConversion = "";
                break;
        }
        
        String processingTimeField =
                statFunction + "(EXTRACT(EPOCH FROM preparation_duration)" + timeConversion + ") AS processing_time";

        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("dish_order_status");
        criteriaSELECT.select(processingTimeField);
        criteriaSELECT.and("dish_id");
        criteriaSELECT.and("dish_status");

        
        LocalDate adjustedEndDate = endDate.plusDays(1);
        criteriaSELECT.andBetween("datetime", startDate, adjustedEndDate);

        String query = criteriaSELECT.build();
        double result = 0.0;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            int paramIndex = 1;
            preparedStatement.setLong(paramIndex++, dishId);
            preparedStatement.setString(paramIndex++, "DONE");
            preparedStatement.setObject(paramIndex++, startDate);
            preparedStatement.setObject(paramIndex, adjustedEndDate);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getDouble("processing_time");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating processing time for dish " + dishId, e);
        }

        return result;
    }
}
