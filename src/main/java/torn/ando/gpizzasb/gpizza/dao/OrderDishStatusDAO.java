package torn.ando.gpizzasb.gpizza.dao;

import torn.ando.gpizzasb.gpizza.criteria.CriteriaINSERT;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaSELECT;
import torn.ando.gpizzasb.gpizza.dataSource.DataSource;
import torn.ando.gpizzasb.gpizza.entity.Dish;
import torn.ando.gpizzasb.gpizza.entity.OrderDishStatus;
import torn.ando.gpizzasb.gpizza.enums.OrderStatusType;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDishStatusDAO implements DAOSchema{
    private final OrderDishStatus orderDishStatus;
    private final DishDAO dishDAO;
    DataSource dataSource = new DataSource();

    public OrderDishStatusDAO(OrderDishStatus orderDishStatus, DishDAO dishDAO) {
        this.orderDishStatus = orderDishStatus;
        this.dishDAO = dishDAO;
    }

    @Override
    public <T> List<T> saveAll(List<T> object) {
        List<OrderDishStatus> orderDishStatusList = new ArrayList<>();
        List<OrderDishStatus> o = (List<OrderDishStatus>) object;
        o.forEach(orderDishStatus -> {
            CriteriaINSERT criteriaINSERT = new CriteriaINSERT("order_dish_status");
            criteriaINSERT.insert("id_dish","reference_order","order_status","updated_at")
                    .values("?","?","?","?")
                    .onConflict("id_dish","reference_order","order_status")
                    .returning("id_dish","reference_order","order_status","updated_at");
        String query = criteriaINSERT.build();
        try(Connection connection = dataSource.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setDouble(1, orderDishStatus.getOrderDish().getDish().getId());
                preparedStatement.setString(2,orderDishStatus.getOrderDish().getOrder().getReference());
                preparedStatement.setObject(3,orderDishStatus.getOrderStatus(),Types.OTHER);
                preparedStatement.setTimestamp(4, Timestamp.valueOf(orderDishStatus.getUpdateAt()));
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
                .select("id_dish","reference_order","order_status","updated_at")
                .and("id_dish")
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

    public List<OrderDishStatus> findByReferenceAndDishId(String reference, long dishId) {
        List<OrderDishStatus> orderDishStatusList = new ArrayList<>();

        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("order_dish_status");
        criteriaSELECT
                .select("id_dish","reference_order","order_status","updated_at")
                .and("id_dish")
                .and("reference_order")
        ;
        String query = criteriaSELECT.build();
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, dishId);
            preparedStatement.setString(2, reference);
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
        Dish dish = dishDAO.findById(resultSet.getLong("id_dish"));
        orderDishStatus.setOrderStatus(OrderStatusType.valueOf(resultSet.getString("order_status")));
        orderDishStatus.setUpdateAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        return orderDishStatus;
    }
}
