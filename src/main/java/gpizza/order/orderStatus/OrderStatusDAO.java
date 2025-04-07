package gpizza.order.orderStatus;

import gpizza.criteria.CriteriaDELETE;
import gpizza.criteria.CriteriaINSERT;
import gpizza.criteria.CriteriaSELECT;
import gpizza.criteria.CriteriaUPDATE;
import gpizza.dataSource.DAOSchema;
import gpizza.dataSource.DataSource;
import gpizza.enums.OrderStatusType;
import gpizza.ingredient.Ingredient;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusDAO implements DAOSchema {
    private DataSource ds;
    public OrderStatusDAO() {
        ds = new DataSource();
    }

    @Override
    public <T> List<T> saveAll(List<T> list) {
        List<OrderStatus> orderStatusDAOList = (List<OrderStatus>) list;
        orderStatusDAOList.stream().forEach(
                orderStatus-> {
                    CriteriaINSERT criteriaINSERT = new CriteriaINSERT("order_status");
                    criteriaINSERT.insert("id_order_status", "reference_order", "order_status","datetime");
                    criteriaINSERT.values(orderStatus.getIdOrderStatus(), orderStatus.getReference_order(), orderStatus.getOrderStatus(),orderStatus.getUpdateAt());
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
        return (List<T>) orderStatusDAOList;
    }

    @Override
    public <T> List<T> findAll() {
        List<T> orderStatusList = new ArrayList<>();

        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("order_status");
        criteriaSELECT.select("id_order_status", "reference_order", "order_status","datetime");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id_order_status");
                String reference_order = resultSet.getString("reference_order");
                String order_status = resultSet.getString("order_status");
                LocalDateTime datetime = resultSet.getTimestamp("datetime").toLocalDateTime();
                OrderStatus ingredient = new OrderStatus(id,reference_order, OrderStatusType.valueOf(order_status),datetime);
                orderStatusList.add((T) ingredient);
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return orderStatusList;
    }

    @Override
    public <T> T findById(double id) {
        OrderStatus orderStatus = null;
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("order_status");
        criteriaSELECT.select("id_order_status","reference_order","order_status","datetime");
        criteriaSELECT.and("id_order_status");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                orderStatus.setReference_order(resultSet.getString("reference_order"));
                orderStatus.setOrderStatus(OrderStatusType.valueOf(resultSet.getString("order_status")));
                orderStatus.setUpdateAt(resultSet.getTimestamp("datetime").toLocalDateTime());
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T) orderStatus;
    }

    @Override
    public <T> T deleteById(double id) {
        OrderStatus orderStatus = findById(id);
        if (orderStatus == null) {
            throw new RuntimeException("this orderStatus does not exist");
        }
        CriteriaDELETE criteriaDELETE = new CriteriaDELETE("order_status");
        criteriaDELETE.where("id_order_status",id);
        String query = criteriaDELETE.build();
        try(Connection connection = ds.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            preparedStatement.execute();
        }catch (SQLException |RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T)orderStatus;
    }

    @Override
    public <T> List<T> deleteAll(List<T> list) {
        List<OrderStatus> orderStatusList = (List<OrderStatus>) list;
        orderStatusList.stream().forEach(ingredient -> {
            deleteById(ingredient.getIdOrderStatus());
        });
        return (List<T>) orderStatusList;
    }

    @Override
    public <T> List<T> update(List<T> t) {
        List<OrderStatus> orderStatusList = (List<OrderStatus>) t;
        orderStatusList.stream().forEach(
                orderStatus -> {
                    Ingredient verification = findById(orderStatus.getIdOrderStatus());
                    if (verification == null){
                        throw new RuntimeException("this orderStatus does not exist");
                    }
                    CriteriaUPDATE criteriaINSERT = new CriteriaUPDATE("order_status");
                    criteriaINSERT.set( "reference_order",orderStatus.getReference_order());
                    criteriaINSERT.set( "order_status",orderStatus.getOrderStatus());
                    criteriaINSERT.set( "datetime",orderStatus.getUpdateAt());
                    criteriaINSERT.and("id_order_status",orderStatus.getIdOrderStatus());
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
