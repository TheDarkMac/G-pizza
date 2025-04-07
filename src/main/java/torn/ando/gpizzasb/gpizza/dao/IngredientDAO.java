package torn.ando.gpizzasb.gpizza.dao;

import lombok.AllArgsConstructor;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaINSERT;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaSELECT;
import torn.ando.gpizzasb.gpizza.criteria.CriteriaUPDATE;
import torn.ando.gpizzasb.gpizza.dataSource.DataSource;
import torn.ando.gpizzasb.gpizza.enums.Unit;
import torn.ando.gpizzasb.gpizza.entity.Ingredient;
import torn.ando.gpizzasb.gpizza.entity.IngredientPrice;
import torn.ando.gpizzasb.gpizza.entity.Stock;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class IngredientDAO implements DAOSchema{

    private final DataSource dataSource;
    private final IngredientPriceDAO ingredientPriceDAO;
    private final StockDAO stockDAO;

    @Override
    public <T> List<T> saveAll(List<T> object) {
        List<Ingredient> ingredientList = (List<Ingredient>) object;
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientList.forEach(ingredient -> {
            CriteriaINSERT criteriaINSERT = new CriteriaINSERT("ingredient");
            criteriaINSERT.insert("id_ingredient", "name")
                    .values("?", "?")
                    .onConflict("id_ingredient")
                    .returning("id_ingredient", "name");

            String query = criteriaINSERT.build();
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, ingredient.getId());
                preparedStatement.setString(2, ingredient.getName());

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    ingredients.add(mapFroResultset(resultSet));
                }
            } catch (RuntimeException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return (List<T>) ingredients;
    }

    @Override
    public <T> List<T> findAll() {
        List<Ingredient> ingredients = null;

        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("ingredient");
        criteriaSELECT.select(
                "ingredient.id_ingredient", "name", //about ingredient
                "available_quantity" //about available_quantity
        );
        criteriaSELECT.join("LEFT",
                "available_quantity",
                "ingredient.id_ingredient = available_quantity.id_ingredient");
        criteriaSELECT.orderBy("id_ingredient",true);
        String query = criteriaSELECT.build();
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            ingredients = new ArrayList<>();
            while (resultSet.next()) {
                Ingredient ingredient = mapFroResultset(resultSet);
                ingredients.add(ingredient);
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
        return (List<T>) ingredients;
    }

    @Override
    public <T> T findById(double id) {
        Ingredient ingredient = null;
        CriteriaSELECT criteriaSELECT = new CriteriaSELECT("ingredient");

        criteriaSELECT.select("ingredient.id_ingredient", "name", //about ingredient
                "available_quantity"
        );

        criteriaSELECT.join("LEFT",
                "available_quantity",
                "ingredient.id_ingredient = available_quantity.id_ingredient");
        criteriaSELECT.and("ingredient.id_ingredient");
        String query = criteriaSELECT.build();
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ingredient = mapFroResultset(resultSet);
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
        return (T) ingredient;
    }

    @Override
    public <T> T deleteById(double id) {
        return null;
    }

    @Override
    public <T> List<T> deleteAll(List<T> list) {
        return List.of();
    }

    @Override
    public <T> List<T> updateAll(List<T> object) {
        List<Ingredient> ingredientList = (List<Ingredient>) object;
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientList.forEach(ingredient -> {
            CriteriaUPDATE criteriaUPDATE = new CriteriaUPDATE("ingredient");
            criteriaUPDATE.set("name","?");
            criteriaUPDATE.set("unit","?");
            criteriaUPDATE.and("id_ingredient",ingredient.getId());

            String query = criteriaUPDATE.build();
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, ingredient.getName());
                preparedStatement.setDouble(3, ingredient.getId());
                preparedStatement.executeUpdate();

            } catch (RuntimeException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return (List<T>) ingredients;
    }

    public Ingredient mapFroResultset(ResultSet rs) throws SQLException {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(rs.getLong("id_ingredient"));
        ingredient.setName(rs.getString("name"));

        List<IngredientPrice> ingredientPriceList = ingredientPriceDAO.findByIdIngredient(rs.getLong("id_ingredient"));

        ingredient.setPrices(ingredientPriceList);

        List<Stock> stockList = stockDAO.findByIdIngredient(rs.getLong("id_ingredient"));
        ingredient.setStockList(stockList);

        // Vérification de l'existence de la colonne "available_quantity"
        try {
            if (rs.findColumn("available_quantity") != -1) {
                ingredient.setAvailableQuantity(rs.getDouble("available_quantity"));
            }
        } catch (SQLException e) {
            // Log or handle if the column doesn't exist
        }
        return ingredient;
    }

}
