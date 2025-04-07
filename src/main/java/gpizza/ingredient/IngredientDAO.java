package gpizza.ingredient;

import gpizza.criteria.CriteriaDELETE;
import gpizza.criteria.CriteriaINSERT;
import gpizza.criteria.CriteriaSELECT;
import gpizza.criteria.CriteriaUPDATE;
import gpizza.dataSource.DAOSchema;
import gpizza.dataSource.DataSource;
import gpizza.enums.Unit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO implements DAOSchema {
    private DataSource ds;
    public IngredientDAO() {
        ds = new DataSource();
    }

    @Override
    public <T> List<T> saveAll(List<T> list) {
        List<Ingredient> ingredients = (List<Ingredient>) list;
        ingredients.stream().forEach(
                ingredient-> {
                    CriteriaINSERT criteriaINSERT = new CriteriaINSERT("ingredient");
                    criteriaINSERT.insert("id_ingredient", "name", "unit");
                    criteriaINSERT.values(ingredient.getId(), ingredient.getName(), ingredient.getUnit());
                    String query = criteriaINSERT.build();
                    try (Connection connection = ds.getConnection()) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                        List<Object> params = criteriaINSERT.getParameters();
                        for (int i = 0; i < params.size(); i++) {
                            preparedStatement.setObject(i + 1, params.get(i),Types.OTHER);
                        }
                        preparedStatement.execute();
                        ResultSet resultSet = preparedStatement.getGeneratedKeys();
                        if (resultSet.next()) {
                            int newId = resultSet.getInt("id_ingredient");
                            ingredient.setId(newId);
                        } else {
                            System.out.println("⚠ Aucune clé générée !");
                        }
                    } catch (SQLException | RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                });
        return (List<T>) ingredients;
    }

    @Override
    public <T> List<T> findAll() {
        List<T> ingredients = new ArrayList<>();

        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("ingredient");
        criteriaSELECT.select("id_ingredient", "name", "unit");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id_ingredient");
                String name = resultSet.getString("name");
                String unit = resultSet.getString("unit");
                Ingredient ingredient = new Ingredient(id,name,Unit.valueOf(unit));
                ingredients.add((T) ingredient);
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return ingredients;
    }

    @Override
    public <T> T findById(double id) {
        Ingredient ingredient = new Ingredient();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("ingredient");
        criteriaSELECT.select("id_ingredient","name","unit");
        criteriaSELECT.and("id_ingredient");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                ingredient.setId(resultSet.getInt("id_ingredient"));
                ingredient.setName(resultSet.getString("name"));
                ingredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T) ingredient;
    }

    @Override
    public <T> T deleteById(double id) {
        Ingredient ingredient = findById(id);
        if (ingredient == null) {
            throw new RuntimeException("this ingredient does not exist");
        }
        CriteriaDELETE criteriaDELETE = new CriteriaDELETE("ingredient");
        criteriaDELETE.where("id_ingredient",id);
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
        List<Ingredient> ingredientList = (List<Ingredient>) list;
        ingredientList.stream().forEach(ingredient -> {
            deleteById(ingredient.getId());
        });
        return (List<T>) ingredientList;
    }

    @Override
    public <T> List<T> update(List<T> t) {
        List<Ingredient> ingredientList = (List<Ingredient>) t;
        ingredientList.stream().forEach(
                ingredient -> {
                    Ingredient verification = findById(ingredient.getId());
                    if (verification == null){
                        throw new RuntimeException("this ingredient does not exist");
                    }
                    CriteriaUPDATE criteriaINSERT = new CriteriaUPDATE("ingredient");
                    criteriaINSERT.set( "name",ingredient.getName());
                    criteriaINSERT.set( "unit",ingredient.getUnit());
                    criteriaINSERT.and("id_ingredient",ingredient.getId());
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
