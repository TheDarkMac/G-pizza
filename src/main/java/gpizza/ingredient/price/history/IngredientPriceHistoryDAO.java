package gpizza.ingredient.price.history;

import gpizza.criteria.CriteriaDELETE;
import gpizza.criteria.CriteriaINSERT;
import gpizza.criteria.CriteriaSELECT;
import gpizza.criteria.CriteriaUPDATE;
import gpizza.dataSource.DAOSchema;
import gpizza.dataSource.DataSource;
import gpizza.ingredient.Ingredient;
import gpizza.ingredient.IngredientDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IngredientPriceHistoryDAO implements DAOSchema {
    private DataSource ds;
    private IngredientDAO ingredientDAO;
    public IngredientPriceHistoryDAO() {
        ds = new DataSource();
        ingredientDAO = new IngredientDAO();
    }

    @Override
    public <T> List<T> saveAll(List<T> list) {
        List<IngredienPriceHistory> ingredientPriceHistory = (List<IngredienPriceHistory>) list;
        ingredientPriceHistory.stream().forEach(
                ingredientPH-> {
                    CriteriaINSERT criteriaINSERT = new CriteriaINSERT("ingredient_price_history");
                    criteriaINSERT.insert("id_price", "id_ingredient", "date_price","unit_price");
                    criteriaINSERT.values(ingredientPH.getId_price(), ingredientPH.getIngredient().getId(), ingredientPH.getDatePrice(),ingredientPH.getUnitPrice());
                    String query = criteriaINSERT.build();
                    try (Connection connection = ds.getConnection()) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                        List<Object> params = criteriaINSERT.getParameters();
                        for (int i = 0; i < params.size(); i++) {
                            preparedStatement.setObject(i + 1, params.get(i), Types.OTHER);
                        }
                        preparedStatement.execute();
                        ResultSet resultSet = preparedStatement.getGeneratedKeys();
                        if (resultSet.next()) {
                            int newId = resultSet.getInt("id_ingredient");
                            ingredientPH.setId_price(newId);
                        } else {
                            System.out.println("⚠ Aucune clé générée !");
                        }
                    } catch (SQLException | RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                });
        return (List<T>) ingredientPriceHistory;
    }

    @Override
    public <T> List<T> findAll() {
        List<T> ingredientPriceHistory = new ArrayList<>();

        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("ingredient_price_history");
        criteriaSELECT.select("id_price", "id_ingredient", "date_price","unit_price");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Ingredient ingredient = ingredientDAO.findById(resultSet.getInt("id_ingredient"));
                long id_price = resultSet.getInt("id_price");
                LocalDate date_price = resultSet.getTimestamp("date_price").toLocalDateTime().toLocalDate();
                double unit = resultSet.getDouble("unit_price");
                IngredienPriceHistory ingredienPriceHistory = new IngredienPriceHistory(id_price,ingredient,date_price,unit);
                ingredientPriceHistory.add((T) ingredienPriceHistory);
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return (List<T>) ingredientPriceHistory;
    }

    @Override
    public <T> T findById(double id) {
        IngredienPriceHistory ingredienPriceHistory = null;
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("ingredient_price_history");
        criteriaSELECT.select("id_price","id_ingredient","date_price","unit_price");
        criteriaSELECT.and("id_price");
        criteriaSELECT.orderBy("date_price",false);
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                ingredienPriceHistory = new IngredienPriceHistory();
                ingredienPriceHistory.setId_price(resultSet.getInt("id_price"));
                ingredienPriceHistory.setIngredient(ingredientDAO.findById(resultSet.getInt("id_ingredient")));
                ingredienPriceHistory.setDatePrice(resultSet.getDate("date_price").toLocalDate());
                ingredienPriceHistory.setUnitPrice(resultSet.getDouble("unit_price"));
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T) ingredienPriceHistory;
    }

    public <T> T findByIngredientId(double id_ingredient) {
        IngredienPriceHistory ingredienPriceHistory = null;
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("ingredient_price_history");
        criteriaSELECT.select("id_price","id_ingredient","date_price","unit_price");
        criteriaSELECT.and("id_ingredient");
        criteriaSELECT.orderBy("date_price",true);
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id_ingredient);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                ingredienPriceHistory = new IngredienPriceHistory();
                ingredienPriceHistory.setId_price(resultSet.getInt("id_price"));
                ingredienPriceHistory.setIngredient(ingredientDAO.findById(resultSet.getInt("id_ingredient")));
                ingredienPriceHistory.setDatePrice(resultSet.getDate("date_price").toLocalDate());
                ingredienPriceHistory.setUnitPrice(resultSet.getDouble("unit_price"));
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T) ingredienPriceHistory;
    }

    public <T> T findById(double id_ingredient, LocalDate atDate) {
        IngredienPriceHistory ingredienPriceHistory = new IngredienPriceHistory();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("ingredient_price_history");
        criteriaSELECT.select("id_price","id_ingredient","date_price","unit_price");
        criteriaSELECT.and("id_ingredient");
        criteriaSELECT.and("date_price");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id_ingredient);
            preparedStatement.setDate(2, Date.valueOf(atDate));
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                ingredienPriceHistory.setId_price(resultSet.getInt("id_price"));
                ingredienPriceHistory.setIngredient(ingredientDAO.findById(resultSet.getInt("id_ingredient")));
                ingredienPriceHistory.setDatePrice(resultSet.getDate("date_price").toLocalDate());
                ingredienPriceHistory.setUnitPrice(resultSet.getDouble("unit_price"));
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T) ingredienPriceHistory;
    }

    @Override
    public <T> T deleteById(double id) {
        IngredienPriceHistory ingredienPriceHistory = findById(id);
        if (ingredienPriceHistory == null) {
            throw new RuntimeException("this ingredient does not exist");
        }
        CriteriaDELETE criteriaDELETE = new CriteriaDELETE("ingredient_price_history");
        criteriaDELETE.where("id_price",id);
        String query = criteriaDELETE.build();
        try(Connection connection = ds.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            preparedStatement.execute();
        }catch (SQLException |RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T)ingredienPriceHistory;
    }

    @Override
    public <T> List<T> deleteAll(List<T> list) {
        List<IngredienPriceHistory> ingredienPriceHistoryList = (List<IngredienPriceHistory>) list;
        ingredienPriceHistoryList.stream().forEach(ingredienPriceHistory -> {
            deleteById(ingredienPriceHistory.getId_price());
        });
        return (List<T>) ingredienPriceHistoryList;
    }

    @Override
    public <T> List<T> update(List<T> t) {
        List<IngredienPriceHistory> ingredienPriceHistoryList = (List<IngredienPriceHistory>) t;
        ingredienPriceHistoryList.stream().forEach(
                ingredienPriceHistory -> {
                    IngredienPriceHistory verification = findById(ingredienPriceHistory.getId_price());
                    if (verification == null){
                        throw new RuntimeException("this ingredienPriceHistory does not exist");
                    }
                    CriteriaUPDATE criteriaUPDATE = new CriteriaUPDATE("ingredient_price_history");
                    criteriaUPDATE.set( "id_ingredient",ingredienPriceHistory.getIngredient().getId());
                    criteriaUPDATE.set("date_price",ingredienPriceHistory.getDatePrice());
                    criteriaUPDATE.set("unit_price",ingredienPriceHistory.getUnitPrice());
                    criteriaUPDATE.and("id_price",ingredienPriceHistory.getId_price());
                    String query = criteriaUPDATE.build();
                    try (Connection connection = ds.getConnection()) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                        List<Object> params = criteriaUPDATE.getParameters();
                        for (int i = 0; i < params.size(); i++) {
                            preparedStatement.setObject(i + 1, params.get(i), Types.OTHER);
                        }
                        preparedStatement.executeUpdate();
                    }catch (SQLException | RuntimeException e){
                        throw new RuntimeException(e);
                    }
                }
        );
        return t;
    }
}
