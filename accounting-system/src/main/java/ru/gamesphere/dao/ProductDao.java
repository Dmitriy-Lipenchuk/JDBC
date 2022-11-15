package ru.gamesphere.dao;

import org.jetbrains.annotations.NotNull;
import ru.gamesphere.model.Product;
import ru.gamesphere.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao implements Dao<Product> {
    private final static String GET_SQL = """
            SELECT *
            FROM products
            WHERE id = ?
            """;

    private final static String ALL_SQL = """
            SELECT *
            FROM products
            """;

    private final static String SAVE_SQL = """
            INSERT INTO products (id, name)
            VALUES (?, ?)
            """;

    private final static String UPDATE_SQL = """
            UPDATE products
            SET name = ?
            where id = ?
            """;

    private final static String DELETE_SQL = """
            DELETE FROM products
            where id = ?
            """;

    private static final String GET_PRICE_AND_QUANTITY_BY_PERIOD_SQL = """
            SELECT invoices.date   AS date,
                   A.name          AS product_name,
                   SUM(A.price)    AS sum_price,
                   SUM(A.quantity) AS sum_quantity
            FROM (SELECT products.name                AS name,
                         invoice_positions.price      AS price,
                         invoice_positions.quantity   AS quantity,
                         invoice_positions.invoice_id AS invoice_id
                  FROM invoice_positions
                           INNER JOIN products ON invoice_positions.product_id = products.id) AS A
                     INNER JOIN invoices ON invoices.id = A.invoice_id
            WHERE date BETWEEN ? AND ?
            GROUP BY invoices.date, name
            ORDER BY invoices.date
            """;

    private static final String GET_AVERAGE_PRICE_BY_PERIOD_SQL = """
            SELECT A.name          AS product_name,
                   AVG(A.price)    AS average_price
            FROM (SELECT products.name                AS name,
                         invoice_positions.price      AS price,
                         invoice_positions.invoice_id AS invoice_id
                  FROM invoice_positions
                           INNER JOIN products ON invoice_positions.product_id = products.id)
                     AS A
                     INNER JOIN invoices ON invoices.id = A.invoice_id
            WHERE date BETWEEN ? AND ?
            GROUP BY name
            ORDER BY name
            """;

    @Override
    public @NotNull Product get(int id) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(GET_SQL)) {
            statement.setInt(1, id);

            ResultSet set = statement.executeQuery();

            if (set.next()) {
                return new Product(set.getInt("id"), set.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        throw new IllegalStateException("Record with id " + id + " not found");
    }

    @Override
    public @NotNull List<@NotNull Product> all() {
        List<Product> products = new ArrayList<>();

        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(ALL_SQL);
             ResultSet set = statement.executeQuery()) {
            while (set.next()) {
                products.add(new Product(set.getInt("id"), set.getString("name")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    @Override
    public void save(@NotNull Product entity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL)) {
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getName());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NotNull Product entity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NotNull Product entity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getQuantityAndSumOfProductsByPeriodForEachDay(@NotNull Timestamp start, @NotNull Timestamp end) {
        int totalSum = 0;
        int totalQuantity = 0;

        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(GET_PRICE_AND_QUANTITY_BY_PERIOD_SQL)) {
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);

            ResultSet set = statement.executeQuery();
            while (set.next()) {
                int currentSum = set.getInt("sum_price");
                int currentQuantity = set.getInt("sum_quantity");
                totalSum += currentSum;
                totalQuantity += currentQuantity;

                System.out.println(set.getTimestamp("date") + "\t"
                        + set.getString("product_name") + "\t"
                        + currentSum + " "
                        + currentQuantity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Total: " + totalSum + " " + totalQuantity);
    }

    public static void getAveragePriceByPeriod(@NotNull Timestamp start, @NotNull Timestamp end) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(GET_AVERAGE_PRICE_BY_PERIOD_SQL)) {
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);

            ResultSet set = statement.executeQuery();
            while (set.next()) {
                System.out.println(set.getString("product_name") + "\t"
                        + set.getInt("average_price"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
