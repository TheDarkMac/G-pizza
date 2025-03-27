package gpizza.stock;

import gpizza.criteria.CriteriaDELETE;
import gpizza.criteria.CriteriaINSERT;
import gpizza.criteria.CriteriaSELECT;
import gpizza.criteria.CriteriaUPDATE;
import gpizza.dataSource.DAOSchema;
import gpizza.dataSource.DataSource;
import gpizza.enums.MovementType;
import gpizza.ingredient.Ingredient;
import gpizza.ingredient.IngredientDAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StockDAO implements DAOSchema {
    private DataSource ds;
    private IngredientDAO ingredientDAO;
    public StockDAO() {
        ds = new DataSource();
        ingredientDAO = new IngredientDAO();
    }

    @Override
    public <T> List<T> saveAll(List<T> list) {
        List<Stock> stockList = (List<Stock>) list;
        stockList.stream().forEach(
                stock-> {
                    CriteriaINSERT criteriaINSERT = new CriteriaINSERT("stock");
                    criteriaINSERT.insert( "id_ingredient", "quantity","date_of_movement","movement_type");
                    criteriaINSERT.values(stock.getIngredient().getId(), stock.getQuantity(), stock.getDateOfMovement(),stock.getMovementType());
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
                            stock.setId_stock(newId);
                        } else {
                            System.out.println("⚠ Aucune clé générée !");
                        }
                    } catch (SQLException | RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                });
        return (List<T>) stockList;
    }

    @Override
    public <T> List<T> findAll() {
        List<T> stockList = new ArrayList<>();

        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("stock");
        criteriaSELECT.select("id_stock", "id_ingredient", "quantity","date_of_movement","movement_type");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id_stock");
                Ingredient id_ingredient = ingredientDAO.findById(resultSet.getInt("id_ingredient"));
                double quantity = resultSet.getDouble("quantity");
                LocalDateTime dateOfmovement =  resultSet.getTimestamp("date_of_movement").toLocalDateTime();
                MovementType movementType = resultSet.getObject("movement_type", MovementType.class);
                Stock stock = new Stock(id,id_ingredient, quantity,dateOfmovement,movementType);
                stockList.add((T) stock);
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return stockList;
    }

    @Override
    public <T> T findById(double id) {
        Stock stock = null;
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("stock");
        criteriaSELECT.select("id_stock","id_ingredient","quantity","date_of_movement","movement_type");
        criteriaSELECT.and("id_stock");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                stock = new Stock();
                stock.setId_stock(resultSet.getInt("id_stock"));
                stock.setIngredient(ingredientDAO.findById(resultSet.getInt("id_ingredient")));
                stock.setDateOfMovement(resultSet.getTimestamp("date_of_movement").toLocalDateTime());
                stock.setQuantity(resultSet.getDouble("quantity"));
                stock.setMovementType(MovementType.IN.valueOf(resultSet.getString("movement_type")));
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }

        return (T) stock;
    }

    public <T> T findByIngredientId(double id) {
        Stock stock = null;
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("stock");
        criteriaSELECT.select("id_stock","id_ingredient","quantity","date_of_movement","movement_type");
        criteriaSELECT.and("id_ingredient");
        criteriaSELECT.orderBy("date_of_movement",true);
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                stock = new Stock();
                stock.setId_stock(resultSet.getInt("id_stock"));
                stock.setIngredient(ingredientDAO.findById(resultSet.getInt("id_ingredient")));
                stock.setQuantity(resultSet.getDouble("quantity"));
                stock.setDateOfMovement(resultSet.getTimestamp("date_of_movement").toLocalDateTime());
                stock.setMovementType(MovementType.IN.valueOf(resultSet.getString("movement_type")));
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }

        return (T) stock;
    }

    @Override
    public <T> T deleteById(double id) {
        Stock ingredient = findById(id);
        if (ingredient == null) {
            throw new RuntimeException("this ingredient does not exist");
        }
        CriteriaDELETE criteriaDELETE = new CriteriaDELETE("stock");
        criteriaDELETE.where("id_stock",id);
        String query = criteriaDELETE.build();
        try(Connection connection = ds.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            preparedStatement.execute();
        }catch (SQLException |RuntimeException e){
            throw new RuntimeException(e);
        }

        return (T)ingredient;
    }

    @Override
    public <T> List<T> deleteAll(List<T> list) {
        List<Stock> stockList = (List<Stock>) list;
        stockList.stream().forEach(ingredient -> {
            deleteById(ingredient.getId_stock());
        });

        return (List<T>) stockList;
    }

    @Override
    public <T> List<T> update(List<T> t) {
        List<Stock> stockList = (List<Stock>) t;
        stockList.stream().forEach(
                ingredient -> {
                    Ingredient verification = findById(ingredient.getId_stock());
                    if (verification == null){
                        throw new RuntimeException("this ingredient does not exist");
                    }
                    CriteriaUPDATE criteriaINSERT = new CriteriaUPDATE("stock");
                    criteriaINSERT.set( "id_ingredient",ingredient.getIngredient().getId());
                    criteriaINSERT.set( "quantity",ingredient.getQuantity());
                    criteriaINSERT.set( "date_of_movement",ingredient.getDateOfMovement());
                    criteriaINSERT.set( "movement_type",ingredient.getMovementType());
                    criteriaINSERT.and("id_ingredient",ingredient.getId_stock());
                    String query = criteriaINSERT.build();
                    try (Connection connection = ds.getConnection()) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                        List<Object> params = criteriaINSERT.getParameters();
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
