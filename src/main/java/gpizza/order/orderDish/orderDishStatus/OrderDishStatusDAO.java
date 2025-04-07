package gpizza.order.orderDish.orderDishStatus;

import gpizza.criteria.CriteriaDELETE;
import gpizza.criteria.CriteriaINSERT;
import gpizza.criteria.CriteriaSELECT;
import gpizza.criteria.CriteriaUPDATE;
import gpizza.dataSource.DAOSchema;
import gpizza.dataSource.DataSource;
import gpizza.enums.OrderStatusType;
import gpizza.ingredient.Ingredient;
import gpizza.order.orderDish.OrderDish;
import gpizza.order.orderDish.OrderDishDAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDishStatusDAO implements DAOSchema {
    private DataSource ds;
    private OrderDishDAO orderDishDAO;
    public OrderDishStatusDAO() {
        ds = new DataSource();
        orderDishDAO = new OrderDishDAO();
    }

    @Override
    public <T> List<T> saveAll(List<T> list) {
        List<OrderDishStatus> orderDishStatusList = (List<OrderDishStatus>) list;
        orderDishStatusList.stream().forEach(
                orderDishStatus-> {
                    CriteriaINSERT criteriaINSERT = new CriteriaINSERT("order_dish_status");
                    criteriaINSERT.insert("id_order_dish_status", "reference_order", "id_dish_order","order_dish_status","update_at");
                    criteriaINSERT.values(orderDishStatus.getId(), orderDishStatus.getOrder().getReference(), orderDishStatus.getOrderDish().getId_order_dish(),orderDishStatus.getOrderStatusType(),orderDishStatus.getUpdateAt());
                    String query = criteriaINSERT.build();
                    try (Connection connection = ds.getConnection()) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                        List<Object> params = criteriaINSERT.getParameters();
                        for (int i = 0; i < params.size(); i++) {
                            preparedStatement.setObject(i + 1, params.get(i), Types.OTHER);
                        }
                        preparedStatement.execute();
                    } catch (SQLException | RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                });
        return (List<T>) orderDishStatusList;
    }

    @Override
    public <T> List<T> findAll() {
        List<T> orderDishStatusList = new ArrayList<>();

        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("order_dish_status");
        criteriaSELECT.select("id_order_dish_status", "reference_order", "id_dish_order","order_dish_status","update_at");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id_order_dish_status");
                OrderDish orderDish = orderDishDAO.findById(resultSet.getInt("id_dish_order"));
                LocalDateTime updateAt = resultSet.getTimestamp("update_at").toLocalDateTime();
                String status = resultSet.getString("order_dish_status");
                OrderDishStatus orderDishStatus = new OrderDishStatus(id,orderDish.getOrder(),orderDish, OrderStatusType.valueOf(status),updateAt);
                orderDishStatusList.add((T) orderDishStatus);
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return orderDishStatusList;
    }

    @Override
    public <T> T findById(double id) {
        OrderDishStatus orderDishStatus = null;
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("order_dish_status");
        criteriaSELECT.select("id_order_dish_status", "reference_order", "id_dish_order","order_dish_status","update_at");
        criteriaSELECT.and("id_order_dish_status");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                OrderDish orderDish = orderDishDAO.findById(resultSet.getInt("id_dish_order"));
                orderDishStatus = new OrderDishStatus();
                orderDishStatus.setId(resultSet.getInt("id_order_dish_status"));
                orderDishStatus.setOrderDish(orderDish);
                orderDishStatus.setOrderStatusType(OrderStatusType.valueOf(resultSet.getString("order_dish_status")));
                orderDishStatus.setOrder(orderDish.getOrder());
                orderDishStatus.setUpdateAt(resultSet.getTimestamp("update_at").toLocalDateTime());
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T) orderDishStatus;
    }

    @Override
    public <T> T deleteById(double id) {
        OrderDishStatus orderDishStatus = findById(id);
        if (orderDishStatus == null) {
            throw new RuntimeException("this orderDishStatus does not exist");
        }
        CriteriaDELETE criteriaDELETE = new CriteriaDELETE("order_dish_status");
        criteriaDELETE.where("id_order_dish_status",id);
        String query = criteriaDELETE.build();
        try(Connection connection = ds.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            preparedStatement.execute();
        }catch (SQLException |RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T)orderDishStatus;
    }

    @Override
    public <T> List<T> deleteAll(List<T> list) {
        List<OrderDishStatus> orderDishStatusList = (List<OrderDishStatus>) list;
        orderDishStatusList.stream().forEach(orderDishStatus -> {
            deleteById(orderDishStatus.getId());
        });
        return (List<T>) orderDishStatusList;
    }

    @Override
    public <T> List<T> update(List<T> t) {
        List<OrderDishStatus> orderDishStatusList = (List<OrderDishStatus>) t;
        orderDishStatusList.stream().forEach(
                orderDishStatus -> {
                    Ingredient verification = findById(orderDishStatus.getId());
                    if (verification == null){
                        throw new RuntimeException("this orderDishStatus does not exist");
                    }
                    CriteriaUPDATE criteriaINSERT = new CriteriaUPDATE("order_dish_status");
                    criteriaINSERT.set( "reference_order",orderDishStatus.getOrder().getReference());
                    criteriaINSERT.set( "id_dish_order",orderDishStatus.getOrderDish().getId_order_dish());
                    criteriaINSERT.set( "order_dish_status",orderDishStatus.getOrderStatusType());
                    criteriaINSERT.set( "update_at",orderDishStatus.getUpdateAt());

                    criteriaINSERT.and("id_order_dish_status",orderDishStatus.getId());
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

    public <T> List<T> findAllByOrderDish(long idOrderDish) {
        List<T> orderDishStatusList = new ArrayList<>();

        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("order_dish_status");
        criteriaSELECT.select("id_order_dish_status", "reference_order", "id_dish_order","order_dish_status","update_at");
        criteriaSELECT.and("id_order_dish_status");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, idOrderDish);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id_order_dish_status");
                OrderDish orderDish = orderDishDAO.findById(resultSet.getInt("id_dish_order"));
                LocalDateTime updateAt = resultSet.getTimestamp("update_at").toLocalDateTime();

                String status = resultSet.getString("order_dish_status");
                OrderDishStatus orderDishStatus = new OrderDishStatus(id,null,orderDish, OrderStatusType.valueOf(status),updateAt);
                orderDishStatusList.add((T) orderDishStatus);
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return orderDishStatusList;
    }

}
