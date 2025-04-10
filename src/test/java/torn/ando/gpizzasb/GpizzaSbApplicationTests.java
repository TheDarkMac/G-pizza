package torn.ando.gpizzasb;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import torn.ando.gpizzasb.gpizza.entity.Order;
import torn.ando.gpizzasb.gpizza.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
class GpizzaSbApplicationTests {

	@Autowired
	private OrderService orderService;

	@Test
	void contextLoads() {
		Order order = new Order();
		order.setId(1);
		order.setReference("ffff");
		order.setOrderDate(LocalDateTime.now());
		orderService.createOrder(List.of(order));
		Order frombdd = orderService.findByReference("ffff");
		Assertions.assertEquals(order.getReference(), frombdd.getReference());
		//orderService.deleteOrderByReference("ffff");
		Order frombdd2 = orderService.findByReference("ffff");
		Assertions.assertNotNull(frombdd2);
	}

	@Test
	void findOrder() {
		Order order = orderService.findByReference("ffff");
		Assertions.assertEquals("ffff", order.getReference());
	}

}
