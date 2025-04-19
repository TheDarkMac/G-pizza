package torn.ando.gpizzasb.gpizza.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import torn.ando.gpizzasb.gpizza.dao.BestSalesDAO;
import torn.ando.gpizzasb.gpizza.dao.DishDAO;
import torn.ando.gpizzasb.gpizza.dao.DishIngredientDAO;
import torn.ando.gpizzasb.gpizza.entity.BestSales;
import torn.ando.gpizzasb.gpizza.entity.Dish;
import torn.ando.gpizzasb.gpizza.entityRest.BestSalesRest;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class BestSalesService {

    private BestSalesDAO bestSalesDAO;
    private DishDAO dishDAO;
    private DishIngredientDAO dishIngredientDAO;

    public List<BestSales> getBestSales(BestSalesRest bestSalesRest) {
        List<BestSales> bestSales = bestSalesDAO.getBestSales(bestSalesRest.getSize(), bestSalesRest.getFrom(), bestSalesRest.getTo());
        bestSales.stream()
                .map(b->{
                    Dish dish =dishDAO.findById(b.getDishId());
                    return b;
                })
                .toList();
        return bestSales;
    }

    public List<BestSales> saveAll(List<BestSales> bestSales) {
        bestSales.forEach(bestSales1 -> {
            if(bestSales1.getFrom().isAfter(bestSales1.getTo())){
                throw new IllegalStateException(" \"to\" must be recent than \"from\" ");
            }
        });
        bestSales = bestSales.stream()
                .map(bestSales1 -> {
                    System.out.println(bestSales1);
                    Dish d = dishDAO.findById(bestSales1.getDishId());
                    Double gm = d.getGrossMargin();
                    bestSales1.setBenefice(gm*bestSales1.getQuantity());
                    return bestSales1;
                }).toList();
        return bestSalesDAO.saveAll(bestSales);
    }

}
