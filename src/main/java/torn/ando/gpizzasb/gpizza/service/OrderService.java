package torn.ando.gpizzasb.gpizza.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import torn.ando.gpizzasb.gpizza.dao.OrderDao;
import torn.ando.gpizzasb.gpizza.entity.Order;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {

    private OrderDao orderDao;

    public Order findByReference(String reference){
        return orderDao.findByReference(reference);
    }

    public List<Order> createOrder(List<Order> orders){
        return orderDao.saveAll(orders);
    }
}
