package torn.ando.gpizzasb.gpizza.dao;

import lombok.AllArgsConstructor;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaINSERT;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaSELECT;
import torn.ando.gpizzasb.gpizza.dataSource.DataSource;
import torn.ando.gpizzasb.gpizza.entity.IngredientPrice;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class IngredientPriceDAO implements DAOSchema {

    private DataSource dataSource;

    @Override
    public <T> List<T> saveAll(List<T> object) {
        List<IngredientPrice> ingredientPrices = new ArrayList<>();
        List<IngredientPrice> ingredientPriceList = (List<IngredientPrice>) object;
        ingredientPriceList.forEach(ingredientPrice -> {
            CriteriaINSERT criteriaINSERT = new CriteriaINSERT("ingredient_price_history");
            criteriaINSERT.insert("id_ingredient","date_price","unit_price")
                    .values("?","?","?").onConflict("id_ingredient","date_price")
                    .returning("id_price","id_ingredient","date_price","unit_price");

            String query = criteriaINSERT.build();
            try(Connection connection = dataSource.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1,ingredientPrice.getIngredient().getId());
                preparedStatement.setTimestamp(2, Timestamp.valueOf(ingredientPrice.getDateValue()));
                preparedStatement.setDouble(3,ingredientPrice.getPrice());


                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    IngredientPrice price = mapFromResultSet(resultSet);
                    ingredientPrices.add(price);
                }

            } catch (RuntimeException | SQLException e) {
                throw new RuntimeException(e);
            }

        });
        return (List<T>) ingredientPrices;
    }

    @Override
    public <T> List<T> findAll() {
        return List.of();
    }

    @Override
    public <T> T findById(double id) {
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("ingredient_price_history");
        criteriaSELECT.select("id_price","id_ingredient","date_price","unit_price");
        criteriaSELECT.and("id_price");
        String query = criteriaSELECT.build();
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                IngredientPrice price = mapFromResultSet(resultSet);
                return (T) price;
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public <T> T findById(double id,double idIngredient) {
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("ingredient_price_history");
        criteriaSELECT.select("id_price","id_ingredient","date_price","unit_price");
        criteriaSELECT.and("id_price");
        criteriaSELECT.and("id_ingredient");
        String query = criteriaSELECT.build();
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1,id);
            preparedStatement.setDouble(2,idIngredient);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                IngredientPrice price = mapFromResultSet(resultSet);
                return (T) price;
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
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

    public List<IngredientPrice> findByIdIngredient(long id) {
        List<IngredientPrice> ingredientPriceList = new ArrayList<>();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("ingredient_price_history");
        criteriaSELECT.select("id_price","id_ingredient","date_price","unit_price");
        criteriaSELECT.and("id_ingredient");
        String query = criteriaSELECT.build();
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                IngredientPrice ingredientPrice = mapFromResultSet(resultSet);
                ingredientPriceList.add(ingredientPrice);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ingredientPriceList;
    }


    public IngredientPrice mapFromResultSet(ResultSet rs) throws SQLException {
        IngredientPrice ingredientPrice = new IngredientPrice();
        ingredientPrice.setId(rs.getLong("id_price"));
        ingredientPrice.setPrice(rs.getDouble("unit_price"));
        ingredientPrice.setDateValue(rs.getTimestamp("date_price").toLocalDateTime());
        return ingredientPrice;
    }
}
