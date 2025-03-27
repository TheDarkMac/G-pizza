package DAOTest;

import gpizza.dataSource.DataSource;
import gpizza.ingredient.IngredientDAO;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.Test;

public class IngredientDAOTest {

    @Test
    public void findSaucisse(){
        IngredientDAO ingredientDAO = new IngredientDAO();
        DataSource dataSource = new DataSource();
        System.out.println(dataSource.getConnection());
        ingredientDAO.findById(1);
    }
}
