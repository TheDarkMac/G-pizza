package torn.ando.gpizzasb.gpizza.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaINSERT;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaSELECT;
import torn.ando.gpizzasb.gpizza.dataSource.DataSource;
import torn.ando.gpizzasb.gpizza.entity.OrderStatus;
import torn.ando.gpizzasb.gpizza.enums.OrderStatusType;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
public class OrderStatusDAO {

    private DataSource dataSource;

    public List<OrderStatus> findOrderStatusByOrderReference(String referenceOrder){
        List<OrderStatus> orderStatusList = new ArrayList<>();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("order_status");
        criteriaSELECT.select("id_order_status","reference_order","order_status","datetime")
                .and("reference_order");
        String query = criteriaSELECT.build();

        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, referenceOrder);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                OrderStatus orderStatus = mapFromResultSet(resultSet);
                orderStatusList.add(orderStatus);
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return orderStatusList;
    }

    public List<OrderStatus> saveAll(List<OrderStatus> orderStatusList) {
        List<OrderStatus> list = new ArrayList<>();
        List<OrderStatus> o = (List<OrderStatus>) orderStatusList;
        o.forEach(orderStatus->{
            OrderStatus newOrderStatus = null;
            CriteriaINSERT criteriaINSERT = new CriteriaINSERT("order_status");
            criteriaINSERT.insert("reference_order","order_status","datetime")
                    .values("?","?","?")
                    .returning("id_order_status","reference_order","order_status","datetime");
            String query = criteriaINSERT.build();

            try(Connection connection = dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setObject(1,orderStatus.getReferenceOrder(), Types.OTHER);
                preparedStatement.setObject(2,orderStatus.getOrderStatus(), Types.OTHER);
                preparedStatement.setObject(3,orderStatus.getDatetime(), Types.OTHER);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    newOrderStatus = mapFromResultSet(resultSet);
                }
            }catch (SQLException | RuntimeException e) {
                throw new RuntimeException(e);
            }
            list.add(newOrderStatus);

        });
        return list;
    }

    public OrderStatus mapFromResultSet(ResultSet rs) throws SQLException {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setReferenceOrder(rs.getString("reference_order"));
        orderStatus.setId_order_status(rs.getLong("id_order_status"));
        orderStatus.setOrderStatus(rs.getObject("order_status", OrderStatusType.class));
        orderStatus.setDatetime(rs.getObject("datetime", LocalDateTime.class));
        return orderStatus;
    }
}
