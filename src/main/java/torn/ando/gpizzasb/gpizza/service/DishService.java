package torn.ando.gpizzasb.gpizza.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import torn.ando.gpizzasb.gpizza.dao.DishDAO;
import torn.ando.gpizzasb.gpizza.entity.Dish;
import torn.ando.gpizzasb.gpizza.enums.DurationUnit;
import torn.ando.gpizzasb.gpizza.enums.StatisticType;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class DishService {
    private DishDAO dishDAO;
    public List<Dish> findAll() {
        return dishDAO.findAll();
    }

    public Dish findById(Long id) {
        return dishDAO.findById(id);
    }

    public List<Dish> saveAll(List<Dish> dishes) {
        return dishDAO.saveAll(dishes);
    }

    public double getProcessingTime(Long dishId, LocalDate startDate, LocalDate endDate,
                                    DurationUnit timeUnit, StatisticType statType) {
        if (dishId == null || startDate == null || endDate == null) {
            throw new IllegalArgumentException("Dish ID and date range must be provided");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        DurationUnit unit = (timeUnit != null) ? timeUnit : DurationUnit.SECONDS;
        StatisticType stat = (statType != null) ? statType : StatisticType.AVERAGE;

        return dishDAO.calculateProcessingTime(dishId, startDate, endDate, unit, stat);
    }

}
