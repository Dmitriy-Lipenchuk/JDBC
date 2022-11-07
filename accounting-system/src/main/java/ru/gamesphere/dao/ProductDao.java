package ru.gamesphere.dao;

import org.jetbrains.annotations.NotNull;
import ru.gamesphere.model.Product;
import ru.gamesphere.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            WHERE id = ?
            """;

    private final static String DELETE_SQL = """
            DELETE FROM products
            WHERE id = ?
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
}
