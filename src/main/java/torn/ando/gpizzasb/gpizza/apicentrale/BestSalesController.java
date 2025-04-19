package torn.ando.gpizzasb.gpizza.apicentrale;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import torn.ando.gpizzasb.gpizza.entity.BestSales;
import torn.ando.gpizzasb.gpizza.entityRest.BestSalesRest;
import torn.ando.gpizzasb.gpizza.mapper.RestMapper;
import torn.ando.gpizzasb.gpizza.service.BestSalesService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class BestSalesController {

    //private final BestSalesService bestSalesService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final BestSalesService bestSalesService;
    private final RestMapper restMapper;

    @GetMapping("/{referenceSalesPoint}")
    public ResponseEntity<List<BestSales>> getStatus(
            @PathVariable String referenceSalesPoint,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {

        String url = "http://localhost:8080/" + referenceSalesPoint + "/bestSales?from=" + from + "&to=" + to;

        ResponseEntity<List<BestSalesRest>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BestSalesRest>>() {}
        );
        List<BestSales> modifiedList = response.getBody()
                .stream()
                .peek(item -> {
                    item.setReference(referenceSalesPoint);
                    item.setFrom(from);
                    item.setTo(to);
                })
                .map(restMapper::mapToBestSales)
                .collect(Collectors.toList());
        bestSalesService.saveAll(modifiedList);
        return ResponseEntity.ok(modifiedList);
    }
}
