package torn.ando.gpizzasb.gpizza.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import torn.ando.gpizzasb.gpizza.dao.BestSalesDAO;
import torn.ando.gpizzasb.gpizza.entity.BestSales;
import torn.ando.gpizzasb.gpizza.entityRest.BestSalesRest;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class BestSalesService {

    private BestSalesDAO bestSalesDAO;

    public List<BestSales> getBestSales(BestSalesRest bestSalesRest) {
        return bestSalesDAO.getBestSales(bestSalesRest.getSize(), bestSalesRest.getFrom(), bestSalesRest.getTo());
    }

}
