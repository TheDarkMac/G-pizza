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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
    private double id;
    private Map<Dish, Integer> dishes;
    private LocalDateTime dateOfOrder;
    private OrderStatus orderStatus;

    private boolean verify(){
        List<Boolean> statu = new ArrayList<>();
        dishes.entrySet().stream().forEach(dish->{
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
                if (!errorMessage.isEmpty()){System.out.println(errorMessage.toString());}
            }else{
                dish.getKey().setStatus(OrderStatus.CONFIRMED);
            }
        });
        return statu.contains(false) != true;
    }

    public void makeOrder(){
        if(verify()){
            orderStatus = OrderStatus.CONFIRMED;
            System.out.println("Order is confirmed");
        }else{
            dishes.entrySet().forEach(dish->{
                if(dish.getKey().getStatus() != OrderStatus.CONFIRMED){
                    System.out.println(dish.getKey().getName() + " not enough stock for now");
                }
            });
        }
    }
}
