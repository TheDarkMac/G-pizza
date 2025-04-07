package torn.ando.gpizzasb.gpizza.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import torn.ando.gpizzasb.gpizza.entity.Order;
import torn.ando.gpizzasb.gpizza.entityRest.OrderDishRest;
import torn.ando.gpizzasb.gpizza.service.OrderService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("orders")
public class OrderController {

    private OrderService orderService;

    @GetMapping("{reference}")
    public ResponseEntity<Order> findByReference(@PathVariable String reference){
        Order order = orderService.findByReference(reference);
        if(order == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("{reference}/dishes")
    public ResponseEntity<Order> updateOrder(@PathVariable String reference, @RequestBody List<OrderDishRest> orderDishRestList){
        Order order = orderService.findByReference(reference);
        if(order == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
