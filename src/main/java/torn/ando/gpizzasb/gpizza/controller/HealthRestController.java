package torn.ando.gpizzasb.gpizza.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/ping")
public class HealthRestController{

    @GetMapping("/pong")
    public String ping(){
        return "ping";
    }

    @GetMapping("/ping")
    public String pong(){
        return "pong";
    }

}
