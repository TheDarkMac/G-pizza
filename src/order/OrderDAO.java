package order;

import criteria.CriteriaINSERT;
import criteria.CriteriaSELECT;
import datasource.DataSource;
import dish.Dish;

import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDAO {
    private DataSource ds = new DataSource();

    public Order createOrder(Order order){
        CriteriaINSERT criteriaINSERTOrder = new CriteriaINSERT("\"order\"");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        criteriaINSERTOrder.insert("datetime_of_order").values(timestamp);
        Order actualOrder = null;
        try (Connection connection = ds.getConnection()){
            String query = criteriaINSERTOrder.build();
            System.out.println("query1 : "+query);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, timestamp,Types.OTHER);
            boolean resultSet = preparedStatement.execute();

            actualOrder = findOrderByDateTime(timestamp.toLocalDateTime());


        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }

        Order finalActualOrder = actualOrder;
        order.getDishesOrder().entrySet().forEach(dishOrderEntry -> {
            CriteriaINSERT criteriaINSERTDishOrder = new CriteriaINSERT("dish_order");
            Dish dish = dishOrderEntry.getKey();
            criteriaINSERTDishOrder.insert("id_dish","id_order").values(BigInteger.valueOf(dish.getId()), BigInteger.valueOf((long) finalActualOrder.getId()));

            String query = criteriaINSERTDishOrder.build();
            System.out.println("query2: " + query);
            try(Connection connection = ds.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                List<Object> params = criteriaINSERTDishOrder.getParameters();
                for(int i = 0; i < params.size(); i++){
                    preparedStatement.setObject(i+1, params.get(i), Types.OTHER);
                }
                boolean b = preparedStatement.execute();
            }catch (RuntimeException | SQLException e){
                throw new RuntimeException(e);
            }
        });
        return order;
    }

    public Order findOrderByDateTime(LocalDateTime dateTime){

        Order order = new Order();
        try(Connection connection = ds.getConnection()){
            CriteriaSELECT criteriaSELECT = new CriteriaSELECT("\"order\"");
            criteriaSELECT.select("id_order","reference_order","datetime_of_order");
            criteriaSELECT.and("datetime_of_order = ?");
            String query = criteriaSELECT.build();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject( 1, dateTime,Types.OTHER);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                order.setId(resultSet.getInt("id_order"));
                order.setDateOfOrder(resultSet.getTimestamp("datetime_of_order").toLocalDateTime());
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return order;
    }
}
