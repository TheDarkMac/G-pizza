package gpizza.dish;

import gpizza.criteria.CriteriaDELETE;
import gpizza.criteria.CriteriaINSERT;
import gpizza.criteria.CriteriaSELECT;
import gpizza.criteria.CriteriaUPDATE;
import gpizza.dataSource.DAOSchema;
import gpizza.dataSource.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DishDAO implements DAOSchema {
    private DataSource ds;
    public DishDAO() {
        ds = new DataSource();
    }

    @Override
    public <T> List<T> saveAll(List<T> list) {
        List<Dish> dishes = (List<Dish>) list;
        dishes.stream().forEach(
                dish-> {
                    CriteriaINSERT criteriaINSERT = new CriteriaINSERT("dish");
                    criteriaINSERT.insert("id_dish", "name", "unit_price");
                    criteriaINSERT.values(dish.getId_dish(), dish.getName(), dish.getUnit_price());
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
                            int newId = resultSet.getInt("id_dish");
                            dish.setId_dish(newId);
                        } else {
                            System.out.println("⚠ Aucune clé générée !");
                        }
                    } catch (SQLException | RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                });

        return (List<T>) dishes;
    }

    @Override
    public <T> List<T> findAll() {
        List<T> dishes = new ArrayList<>();

        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("dish");
        criteriaSELECT.select("id_dish", "name", "unit_price");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id_dish");
                String name = resultSet.getString("name");
                double unit = resultSet.getDouble("unit_price");
                Dish dish = new Dish(id,name,unit);
                dishes.add((T) dish);
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return dishes;
    }

    @Override
    public <T> T findById(double id) {
        Dish dish = new Dish();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("dish");
        criteriaSELECT.select("id_dish","name","unit_price");
        criteriaSELECT.and("id_dish");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                dish.setId_dish(resultSet.getInt("id_dish"));
                dish.setName(resultSet.getString("name"));
                dish.setUnit_price(resultSet.getDouble("unit_price"));
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }

       return (T) dish;
    }

    @Override
    public <T> T deleteById(double id) {
        Dish dish = findById(id);
        if (dish == null) {
            throw new RuntimeException("this dish does not exist");
        }
        CriteriaDELETE criteriaDELETE = new CriteriaDELETE("dish");
        criteriaDELETE.where("id_dish",id);
        String query = criteriaDELETE.build();
        try(Connection connection = ds.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            preparedStatement.execute();
        }catch (SQLException |RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T)dish;
    }

    @Override
    public <T> List<T> deleteAll(List<T> list) {
        List<Dish> dishList = (List<Dish>) list;
        dishList.stream().forEach(dish -> {
            deleteById(dish.getId_dish());
        });
        return list;
    }

    @Override
    public <T> List<T> update(List<T> t) {
        List<Dish> dishList = (List<Dish>) t;
        System.out.println(dishList);
        dishList.stream().forEach(
                dish -> {
                    Dish verification = findById(dish.getId_dish());
                    if (verification == null){
                        throw new RuntimeException("this ingredienPriceHistory does not exist");
                    }
                    CriteriaUPDATE criteriaUPDATE = new CriteriaUPDATE("dish");
                    criteriaUPDATE.set("name",dish.getName());
                    criteriaUPDATE.set("unit_price",dish.getUnit_price());
                    criteriaUPDATE.and("id_dish",dish.getId_dish());
                    String query = criteriaUPDATE.build();
                    try (Connection connection = ds.getConnection()) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                        System.out.println(preparedStatement);
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
