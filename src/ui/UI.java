package ui;

import dish.Dish;
import lombok.AllArgsConstructor;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class UI {
    Map<Dish,Integer> orderList;

    public UI(){
        this.orderList = new HashMap<>();
    }

    public void orderDish(){
        System.out.println("What dish you order ? ");
        System.out.println("------------------");
        System.out.println(" 1 - Hot Dog");
        System.out.println(" 2 - Vary sy Ravitoto");
        System.out.println(" 3 - Burger");
        System.out.println(" 4 - Sandwich");
        System.out.println(" 5 - Exit");
        System.out.println("------------------");
        System.out.println("\n");
        try{
            System.out.println("your order? ");
            Scanner inputStream = new Scanner(System.in);
            String dish = inputStream.nextLine();
            System.out.println("how many?");
            Integer dishNumber = inputStream.nextInt();
            Dish dish1 = new Dish();
            while(dish != "5"){
                System.out.println("your order? ");
                dish = inputStream.nextLine();
                System.out.println("how many?");
                dishNumber = inputStream.nextInt();
                dish1 = new Dish();
                switch (dish){
                    case "1":
                        dish1.setName("Hot dog");
                        break;
                    case "2":
                        dish1.setName("vary sy ravitoto");
                        break;
                    case "3":
                        dish1.setName("burger");
                        break;
                    case "4":
                        dish1.setName("sandwich");
                        break;
                    default:
                        System.out.println("what are you doing here?");
                        dish = "5";
                }
            }
            orderList.put(dish1,dishNumber);
            inputStream.close();
        }catch (RuntimeException e){
            System.out.println(e);
        }
        orderList.entrySet().stream().map(order->{
            System.out.println("you order");
            System.out.println(order.getValue() +" "+ order.getKey());
            return null;
        });
    }
}
