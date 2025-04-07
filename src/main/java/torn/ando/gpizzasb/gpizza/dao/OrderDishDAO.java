package torn.ando.gpizzasb.gpizza.dao;

import lombok.AllArgsConstructor;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaINSERT;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaSELECT;
import torn.ando.gpizzasb.gpizza.dataSource.DataSource;
import torn.ando.gpizzasb.gpizza.entity.Dish;
import torn.ando.gpizzasb.gpizza.entity.OrderDish;
import torn.ando.gpizzasb.gpizza.entity.OrderDishStatus;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class OrderDishDAO implements DAOSchema{

    private DataSource dataSource;
    private DishDAO dishDAO;
    private OrderDishStatusDAO orderDishStatusDAO;

    @Override
    public <T> List<T> saveAll(List<T> object) {
        List<OrderDish> saveList = new ArrayList<>();
        List<OrderDish> orderDishList = (List<OrderDish>) object;
        orderDishList.forEach(orderDish -> {
            CriteriaINSERT criteriaINSERT = new CriteriaINSERT("dish_order");
            criteriaINSERT.insert("reference_order","reference_dish_order","id_dish","quantity")
                    .onConflict("id_dish")
                    .doUpdate("quantity",orderDish.getQuantity())
                    .returning("id_dish_order","reference_order","reference_dish_order","id_dish");
            String query = criteriaINSERT.build();
            try(Connection connection = dataSource.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,orderDish.getOrder().getReference());

                preparedStatement.setDouble(3,orderDish.getDish().getId());
                preparedStatement.setDouble(4,orderDish.getQuantity());

                // if need update
                preparedStatement.setDouble(5,orderDish.getQuantity());

                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    OrderDish orderDish1 = mapFromResultSet(resultSet);
                    saveList.add(orderDish1);
                }
            }catch (SQLException |RuntimeException e){
                throw new RuntimeException(e);
            }
        });
        return (List<T>) saveList;
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

    public List<OrderDish> findByOrderReference(String reference) {
        List<OrderDish> orderDishList = new ArrayList<>();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("dish_order");
        criteriaSELECT
                .select(
                        "dish_order.id_dish_order","id_dish","dish_order.reference_order","number", //about dish_order
                        "id_order_dish_status","order_dish_status","update_at" // order_dish_status
                )
                .join("LEFT","order_dish_status","dish_order.reference_order = order_dish_status.reference_order")
                .and("dish_order.reference_order");

        String query = criteriaSELECT.build();
        try(Connection connection = new DataSource().getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, reference);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                OrderDish orderDish = mapFromResultSet(resultSet);
                orderDishList.add(orderDish);
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
        return orderDishList;
    }

    public OrderDish mapFromResultSet(ResultSet resultSet) throws SQLException {
        OrderDish orderDish = new OrderDish();
        List<OrderDishStatus> orderDishStatusList = orderDishStatusDAO.findByOrderDishId(resultSet.getLong("id_dish_order"));
        orderDish.setId(resultSet.getLong("id_dish_order"));
        Dish dish = dishDAO.findById(resultSet.getLong("id_dish"));
        orderDish.setDish(dish);
        orderDish.setQuantity(resultSet.getDouble("number"));
        orderDish.setOrderStatusList(orderDishStatusList);
        return orderDish;
    }
}
