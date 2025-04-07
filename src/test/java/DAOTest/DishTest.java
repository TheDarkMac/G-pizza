package DAOTest;

import torn.ando.gpizzasb.gpizza.dao.OrderDishDAO;
import torn.ando.gpizzasb.gpizza.entity.OrderDish;
import org.junit.Test;

import java.util.List;

public class DishTest {

    private OrderDishDAO orderDishDAO;

    @Test
    public void test() {
        List<OrderDish> orderDish = orderDishDAO.findByOrderReference("ffff");
        orderDish.forEach(orderDish1 -> {
            System.out.println(orderDish1.getOrderStatusList());
        });
    }
}
