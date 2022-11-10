package ru.gamesphere.dao;

import org.jetbrains.annotations.NotNull;
import ru.gamesphere.model.InvoicePosition;
import ru.gamesphere.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoicePositionDao implements Dao<InvoicePosition> {

    private final static String GET_SQL = """
            SELECT *
            FROM invoice_positions
            WHERE id = ?
            """;

    private final static String ALL_SQL = """
            SELECT *
            FROM invoice_positions
            """;

    private final static String SAVE_SQL = """
            INSERT INTO invoice_positions (id, product_id, invoice_id, price, quantity)
            VALUES (?, ?, ?, ?, ?)
            """;

    private final static String UPDATE_SQL = """
            UPDATE invoice_positions
            SET product_id = ?, invoice_id = ?, price = ?, quantity = ?
            where id = ?
            """;

    private final static String DELETE_SQL = """
            DELETE FROM invoice_positions
            where id = ?
            """;

    private final static String GET_BY_INVOICE_ID_SQL = """
            SELECT *
            FROM invoice_positions
            WHERE invoice_id = ?
            """;

    @Override
    public @NotNull InvoicePosition get(int id) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(GET_SQL)) {
            statement.setInt(1, id);

            ResultSet set = statement.executeQuery();

            if (set.next()) {
                return new InvoicePosition(set.getInt("id"),
                        set.getInt("product_id"),
                        set.getInt("invoice_id"),
                        set.getInt("price"),
                        set.getInt("quantity"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        throw new IllegalStateException("Record with id " + id + " not found");
    }

    @Override
    public @NotNull List<@NotNull InvoicePosition> all() {
        List<InvoicePosition> invoicePositions = new ArrayList<>();

        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(ALL_SQL);
             ResultSet set = statement.executeQuery()) {

            while (set.next()) {
                invoicePositions.add(new InvoicePosition(set.getInt("id"),
                        set.getInt("product_id"),
                        set.getInt("invoice_id"),
                        set.getInt("price"),
                        set.getInt("quantity")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return invoicePositions;
    }

    @Override
    public void save(@NotNull InvoicePosition entity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL)) {
            statement.setInt(1, entity.getId());
            statement.setInt(2, entity.getProductId());
            statement.setInt(3, entity.getInvoiceId());
            statement.setInt(4, entity.getPrice());
            statement.setInt(5, entity.getQuantity());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NotNull InvoicePosition entity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setInt(1, entity.getProductId());
            statement.setInt(2, entity.getInvoiceId());
            statement.setInt(3, entity.getPrice());
            statement.setInt(4, entity.getQuantity());
            statement.setInt(5, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NotNull InvoicePosition entity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<InvoicePosition> getByInvoiceId(int invoiceId) {
        List<InvoicePosition> invoicePositions = new ArrayList<>();

        try (Connection connection = ConnectionManager.open();
             PreparedStatement invoiceStatement = connection.prepareStatement(GET_BY_INVOICE_ID_SQL)) {
            invoiceStatement.setInt(1, invoiceId);

            ResultSet set = invoiceStatement.executeQuery();

            while (set.next()) {
                invoicePositions.add(new InvoicePosition(set.getInt("id"),
                        set.getInt("product_id"),
                        set.getInt("invoice_id"),
                        set.getInt("price"),
                        set.getInt("quantity")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return invoicePositions;
    }
}
