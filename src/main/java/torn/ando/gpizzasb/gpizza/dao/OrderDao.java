package torn.ando.gpizzasb.gpizza.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaSELECT;
import torn.ando.gpizzasb.gpizza.dataSource.DataSource;
import torn.ando.gpizzasb.gpizza.entity.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
@Repository
public class OrderDao implements DAOSchema{

    private DataSource dataSource;

    @Override
    public <T> List<T> saveAll(List<T> list) {
        return List.of();
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
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("\"order\"");
        criteriaSELECT.select("id_order","reference_order","datetime_of_order")
                .and("reference_order");
        String query = criteriaSELECT.build();

        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, reference);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return mapFromResultSet(resultSet);
            }
        }catch (RuntimeException | SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public Order mapFromResultSet(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getLong("id_order"));
        order.setReference(resultSet.getString("reference_order"));
        order.setOrderDate(resultSet.getTimestamp("datetime_of_order").toLocalDateTime());
        return order;
    }
}
