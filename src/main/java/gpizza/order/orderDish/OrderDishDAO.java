package gpizza.order.orderDish;

import gpizza.criteria.CriteriaDELETE;
import gpizza.criteria.CriteriaINSERT;
import gpizza.criteria.CriteriaSELECT;
import gpizza.criteria.CriteriaUPDATE;
import gpizza.dataSource.DAOSchema;
import gpizza.dataSource.DataSource;
import gpizza.dish.DishDAO;
import gpizza.dish.dishIngredient.DishIngredient;
import gpizza.dish.dishIngredient.DishIngredientDAO;
import gpizza.ingredient.Ingredient;
import gpizza.order.Order;
import gpizza.order.OrderDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDishDAO implements DAOSchema {
    private final DataSource ds = new DataSource();;

    private OrderDAO orderDAO = new OrderDAO();
    private DishDAO dishDAO = new DishDAO();
    private DishIngredientDAO dishIngredientDAO = new DishIngredientDAO();

    @Override
    public <T> List<T> saveAll(List<T> list) {
        List<OrderDish> orderDishList = (List<OrderDish>) list;
        orderDishList.stream().forEach(
                orderDish-> {
                    CriteriaINSERT criteriaINSERT = new CriteriaINSERT("dish_order");
                    criteriaINSERT.insert("id_dish_order", "id_dish", "reference_order","number");
                    criteriaINSERT.values(orderDish.getId_order_dish(), orderDish.getDishIngredient().getIngredient().getId(), orderDish.getOrder().getReference(),orderDish.getQuantity());
                    String query = criteriaINSERT.build();
                    try (Connection connection = ds.getConnection()) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                        List<Object> params = criteriaINSERT.getParameters();
                        for (int i = 0; i < params.size(); i++) {
                            preparedStatement.setObject(i + 1, params.get(i), Types.OTHER);
                        }
                        preparedStatement.execute();
                        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            orderDish.setId_order_dish(generatedKeys.getLong(1));
                        }

                    } catch (SQLException | RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                });
        return (List<T>) orderDishList;
    }

    @Override
    public <T> List<T> findAll() {
        List<T> orderDishList = new ArrayList<>();

        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("dish_order");
        criteriaSELECT.select("id_dish_order", "id_dish", "reference","number");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id_order_dish");
                OrderDAO orderDAO = new OrderDAO();
                DishDAO dishDAO = new DishDAO();
                Order order = orderDAO.findByIReference(resultSet.getString("reference_order"));
                DishIngredient dishIngredient = dishDAO.findById(resultSet.getInt("id_dish"));
                double quantity = resultSet.getDouble("number");
                OrderDish orderDish = new OrderDish();
                orderDish.setId_order_dish(id);
                orderDish.setOrder(order);
                orderDish.setDishIngredient(dishIngredient);
                orderDish.setQuantity(quantity);
                orderDishList.add((T) orderDish);
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return orderDishList;
    }

    @Override
    public <T> T findById(double id) {
        OrderDish orderDish = null;
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("dish_order");
        criteriaSELECT.select("id_dish_order","id_dish","reference_order","number");
        criteriaSELECT.and("id_dish_order");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                orderDish =  new OrderDish();
                orderDish.setDishIngredient(dishIngredientDAO.findById(resultSet.getInt("id_dish")));
                orderDish.setQuantity(resultSet.getDouble("number"));
                orderDish.setOrder(orderDAO.findByIReference(resultSet.getString("reference_order")));
                orderDish.setId_order_dish((resultSet.getInt("id_dish_order")));
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T) orderDish;
    }

    @Override
    public <T> T deleteById(double id) {
        OrderDish orderDish = findById(id);
        if (orderDish == null) {
            throw new RuntimeException("this orderDish does not exist");
        }
        CriteriaDELETE criteriaDELETE = new CriteriaDELETE("dish_order");
        criteriaDELETE.where("id_dish_order",id);
        String query = criteriaDELETE.build();
        try(Connection connection = ds.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            preparedStatement.execute();
        }catch (SQLException |RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T)orderDish;
    }

    @Override
    public <T> List<T> deleteAll(List<T> list) {
        List<OrderDish> orderDishList = (List<OrderDish>) list;
        orderDishList.stream().forEach(orderDish -> {
            deleteById(orderDish.getId_order_dish());
        });
        return (List<T>) orderDishList;
    }

    @Override
    public <T> List<T> update(List<T> t) {
        List<OrderDish> orderDishList = (List<OrderDish>) t;
        orderDishList.stream().forEach(
                orderDish -> {
                    Ingredient verification = findById(orderDish.getId_order_dish());
                    if (verification == null){
                        throw new RuntimeException("this orderDish does not exist");
                    }
                    CriteriaUPDATE criteriaINSERT = new CriteriaUPDATE("dish_order");
                    criteriaINSERT.set( "reference_order",orderDish.getOrder().getReference());
                    criteriaINSERT.set( "id_dish",orderDish.getDishIngredient().getDish().getId_dish());
                    criteriaINSERT.and("id_dish_order",orderDish.getId_order_dish());
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

    public <T> List<T> findAllByReference( String reference ) {
        List<T> orderDishList = new ArrayList<>();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("dish_order");
        criteriaSELECT.select("id_dish_order", "id_dish", "reference_order","number");
        criteriaSELECT.and("reference_order");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, reference);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id_dish_order");
                OrderDAO orderDAO = new OrderDAO();
                Order order = orderDAO.findByIReference(resultSet.getString("reference_order"));
                DishIngredient dishIngredient = dishIngredientDAO.findById(resultSet.getInt("id_dish"));
                double quantity = resultSet.getDouble("number");
                System.out.println("dishIngredient: " + dishIngredient);
                OrderDish orderDish = new OrderDish();
                orderDish.setId_order_dish(id);
                orderDish.setDishIngredient(dishIngredient);
                orderDish.setOrder(order);
                orderDish.setQuantity(quantity);
                orderDishList.add((T) orderDish);
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return orderDishList;
    }
}
