package torn.ando.gpizzasb.gpizza.controller;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import torn.ando.gpizzasb.gpizza.entityRest.BestSalesRest;

@RestController
@RequestMapping("/dashboard")
public class BestSalesController {

    @PutMapping
   public BestSalesRest getStatus(@RequestBody BestSalesRest bestSales) {
        return bestSales;
    }
}
