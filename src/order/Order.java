package order;

import dish.Dish;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
    private double id;
    private Map<Dish, Integer> dishesOrder;
    private LocalDateTime dateOfOrder;
    private OrderStatus orderStatus;

    private boolean verify(){
        List<Boolean> statu = new ArrayList<>();
        dishesOrder.entrySet().stream().forEach(dish->{
            if(dish.getKey().getAvailableQuantity()<=dish.getValue()){
                List<String> errorMessage = new ArrayList<>();
                dish.getKey().getIngredients().stream().forEach(ingredientDish -> {
                    double neededQuantity = ingredientDish.getRequiredQuantity()*dish.getValue()-ingredientDish.getIngredients().getAvailableQuantity();
                    if(neededQuantity>0){
                        String s = (ingredientDish.getIngredients().getName() + " required : "+ dish.getValue());
                        s += (" | in stock "+ingredientDish.getIngredients().getAvailableQuantity());
                        s += (" ");
                        s += ("you need " + ingredientDish.getIngredients().getName() + " : " + neededQuantity);
                        errorMessage.add(s);
                        statu.add(false);
                    }else{
                        dish.getKey().setStatus(OrderStatus.CONFIRMED);
                    }
                });
                if (!errorMessage.isEmpty()){throw new RuntimeException(errorMessage.toString());}
            }else{
                dish.getKey().setStatus(OrderStatus.CONFIRMED);
            }
        });
        return statu.contains(false) != true;
    }

    public void makeOrder(){
        if(verify()){
            setOrderStatus(OrderStatus.CREATE);
            System.out.println("Order is confirmed");
            setOrderStatus(OrderStatus.CONFIRMED);
        }else{
            dishesOrder.entrySet().forEach(dish->{
                if(dish.getKey().getStatus() != OrderStatus.CONFIRMED){
                    System.out.println(dish.getKey().getName() + " not enough stock for now");
                }
            });
        }
    }

    public void setOrderStatus(OrderStatus orderStatus){
        if(orderStatus == OrderStatus.CONFIRMED && this.orderStatus != OrderStatus.CREATE){
            throw new IllegalStateException("Can't jump to confirmed order");
        }
        if(orderStatus == OrderStatus.IN_PREPARATION && this.orderStatus != OrderStatus.CONFIRMED){
            throw new IllegalStateException("Can't jump to confirmed order");
        }
        if(orderStatus == OrderStatus.DONE && this.orderStatus != OrderStatus.IN_PREPARATION){
            throw new IllegalStateException("Can't jump to confirmed order");
        }
        if(orderStatus == OrderStatus.SERVED && this.orderStatus != OrderStatus.DONE){
            throw new IllegalStateException("Can't jump to confirmed order");
        }
        this.orderStatus = orderStatus;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Order order = (Order) object;
        return Double.compare(id, order.id) == 0 && Objects.equals(dishesOrder, order.dishesOrder) && Objects.equals(dateOfOrder, order.dateOfOrder) && orderStatus == order.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dishesOrder, dateOfOrder, orderStatus);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", dishesOrder=" + dishesOrder +
                ", dateOfOrder=" + dateOfOrder +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
