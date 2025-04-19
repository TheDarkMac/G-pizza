package torn.ando.gpizzasb.gpizza.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import torn.ando.gpizzasb.gpizza.dataSource.DataSource;
import torn.ando.gpizzasb.gpizza.entity.BestProcessingTime;
import torn.ando.gpizzasb.gpizza.entity.Dish;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
/*
@Repository
@AllArgsConstructor
public class BestTimeProcessingDAO {
    private DataSource dataSource;
    private DishDAO dishDAO;

    public List<BestProcessingTime> findAll() {
        List<BestProcessingTime> result = new ArrayList<>();
        String query = "SELECT dish_id, preparation_duration FROM dish_order_status WHERE dish_status = 'DONE'";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                long dishId = rs.getLong("dish_id");
                Duration duration = rs.getObject("preparation_duration", Duration.class);
                Dish dish = dishDAO.findById(dishId);

                BestProcessingTime bestTime = new BestProcessingTime();
                bestTime.setDish(dish);
                bestTime.setPreparationDuration(duration);
                result.add(bestTime);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}*/
@Repository
@AllArgsConstructor
public class BestTimeProcessingDAO {
    private DataSource dataSource;
    private DishDAO dishDAO;

    public List<BestProcessingTime> findAll() {
        List<BestProcessingTime> result = new ArrayList<>();
        String query = "SELECT dish_id, preparation_duration FROM dish_order_status WHERE dish_status = 'DONE'";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                long dishId = rs.getLong("dish_id");
                Duration duration = rs.getObject("preparation_duration", Duration.class);
                Dish dish = dishDAO.findById(dishId);

                BestProcessingTime bestTime = new BestProcessingTime();
                bestTime.setDish(dish);
                bestTime.setPreparationDuration(duration);
                result.add(bestTime);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
