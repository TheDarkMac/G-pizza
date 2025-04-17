package torn.ando.gpizzasb.gpizza.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import torn.ando.gpizzasb.gpizza.entity.BestSales;
import torn.ando.gpizzasb.gpizza.entityRest.BestSalesRest;
import torn.ando.gpizzasb.gpizza.service.BestSalesService;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/dashboard")
public class BestSalesController {

    private BestSalesService bestSalesService;

    @PutMapping
   public ResponseEntity<List<BestSales>> getStatus(@RequestBody BestSalesRest bestSales) {
        List<BestSales> bs = bestSalesService.getBestSales(bestSales);
        return ResponseEntity.ok(bs);
    }
}
