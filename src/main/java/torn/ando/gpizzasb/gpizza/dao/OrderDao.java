package torn.ando.gpizzasb.gpizza.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaDELETE;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaINSERT;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaSELECT;
import torn.ando.gpizzasb.gpizza.dataSource.DataSource;
import torn.ando.gpizzasb.gpizza.entity.Order;
import torn.ando.gpizzasb.gpizza.entity.OrderDish;
import torn.ando.gpizzasb.gpizza.entity.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
public class OrderDao implements DAOSchema{

    private OrderDishDAO orderDishDAO;
    private OrderStatusDAO orderStatusDAO;
    private DataSource dataSource;

    @Override
    public <T> List<T> saveAll(List<T> list) {
        List<Order> orders = (List<Order>) list;
        List<Order> orderList = new ArrayList<>();
        orders.forEach(order -> {
            CriteriaINSERT criteriaINSERT = new CriteriaINSERT("\"order\"");
            criteriaINSERT.insert("reference_order","datetime_of_order")
                    .values("?","?")
                    .onConflict("reference_order")
                    .doUpdate("datetime_of_order","?")
                    .returning("id_order","reference_order","datetime_of_order");
            String query = criteriaINSERT.build();
            try(Connection connection = dataSource.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,order.getReference());
                preparedStatement.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));

                //if on conflict, make update
                preparedStatement.setTimestamp(3, Timestamp.valueOf(order.getOrderDate()));

                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    Order o = mapFromResultSet(resultSet);
                    orderList.add(o);
                }
            }catch (SQLException |RuntimeException e){
                throw new RuntimeException(e);
            }

        });
        return (List<T>) orderList;
    }

    @Override
    public <T> List<T> findAll() {
        return List.of();
    }

    @Override
    public <T> T findById(double id) {
        return null;
    }

    @Override
    public <T> T deleteById(double id) {
        return null;
    }

    @Override
    public <T> List<T> deleteAll(List<T> list) {
        return List.of();
    }

    @Override
    public <T> List<T> updateAll(List<T> t) {
        return List.of();
    }

    public Order findByReference(String reference){
        Order order = null;
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("\"order\"");
        criteriaSELECT.select("id_order","reference_order","datetime_of_order")
                .and("reference_order");
        String query = criteriaSELECT.build();

        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, reference);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                order = mapFromResultSet(resultSet);
            }
        }catch (RuntimeException | SQLException e){
            throw new RuntimeException(e);
        }
        return order;
    }

    public void deleteByReference(String reference){
        CriteriaDELETE criteriaDELETE = new CriteriaDELETE("\"order\"");
        criteriaDELETE.where("reference_order",reference);
        String query = criteriaDELETE.build();
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, reference);
            preparedStatement.executeUpdate();
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public Order mapFromResultSet(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        List<OrderStatus> orderStatusList = orderStatusDAO
                .findOrderStatusByOrderReference(resultSet.getString("reference_order"));
        if(!orderStatusList.isEmpty()){
            order.setOrderStatusList(orderStatusList);
        }else{
            order.setOrderStatusList(List.of());
        }
        List<OrderDish> orderDishList = orderDishDAO.findByOrderReference(resultSet.getString("reference_order"));
        if(!orderDishList.isEmpty()){
            order.setOrderDishList(orderDishList);
        }else{
            order.setOrderDishList(List.of());
        }
        order.setId(resultSet.getLong("id_order"));
        order.setReference(resultSet.getString("reference_order"));
        order.setOrderDate(resultSet.getTimestamp("datetime_of_order").toLocalDateTime());
        return order;
    }
}
