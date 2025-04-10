package torn.ando.gpizzasb.gpizza.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import torn.ando.gpizzasb.gpizza.entity.Order;
import torn.ando.gpizzasb.gpizza.entity.OrderDish;
import torn.ando.gpizzasb.gpizza.entity.OrderDishStatus;
import torn.ando.gpizzasb.gpizza.entityRest.OrderDishRest;
import torn.ando.gpizzasb.gpizza.entityRest.OrderDishStatusRest;
import torn.ando.gpizzasb.gpizza.mapper.RestMapper;
import torn.ando.gpizzasb.gpizza.service.OrderDishService;
import torn.ando.gpizzasb.gpizza.service.OrderDishStatusService;
import torn.ando.gpizzasb.gpizza.service.OrderService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("orders")
public class OrderController {
    private RestMapper restMapper;
    private OrderService orderService;
    private OrderDishService orderDishService;
    private OrderDishStatusService orderDishStatusService;

    @GetMapping("{reference}")
    public ResponseEntity<Order> findByReference(@PathVariable String reference){
        Order order = orderService.findByReference(reference);
        if(order == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        orderService.changeOrderStatus(reference);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("{reference}/dishes")
    public ResponseEntity<Order> updateOrder(@PathVariable String reference, @RequestBody List<OrderDishRest> orderDishRestList){
        Order order = orderService.findByReference(reference);
        if(order == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<OrderDish> orderDishList = orderDishRestList
                .stream()
                .map(orderDishRest -> {
                    OrderDish orderDish = restMapper.mapToOrderDish(orderDishRest);
                    orderDish.setOrder(order);
                    return orderDish;
                })
                .toList();
        orderDishService.saveAll(orderDishList);
        Order newOrder = orderService.findByReference(reference);
    return ResponseEntity.ok(newOrder);
    }

    @PutMapping("{reference}/dishes/{dishId}")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable String reference,
            @PathVariable Long dishId,
            @RequestBody OrderDishStatusRest status){
        status.setDishId(dishId);
        status.setOrderReference(reference);
        OrderDishStatus ods = restMapper.mapToOrderDishStatus(status);
        orderDishStatusService.saveAll(List.of(ods));
        return ResponseEntity.ok(null);
    }
}
