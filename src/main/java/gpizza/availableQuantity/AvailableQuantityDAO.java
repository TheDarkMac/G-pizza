package gpizza.availableQuantity;

import gpizza.criteria.CriteriaSELECT;
import gpizza.dataSource.DAOSchema;
import gpizza.dataSource.DataSource;
import gpizza.ingredient.Ingredient;
import gpizza.ingredient.IngredientDAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AvailableQuantityDAO implements DAOSchema {
    private DataSource ds;
    private IngredientDAO ingredientDAO;
    public AvailableQuantityDAO() {
        ds = new DataSource();
        ingredientDAO = new IngredientDAO();
    }

    @Override
    public <T> List<T> saveAll(List<T> list) {
        throw new UnsupportedOperationException("Not supported yet.");
//        List<AvailableQuantity> availableQuantityList = (List<AvailableQuantity>) list;
//        availableQuantityList.stream().forEach(
//                availableQuantity-> {
//                    CriteriaINSERT criteriaINSERT = new CriteriaINSERT("available_quantity");
//                    criteriaINSERT.insert("id_ingredient", "available_quantity", "date_of_last_movement");
//                    criteriaINSERT.values(availableQuantity.getId_ingredient(), availableQuantity.getQuantity(), availableQuantity.getDate_of_last_movement());
//                    String query = criteriaINSERT.build();
//                    try (Connection connection = ds.getConnection()) {
//                        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
//                        List<Object> params = criteriaINSERT.getParameters();
//                        for (int i = 0; i < params.size(); i++) {
//                            preparedStatement.setObject(i + 1, params.get(i), Types.OTHER);
//                        }
//                        preparedStatement.execute();
//                        ResultSet resultSet = preparedStatement.getGeneratedKeys();
//
//                    } catch (SQLException | RuntimeException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//        return (List<T>) availableQuantityList;
    }

    @Override
    public <T> List<T> findAll() {
        List<T> availableQuantityList = new ArrayList<>();

        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("available_quantity");
        criteriaSELECT.select("id_ingredient", "available_quantity", "date_of_last_movement");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Ingredient id_ingredient = ingredientDAO.findById(resultSet.getInt("id_ingredient"));
                double availableQuantity1 = resultSet.getDouble("available_quantity");
                LocalDateTime dateOfLastMovement = resultSet.getTimestamp("date_of_last_movement").toLocalDateTime();

                AvailableQuantity availableQuantity = new AvailableQuantity(id_ingredient,availableQuantity1, dateOfLastMovement);
                availableQuantityList.add((T) availableQuantity);
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return availableQuantityList;
    }

    @Override
    public <T> T findById(double id) {
        AvailableQuantity availableQuantity = null;
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("available_quantity");
        criteriaSELECT.select("id_ingredient","available_quantity","date_of_last_movement");
        criteriaSELECT.and("id_ingredient");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                availableQuantity = new AvailableQuantity();
                availableQuantity.setIngredient(ingredientDAO.findById(resultSet.getInt("id_ingredient")));
                availableQuantity.setQuantity(resultSet.getDouble("available_quantity"));
                availableQuantity.setDate_of_last_movement(resultSet.getTimestamp("date_of_last_movement").toLocalDateTime());
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
       return (T) availableQuantity;
    }

    @Override
    public <T> T deleteById(double id) {
        throw new UnsupportedOperationException("Not supported yet.");
//        AvailableQuantity availableQuantity = findById(id);
//        if (availableQuantity == null) {
//            throw new RuntimeException("this availableQuantity does not exist");
//        }
//        CriteriaDELETE criteriaDELETE = new CriteriaDELETE("available_quantity");
//        criteriaDELETE.where("id_ingredient",id);
//        String query = criteriaDELETE.build();
//        try(Connection connection = ds.getConnection()) {
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setDouble(1, id);
//            preparedStatement.execute();
//        }catch (SQLException |RuntimeException e){
//            throw new RuntimeException(e);
//        }
//        return (T)availableQuantity;
    }

    @Override
    public <T> List<T> deleteAll(List<T> list) {

        throw new UnsupportedOperationException("Not supported yet.");
//        List<AvailableQuantity> availableQuantityList = (List<AvailableQuantity>) list;
//        availableQuantityList.stream().forEach(availableQuantity1 -> {
//            deleteById(availableQuantity1.getId_ingredient());
//        });
//        return (List<T>) availableQuantityList;
    }

    @Override
    public <T> List<T> update(List<T> t) {

        throw new UnsupportedOperationException("Not supported yet.");
//        List<AvailableQuantity> availableQuantityList = (List<AvailableQuantity>) t;
//        availableQuantityList.stream().forEach(
//                availableQuantity -> {
//                    Ingredient verification = findById(availableQuantity.getId_ingredient());
//                    if (verification == null){
//                        throw new RuntimeException("this availableQuantity does not exist");
//                    }
//                    CriteriaUPDATE criteriaINSERT = new CriteriaUPDATE("available_quantity");
//                    criteriaINSERT.set( "available_quantity",availableQuantity.getQuantity());
//                    criteriaINSERT.set( "date_of_last_movement",availableQuantity.getDate_of_last_movement());
//                    criteriaINSERT.and("id_ingredient",availableQuantity.getDate_of_last_movement());
//                    String query = criteriaINSERT.build();
//                    try (Connection connection = ds.getConnection()) {
//                        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
//                        List<Object> params = criteriaINSERT.getParameters();
//                        for (int i = 0; i < params.size(); i++) {
//                            preparedStatement.setObject(i + 1, params.get(i), Types.OTHER);
//                        }
//                        preparedStatement.executeUpdate();
//                    }catch (SQLException | RuntimeException e){
//                        throw new RuntimeException(e);
//                    }
//                }
//        );
//        return t;
    }
}
