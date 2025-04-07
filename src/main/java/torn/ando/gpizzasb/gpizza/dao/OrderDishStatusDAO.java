package torn.ando.gpizzasb.gpizza.dao;

import torn.ando.gpizzasb.gpizza.criteria.CriteriaINSERT;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaSELECT;
import torn.ando.gpizzasb.gpizza.dataSource.DataSource;
import torn.ando.gpizzasb.gpizza.entity.OrderDishStatus;
import torn.ando.gpizzasb.gpizza.enums.OrderStatusType;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDishStatusDAO implements DAOSchema{
    private final OrderDishStatus orderDishStatus;
    DataSource dataSource = new DataSource();

    public OrderDishStatusDAO(OrderDishStatus orderDishStatus) {
        this.orderDishStatus = orderDishStatus;
    }

    @Override
    public <T> List<T> saveAll(List<T> object) {
        List<OrderDishStatus> orderDishStatusList = new ArrayList<>();
        List<OrderDishStatus> o = (List<OrderDishStatus>) object;
        o.forEach(orderDishStatus -> {
            CriteriaINSERT criteriaINSERT = new CriteriaINSERT("order_dish_status");
            criteriaINSERT.insert("reference_order","order_status","reference_dish_order")
                    .returning("id_order_dish_status","reference_order","order_status","reference_dish_order");
        String query = criteriaINSERT.build();
        try(Connection connection = dataSource.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,"reference_order");
                preparedStatement.setString(2,"order_status");
                preparedStatement.setString(3,"reference_dish_order");
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    OrderDishStatus orderDishStatusL = mapFromResultSet(resultSet);
                    orderDishStatusList.add(orderDishStatusL);
                }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        });
        return (List<T>) orderDishStatusList;
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
    public <T> List<T> updateAll(List<T> object) {
        return List.of();
    }

    public List<OrderDishStatus> findByOrderDishId(long orderDishId) {
        List<OrderDishStatus> orderDishStatusList = new ArrayList<>();

        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("order_dish_status");
        criteriaSELECT
                .select("id_order_dish_status","reference_order","id_dish_order","order_dish_status","update_at")
                .and("id_dish_order")
        ;
        String query = criteriaSELECT.build();
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, orderDishId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                OrderDishStatus orderDishStatus = mapFromResultSet(resultSet);
                orderDishStatusList.add(orderDishStatus);
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }

        return orderDishStatusList;
    }

    public OrderDishStatus mapFromResultSet(ResultSet resultSet) throws SQLException {
        OrderDishStatus orderDishStatus = new OrderDishStatus();
        orderDishStatus.setId(resultSet.getLong("id_order_dish_status"));
        orderDishStatus.setOrderStatus(OrderStatusType.valueOf(resultSet.getString("order_dish_status")));
        orderDishStatus.setUpdateAt(resultSet.getTimestamp("update_at").toLocalDateTime());
        return orderDishStatus;
    }
}
