package torn.ando.gpizzasb.gpizza.salespoint;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import torn.ando.gpizzasb.gpizza.entityRest.BestSalesRest;
import torn.ando.gpizzasb.gpizza.service.BestSalesService;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/analamahitsy/bestSales")
public class BestSalesSP {

    private BestSalesService bestSalesService;

    @GetMapping
    public ResponseEntity<List<torn.ando.gpizzasb.gpizza.entity.BestSales>> getStatus(@RequestParam LocalDate from, @RequestParam LocalDate to) {
        BestSalesRest bestSalesRest = new BestSalesRest();
        bestSalesRest.setFrom(from);
        bestSalesRest.setTo(to);
        bestSalesRest.setSize(10);
        System.out.println(bestSalesRest);
        List<torn.ando.gpizzasb.gpizza.entity.BestSales> bs = bestSalesService.getBestSales(bestSalesRest);
        return ResponseEntity.ok(bs);
    }
}
