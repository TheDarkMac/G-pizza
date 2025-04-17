package torn.ando.gpizzasb.gpizza.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaSELECT;
import torn.ando.gpizzasb.gpizza.dataSource.DataSource;
import torn.ando.gpizzasb.gpizza.entity.BestSales;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
public class BestSalesDAO {

    private DataSource dataSource;

    public List<BestSales> getBestSales(Integer size, LocalDate from, LocalDate to) {
        List<BestSales> bestSalesList = new ArrayList<>();
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("dish_order \"do\"");
        criteriaSELECT.select("name",
                "SUM(\"do\".quantity) AS total_quantity_sold",
                "SUM(\"do\".quantity * d.unit_price) AS total_revenue"
                )
                .join("INNER","\"order\" o","\"do\".reference_order = o.reference_order")
                .join("INNER","dish d","\"do\".id_dish = d.id_dish")
                .join("INNER","order_status os","os.reference_order = o.reference_order")
                .and("os.order_status")
                .andBetween("os.datetime","?","?")
                .orderBy("total_quantity_sold",false)
                .groupBy("d.name")
                .limit(size)
        ;
        String query = criteriaSELECT.build();

        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1,"CREATED",Types.OTHER);
            preparedStatement.setDate(2, Date.valueOf(from));
            preparedStatement.setDate(3, Date.valueOf(to));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                BestSales bestSales = mapFromResultSet(resultSet);
                bestSalesList.add(bestSales);
            }
        }catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
        return bestSalesList;
    }

    public BestSales mapFromResultSet(ResultSet resultSet) throws SQLException {
        BestSales bestSales = new BestSales();
        bestSales.setDishName(resultSet.getString("name"));
        bestSales.setQuantity(resultSet.getInt("total_quantity_sold"));
        bestSales.setBenefice(resultSet.getDouble("total_revenue"));
        return bestSales;
    }
}
