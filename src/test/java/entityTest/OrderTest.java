package entityTest;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import torn.ando.gpizzasb.gpizza.entity.Order;
import torn.ando.gpizzasb.gpizza.entity.OrderStatus;
import torn.ando.gpizzasb.gpizza.enums.OrderStatusType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderTest {

    @Test
    public void orderTest(){


        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setDatetime(LocalDateTime.now());
        orderStatus.setReferenceOrder("reference");
        orderStatus.setOrderStatus(OrderStatusType.CREATED);


        Order order = new Order();
        order.setId(1);
        order.setReference("reference");
        order.setOrderDate(LocalDateTime.now());


        OrderStatus orderStatus1 = new OrderStatus();
        orderStatus1.setDatetime(LocalDateTime.now());
        orderStatus1.setReferenceOrder("reference");
        orderStatus1.setOrderStatus(OrderStatusType.CONFIRMED);
        order.getOrderStatusList().add(orderStatus1);
        order.getOrderStatusList().add(orderStatus);

        Assertions.assertEquals("CONFIRMED",order.getActualStatus().toString());
    }
}
