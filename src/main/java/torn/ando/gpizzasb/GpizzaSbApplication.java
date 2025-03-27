package torn.ando.gpizzasb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = {"gpizza"})
public class GpizzaSbApplication {

	public static void main(String[] args) {
		SpringApplication.run(GpizzaSbApplication.class, args);
	}

}
