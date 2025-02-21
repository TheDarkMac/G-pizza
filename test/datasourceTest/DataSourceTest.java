package datasourceTest;

import datasource.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class DataSourceTest {

    @Test
    void test_if_connection_is_ok_after_close(){
        DataSource dataSource = new DataSource();
        try {
            dataSource.getConnection().close();
            Assertions.assertTrue(!dataSource.getConnection().isClosed());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
