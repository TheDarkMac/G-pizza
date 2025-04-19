package torn.ando.gpizzasb.gpizza.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaINSERT;
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
                "d.id_dish"
                )
                .join("INNER","\"order\" o","\"do\".reference_order = o.reference_order")
                .join("INNER","dish d","\"do\".id_dish = d.id_dish")
                .join("INNER","order_status os","os.reference_order = o.reference_order")
                .and("os.order_status")
                .andBetween("os.datetime","?","?")
                .orderBy("total_quantity_sold",false)
                .groupBy("d.name","d.id_dish")
                .limit(size)
        ;
        String query = criteriaSELECT.build();

        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            // just test fa rehefa aminy farany dia atao DONE io
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
        bestSales.setDishId(resultSet.getLong("id_dish"));
        bestSales.setDishName(resultSet.getString("name"));
        bestSales.setQuantity(resultSet.getInt("total_quantity_sold"));
        return bestSales;
    }

    public List<BestSales> saveAll(List<BestSales> bestSales) {
        List<BestSales> bestSalesList = new ArrayList<>();
        bestSales.forEach(bestSales1 -> {
            CriteriaINSERT criteriaINSERT = new CriteriaINSERT("best_sales");
            criteriaINSERT.insert("reference","id_dish","quantity","\"from\"","\"to\"","benefice");
            criteriaINSERT.values("?","?","?","?","?","?")
                    .onConflict("reference","id_dish","\"from\"","\"to\"")
                    .returning("reference","id_dish","quantity","\"from\"","\"to\"")
            ;
            String query = criteriaINSERT.build();
            System.out.println(query);
            try(Connection connection = dataSource.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1,bestSales1.getReference());
                preparedStatement.setDouble(2,bestSales1.getDishId());
                preparedStatement.setDouble(3,bestSales1.getQuantity());
                preparedStatement.setDate(4, Date.valueOf(bestSales1.getFrom()));
                preparedStatement.setDate(5, Date.valueOf(bestSales1.getTo()));
                preparedStatement.setDouble(6,bestSales1.getBenefice());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    BestSales bestSales2 = new BestSales();
                    bestSales2.setDishId(resultSet.getLong("id_dish"));
                    bestSales2.setReference(resultSet.getString("reference"));
                    bestSales2.setQuantity((int) resultSet.getDouble("quantity"));
                    bestSales2.setFrom(resultSet.getDate("\"from\"").toLocalDate());
                    bestSales2.setTo(resultSet.getDate("\"to\"").toLocalDate());
                    bestSales2.setBenefice(resultSet.getDouble("benefice"));
                    bestSalesList.add(bestSales2);
                }
            } catch (SQLException|RuntimeException e) {
                throw new RuntimeException(e);
            }
        });
        return bestSalesList;
    }
}
