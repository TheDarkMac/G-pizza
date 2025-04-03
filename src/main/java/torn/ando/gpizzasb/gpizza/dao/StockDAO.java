package torn.ando.gpizzasb.gpizza.dao;

import torn.ando.gpizzasb.gpizza.criteria.CriteriaSELECT;
import torn.ando.gpizzasb.gpizza.dataSource.DataSource;
import torn.ando.gpizzasb.gpizza.enums.MovementType;
import torn.ando.gpizzasb.gpizza.entity.Stock;
import org.springframework.stereotype.Repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StockDAO implements DAOSchema{

    DataSource dataSource = new DataSource();

    @Override
    public <T> List<T> saveAll(List<T> object) {
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
    public <T> List<T> updateAll(List<T> object) {
        return List.of();
    }

    public List<Stock> findByIdIngredient(long id) {
        List<Stock> stockList = new ArrayList<>();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("stock");
        criteriaSELECT.select("id_stock","stock.id_ingredient","quantity","date_of_movement","movement_type"
        );
        criteriaSELECT.join("LEFT","ingredient","ingredient.id_ingredient = stock.id_ingredient");

        criteriaSELECT.and("stock.id_ingredient");
        String query = criteriaSELECT.build();
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Stock stock = mapFromResultSet(resultSet);
                stockList.add(stock);
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
        return stockList;
    }

    public Stock mapFromResultSet(ResultSet rs) throws SQLException {
        Stock stock = new Stock();
        stock.setId(rs.getLong("id_stock"));
        stock.setQuantityINOUT(rs.getLong("quantity"));
        stock.setDateOfMovement(rs.getTimestamp("date_of_movement").toLocalDateTime());
        stock.setMovementType(MovementType.valueOf(rs.getString("movement_type")));
        return stock;
    }
}
