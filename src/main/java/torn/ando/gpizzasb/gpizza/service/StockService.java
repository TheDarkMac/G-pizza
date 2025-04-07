package torn.ando.gpizzasb.gpizza.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import torn.ando.gpizzasb.gpizza.dao.StockDAO;
import torn.ando.gpizzasb.gpizza.entity.Ingredient;
import torn.ando.gpizzasb.gpizza.entity.Stock;
import torn.ando.gpizzasb.gpizza.entityRest.StockRest;
import torn.ando.gpizzasb.gpizza.mapper.RestMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@Service
public class StockService {

    private final RestMapper restMapper;
    private StockDAO stockDAO;

    public Stock findById(Long id) {
        return stockDAO.findById(id);
    }

    public List<Stock> findAll() {
        return stockDAO.findAll();
    }

    public List<Stock> findByIngredient(Ingredient ingredient) {
        return stockDAO.findByIdIngredient(ingredient.getId());
    }

    public List<Stock> saveAll(List<StockRest> stockList) {
        List<Stock> stocks = stockList.stream()
                .map(stockRest -> restMapper.mapToStock(stockRest))
                .collect(Collectors.toList());
        return stockDAO.saveAll(stocks);
    }

}
