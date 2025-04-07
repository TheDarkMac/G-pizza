package gpizza.dish.dishIngredient;

import gpizza.criteria.CriteriaDELETE;
import gpizza.criteria.CriteriaINSERT;
import gpizza.criteria.CriteriaSELECT;
import gpizza.criteria.CriteriaUPDATE;
import gpizza.dataSource.DAOSchema;
import gpizza.dataSource.DataSource;
import gpizza.dish.Dish;
import gpizza.dish.DishDAO;
import gpizza.ingredient.Ingredient;
import gpizza.ingredient.IngredientDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DishIngredientDAO implements DAOSchema {
    private DataSource ds;
    private DishDAO dishDAO;
    private IngredientDAO ingredientDAO;
    public DishIngredientDAO() {
        ds = new DataSource();
        dishDAO = new DishDAO();
        ingredientDAO = new IngredientDAO();
    }

    @Override
    public <T> List<T> saveAll(List<T> list) {
        List<DishIngredient> dishIngredients = (List<DishIngredient>) list;
        dishIngredients.stream().forEach(
                dishIngredient-> {
                    CriteriaINSERT criteriaINSERT = new CriteriaINSERT("dish_ingredient");
                    criteriaINSERT.insert("id_dish_ingredient","id_dish","id_ingredient", "quantity");
                    criteriaINSERT.values(dishIngredient.getId(),dishIngredient.getDish().getId_dish(), dishIngredient.getIngredient().getId(), dishIngredient.getQuantity());
                    String query = criteriaINSERT.build();
                    try (Connection connection = ds.getConnection()) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                        List<Object> params = criteriaINSERT.getParameters();
                        for (int i = 0; i < params.size(); i++) {
                            preparedStatement.setObject(i + 1, params.get(i), Types.OTHER);
                        }
                        preparedStatement.execute();
                        ResultSet resultSet = preparedStatement.getGeneratedKeys();

                    } catch (SQLException | RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                });
        return (List<T>) dishIngredients;
    }

    @Override
    public <T> List<T> findAll() {
        List<T> dishIngredients = new ArrayList<>();

        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("dish_ingredient");
        criteriaSELECT.select( "id_dish_ingredient","id_dish","id_ingredient", "quantity");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Ingredient id_ingredient = ingredientDAO.findById(resultSet.getInt("id_ingredient"));
                Dish id_dish = dishDAO.findById(resultSet.getInt("id_dish"));
                double quantity = resultSet.getDouble("quantity");
                long id = resultSet.getLong("id_dish_ingredient");
                DishIngredient dishIngredient = new DishIngredient(id,id_dish,id_ingredient,quantity);
                dishIngredients.add((T) dishIngredient);
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }

        return dishIngredients;
    }

    @Override
    public <T> T findById(double id) {
        DishIngredient dishIngredient = null;
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("dish_ingredient");
        criteriaSELECT.select("id_dish_ingredient","id_dish","id_ingredient", "quantity");
        criteriaSELECT.and("id_dish");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                dishIngredient = new DishIngredient();
                dishIngredient.setId(resultSet.getInt("id_dish_ingredient"));
                dishIngredient.setIngredient(ingredientDAO.findById(resultSet.getInt("id_ingredient")));
                dishIngredient.setQuantity((resultSet.getDouble("quantity")));
                dishIngredient.setDish(dishDAO.findById(resultSet.getInt("id_dish")));
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T) dishIngredient;
    }

    public <T> T findIngredientByDish(double id_ingredient, double id_dish) {
        DishIngredient dishIngredient = new DishIngredient();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("dish_ingredient");
        criteriaSELECT.select("id_dish_ingredient","id_dish","id_ingredient","quantity");
        criteriaSELECT.and("id_dish");
        criteriaSELECT.and("id_ingredient");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id_dish);
            preparedStatement.setDouble(2, id_ingredient);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                dishIngredient.setId(resultSet.getInt("id_dish_ingredient"));
                dishIngredient.setDish(dishDAO.findById(resultSet.getInt("id_dish")));
                dishIngredient.setIngredient(ingredientDAO.findById(resultSet.getInt("id_ingredient")));
                dishIngredient.setQuantity(resultSet.getDouble("quantity"));
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }

        return (T) dishIngredient;
    }

    @Override
    public <T> T deleteById(double id) {


        throw new UnsupportedOperationException("Not yet supported");
//        return (T)ingredient;
    }

    @Override
    public <T> List<T> deleteAll(List<T> list) {

        throw new UnsupportedOperationException("Not yet supported");
//        return (List<T>) ingredientList;
    }

    @Override
    public <T> List<T> update(List<T> t) {
        List<DishIngredient> dishIngredientList = (List<DishIngredient>) t;
        dishIngredientList.stream().forEach(
                dishIngredient -> {
                    DishIngredient verification = findById(dishIngredient.getId());
                    if (verification == null){
                        throw new RuntimeException("this dishIngredient does not exist");
                    }
                    CriteriaUPDATE criteriaINSERT = new CriteriaUPDATE("dish_ingredient");
                    criteriaINSERT.set( "quantity",dishIngredient.getQuantity());
                    criteriaINSERT.and("id_ingredient",dishIngredient.getIngredient().getId());
                    criteriaINSERT.and("id_dish",verification.getDish().getId_dish());
                    String query = criteriaINSERT.build();
                    try (Connection connection = ds.getConnection()) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                        List<Object> params = criteriaINSERT.getParameters();
                        for (int i = 0; i < params.size(); i++) {
                            preparedStatement.setObject(i + 1, params.get(i), Types.OTHER);
                        }
                        System.out.println(preparedStatement);
                       // preparedStatement.executeUpdate();
                    }catch (SQLException | RuntimeException e){
                        throw new RuntimeException(e);
                    }
                }
        );

        throw new UnsupportedOperationException("Not yet supported");
//        return t;
    }

    public List<DishIngredient> deleteByDish(int id_dish){
        List<DishIngredient> dishIngredientList = findByDish(id_dish);
        if(dishIngredientList == null){
            throw new RuntimeException("dishIngredients do not exist");
        }
        CriteriaDELETE criteriaDELETE = new CriteriaDELETE("dish_ingredient");
        criteriaDELETE.where("id_dish",id_dish);
        String query = criteriaDELETE.build();
        try(Connection connection = ds.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id_dish);
            preparedStatement.execute();
        }catch (SQLException |RuntimeException e){
            throw new RuntimeException(e);
        }
        return dishIngredientList;
    }

    public List<DishIngredient> findByDish(int idDish) {
        List<DishIngredient> dishIngredientList = new ArrayList<>();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("dish_ingredient");
        criteriaSELECT.select("id_dish_ingredient","id_dish","id_ingredient","quantity");
        criteriaSELECT.and("id_dish");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, idDish);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                dishIngredientList.add(
                        new DishIngredient(
                                resultSet.getInt("id_dish_ingredient"),
                                dishDAO.findById(resultSet.getInt("id_dish")),
                                ingredientDAO.findById(resultSet.getInt("id_ingredient")),
                                resultSet.getDouble("quantity")
                        )
                );
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return dishIngredientList;
    }
}
