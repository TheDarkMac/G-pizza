package torn.ando.gpizzasb.gpizza.dao;

import torn.ando.gpizzasb.gpizza.criteria.CriteriaINSERT;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaSELECT;
import torn.ando.gpizzasb.gpizza.dataSource.DataSource;
import torn.ando.gpizzasb.gpizza.enums.MovementType;
import torn.ando.gpizzasb.gpizza.entity.Stock;
import org.springframework.stereotype.Repository;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StockDAO implements DAOSchema{

    DataSource dataSource = new DataSource();

    @Override
    public <T> List<T> saveAll(List<T> stockList) {
        List<Stock> stocks = (List<Stock>)stockList;
        CriteriaINSERT criteriaINSERT =  new CriteriaINSERT("stock");
        criteriaINSERT.insert("id_ingredient","quantity","date_of_movement","movement_type")
                .values("?","?","?","?")
        ;
        String query = criteriaINSERT.build();
        stocks.forEach(stock -> {
            try (Connection connection = dataSource.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setDouble(1, stock.getIngredient().getId());
                preparedStatement.setDouble(2,stock.getQuantityINOUT());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.setObject(4, stock.getMovementType(),Types.OTHER);
                preparedStatement.executeUpdate();
            }catch (SQLException | RuntimeException e){
                throw new RuntimeException(e);
            }
        });
        return stockList;
    }

    @Override
    public <T> List<T> findAll() {
        CriteriaSELECT criteriaSELECT =  new CriteriaSELECT("stock");
        criteriaSELECT.select("id_stock","id_ingredient","quantity","date_of_movement","movement_type");
        String query = criteriaSELECT.build();;

        List<Stock> stockList = new ArrayList<>();
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Stock stock = new Stock();
                stock.setId(resultSet.getLong("id_stock"));
                stock.setQuantityINOUT(resultSet.getDouble("quantity"));
                stock.setMovementType(MovementType.valueOf(resultSet.getString("movement_type")));
                stock.setDateOfMovement(resultSet.getTimestamp("date_of_movement").toLocalDateTime());
                stockList.add(stock);
            }

        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        }
        return (List<T>) stockList;
    }

    @Override
    public <T> T findById(double id) {

        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public <T> T deleteById(double id) {

        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public <T> List<T> deleteAll(List<T> list) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public <T> List<T> updateAll(List<T> object) {

        throw new UnsupportedOperationException("not yet implemented");
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
