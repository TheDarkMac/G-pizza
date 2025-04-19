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
            criteriaINSERT.insert("reference_order","id_dish","quantity")
                    .values("?","?","?")
                    .onConflict("id_dish","reference_order")
                    .doUpdate("quantity",orderDish.getQuantity())
                    .returning("reference_order","id_dish","quantity");
            String query = criteriaINSERT.build();
            try(Connection connection = dataSource.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,orderDish.getOrder().getReference());
                preparedStatement.setDouble(2,orderDish.getDish().getId());
                preparedStatement.setDouble(3,orderDish.getQuantity());

                // if need update
                preparedStatement.setDouble(4,orderDish.getQuantity());

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
                        "id_dish","reference_order","quantity"
                )
                .and("reference_order");

        String query = criteriaSELECT.build();
        try(Connection connection = new DataSource().getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, reference);
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

    public OrderDish findByReferenceAndDishId(String reference,Long dishId) {

        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("dish_order");
        criteriaSELECT
                .select(
                        "id_dish","reference_order","quantity"
                )
                .and("reference_order")
                .and("id_dish")
        ;

        String query = criteriaSELECT.build();
        try(Connection connection = new DataSource().getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, reference);
            preparedStatement.setLong(2, dishId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                return mapFromResultSet(resultSet);
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public OrderDish mapFromResultSet(ResultSet resultSet) throws SQLException {
        OrderDish orderDish = new OrderDish();
        List<OrderDishStatus> orderDishStatusList = orderDishStatusDAO.findByReferenceAndDishId(resultSet.getString("reference_order"), resultSet.getLong("id_dish"));
        if(orderDishStatusList != null){
            orderDish.setOrderStatusList(orderDishStatusList);
        }
        Dish dish = dishDAO.findById(resultSet.getLong("id_dish"));
        orderDish.setDish(dish);
        orderDish.setQuantity(resultSet.getDouble("quantity"));
        return orderDish;
    }
}
