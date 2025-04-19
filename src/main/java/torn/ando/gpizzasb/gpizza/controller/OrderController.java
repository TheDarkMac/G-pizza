package torn.ando.gpizzasb.gpizza.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import torn.ando.gpizzasb.gpizza.entity.Order;
import torn.ando.gpizzasb.gpizza.entity.OrderDish;
import torn.ando.gpizzasb.gpizza.entity.OrderDishStatus;
import torn.ando.gpizzasb.gpizza.entityRest.DishRest;
import torn.ando.gpizzasb.gpizza.entityRest.OrderDishStatusRest;
import torn.ando.gpizzasb.gpizza.entityRest.OrderRest;
import torn.ando.gpizzasb.gpizza.mapper.RestMapper;
import torn.ando.gpizzasb.gpizza.service.OrderDishService;
import torn.ando.gpizzasb.gpizza.service.OrderDishStatusService;
import torn.ando.gpizzasb.gpizza.service.OrderService;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("orders")
public class OrderController {
    private RestMapper restMapper;
    private OrderService orderService;
    private OrderDishService orderDishService;
    private OrderDishStatusService orderDishStatusService;

    @PutMapping
    public ResponseEntity<List<Order>> updateOrder(@RequestBody List<OrderRest> order) {
        List<Order> orderList = new ArrayList<>();
        order.forEach(orderRest -> {
            orderList.add(restMapper.mapToOrder(orderRest));
        });

        return ResponseEntity.ok(orderService.createOrder(orderList));

    }

    @GetMapping("{reference}")
    public ResponseEntity<Order> findByReference(@PathVariable String reference){
        Order order = orderService.findByReference(reference);
        if(order == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //orderService.changeOrderStatus(reference);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("{reference}/dishes")
    public ResponseEntity<Order> updateOrder(@PathVariable String reference, @RequestBody List<DishRest> dishRestList){
        Order order = orderService.findByReference(reference);
        if(order == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<OrderDish> orderDishList = dishRestList
                .stream()
                .map(dishRest -> {
                    OrderDish orderDish = restMapper.mapToOrderDish(dishRest);
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
        Order order = orderService.findByReference(reference);
        return ResponseEntity.ok(order);
    }
}
