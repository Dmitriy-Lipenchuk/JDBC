package ru.gamesphere.dao;

import org.jetbrains.annotations.NotNull;
import ru.gamesphere.model.Organisation;
import ru.gamesphere.model.Product;
import ru.gamesphere.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrganisationDao implements Dao<Organisation> {
    private final static String GET_SQL = """
            SELECT *
            FROM organisations
            WHERE id = ?
            """;


    private final static String ALL_SQL = """
            SELECT *
            FROM organisations
            """;

    private final static String SAVE_SQL = """
            INSERT INTO organisations (id, name, inn, bank_account)
            VALUES (?, ?, ?, ?)
            """;

    private final static String UPDATE_SQL = """
            UPDATE organisations
            SET name = ?, inn =?, bank_account =?
            WHERE id = ?
            """;

    private final static String DELETE_SQL = """
            DELETE FROM organisations
            WHERE id = ?
            """;

    @Override
    public @NotNull Organisation get(int id) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(GET_SQL)) {
            statement.setInt(1, id);

            ResultSet set = statement.executeQuery();

            if (set.next()) {
                return new Organisation(set.getInt("id"),
                        set.getString("name"),
                        set.getInt("inn"),
                        set.getInt("bank_account"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        throw new IllegalStateException("Record with id " + id + " not found");
    }

    @Override
    public @NotNull List<@NotNull Organisation> all() {
        List<Organisation> organisations = new ArrayList<>();

        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(ALL_SQL);
             ResultSet set = statement.executeQuery()) {

            while (set.next()) {
                organisations.add(new Organisation(set.getInt("id"),
                        set.getString("name"),
                        set.getInt("inn"),
                        set.getInt("bank_account")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return organisations;
    }

    @Override
    public void save(@NotNull Organisation entity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL)) {
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getName());
            statement.setInt(3, entity.getInn());
            statement.setInt(4, entity.getBankAccount());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NotNull Organisation entity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getInn());
            statement.setInt(3, entity.getBankAccount());
            statement.setInt(4, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NotNull Organisation entity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
