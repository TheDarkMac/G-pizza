package gpizza.order;

import gpizza.criteria.CriteriaDELETE;
import gpizza.criteria.CriteriaINSERT;
import gpizza.criteria.CriteriaSELECT;
import gpizza.criteria.CriteriaUPDATE;
import gpizza.dataSource.DAOSchema;
import gpizza.dataSource.DataSource;
import gpizza.order.orderDish.OrderDish;
import gpizza.order.orderDish.OrderDishDAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDAO implements DAOSchema {
    private DataSource ds;
    public OrderDAO() {
        ds = new DataSource();
    }

    @Override public <T> List<T> saveAll(List<T> list) {
        List<Order> orderList = (List<Order>) list;

        orderList.stream().forEach(
                order-> {
                    CriteriaINSERT criteriaINSERT = new CriteriaINSERT("\"order\"");
                    criteriaINSERT.insert("id_order", "reference_order", "datetime_of_order");
                    criteriaINSERT.values(order.getId_order(), order.getReference(), order.getOrder_datetime());
                    String query = criteriaINSERT.build();
                    try (Connection connection = ds.getConnection()) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                        List<Object> params = criteriaINSERT.getParameters();
                        for (int i = 0; i < params.size(); i++) {
                            preparedStatement.setObject(i + 1, params.get(i), Types.OTHER);
                        }
                        preparedStatement.execute(); ResultSet resultSet = preparedStatement.getGeneratedKeys();

        if(resultSet.next()) { order.setId_order(resultSet.getInt(1)); }

        if(order.getOrderDishList() != null && !order.getOrderDishList().isEmpty()) {
            List<OrderDish> orderDishList = order.getOrderDishList()
                    .stream()
                    .filter(orderDish -> orderDish.getId_order_dish() == 0)
                    .peek(orderDish -> orderDish.setOrder(order))
                    .collect(Collectors.toList());
            OrderDishDAO orderDishDAO = new OrderDishDAO();
            if(!orderList.isEmpty()) { orderDishDAO.saveAll(orderDishList); } }

    } catch (SQLException | RuntimeException e) { throw new RuntimeException(e); } }); return (List<T>) orderList; }

    @Override
    public <T> List<T> findAll() {
        List<T> orderList = new ArrayList<>();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("\"order\"");
        criteriaSELECT.select("id_order", "reference_order", "datetime_of_order");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id_order = resultSet.getInt("id_order");
                String reference = resultSet.getString("reference_order");
                OrderDishDAO orderDishDAO = new OrderDishDAO();
                List<OrderDish> orderDishList = orderDishDAO.findAllByReference(reference);
                LocalDateTime datetimeOfOrder = resultSet.getTimestamp("datetime_of_order").toLocalDateTime();
                Order order = new Order(id_order,reference, datetimeOfOrder,orderDishList);
                orderList.add((T) order);
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return orderList;
    }

    @Override
    public <T> T findById(double id) {
        Order order = null;
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("\"order\"");
        criteriaSELECT.select("id_order","reference_order","datetime_of_order");
        criteriaSELECT.and("id_order");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                order = new Order();
                order.setId_order(resultSet.getInt("id_order"));
                order.setReference(resultSet.getString("reference_order"));
                order.setOrder_datetime(resultSet.getTimestamp("datetime_of_order").toLocalDateTime());
                OrderDishDAO orderDishDAO = new OrderDishDAO();

                order.setOrderDishList(orderDishDAO.findAllByReference(order.getReference()));
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T) order;
    }

    public <T> T findByIReference(String reference_order) {
        Order order = null;
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("\"order\"");
        criteriaSELECT.select("id_order","reference_order","datetime_of_order");
        criteriaSELECT.and("reference_order");
        String query = criteriaSELECT.build();
        try(Connection connection = ds.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, reference_order);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                order = new Order();
                order.setId_order(resultSet.getInt("id_order"));
                order.setReference(resultSet.getString("reference_order"));
                order.setOrder_datetime(resultSet.getTimestamp("datetime_of_order").toLocalDateTime());
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T) order;
    }

    @Override
    public <T> T deleteById(double id) {
        Order order = findById(id);
        if (order == null) {
            throw new RuntimeException("this order does not exist");
        }
        CriteriaDELETE criteriaDELETE = new CriteriaDELETE("\"order\"");
        criteriaDELETE.where("id_order",id);
        String query = criteriaDELETE.build();
        try(Connection connection = ds.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            preparedStatement.execute();
        }catch (SQLException |RuntimeException e){
            throw new RuntimeException(e);
        }
        return (T)order;
    }

    @Override
    public <T> List<T> deleteAll(List<T> list) {
        List<Order> orderList = (List<Order>) list;
        orderList.stream().forEach(order -> {
            deleteById(order.getId_order());
        });
        return (List<T>) orderList;
    }

    @Override
    public <T> List<T> update(List<T> t) {
        List<Order> orderList = (List<Order>) t;
        orderList.stream().forEach(
                order -> {
                    Order verification = findById(order.getId_order());
                    if (verification == null){
                        throw new RuntimeException("this order does not exist");
                    }
                    CriteriaUPDATE criteriaINSERT = new CriteriaUPDATE("\"order\"");
                    criteriaINSERT.set( "reference_order",order.getReference());
                    criteriaINSERT.set( "datetime_of_order",order.getOrder_datetime());
                    criteriaINSERT.and("id_order",order.getId_order());
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
