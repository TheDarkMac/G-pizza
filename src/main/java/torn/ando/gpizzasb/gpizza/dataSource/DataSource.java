package torn.ando.gpizzasb.gpizza.dataSource;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
public class DataSource implements Serializable {
    Dotenv dotenv = Dotenv.load();
    private Connection connection = null;
    private String system = dotenv.get("DB_SYSTEM");
    private String host = dotenv.get("DB_HOST");
    private int port = Integer.parseInt(dotenv.get("DB_PORT"));
    private String user = dotenv.get("DB_USER");
    private String password = dotenv.get("DB_PASSWORD");
    private String dbName = dotenv.get("DB_NAME");

    public Connection getConnection() {
        try{
            connection = DriverManager.getConnection(
                    "jdbc:"+system+"://" + host + ":" + port + "/" + dbName,
                    user,
                    password
            );
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return connection;
    }

}
