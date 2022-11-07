package ru.gamesphere.dao;

import org.jetbrains.annotations.NotNull;
import ru.gamesphere.model.Invoice;
import ru.gamesphere.model.InvoicePosition;
import ru.gamesphere.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDao implements Dao<Invoice> {

    @NotNull
    private final InvoicePositionDao invoicePositionDao;

    private final static String GET_SQL = """
            SELECT *
            FROM invoices
            WHERE id = ?
            """;

    private final static String ALL_SQL = """
            SELECT *
            FROM invoices
            """;

    private final static String SAVE_SQL = """
            INSERT INTO invoices (id, date, organisation_id)
            VALUES (?, ?, ?)
            """;

    private final static String UPDATE_SQL = """
            UPDATE invoices
            SET date = ?, organisation_id =?
            WHERE id = ?
            """;

    private final static String DELETE_SQL = """
            DELETE FROM invoices
            WHERE id = ?
            """;

    public InvoiceDao(@NotNull InvoicePositionDao invoicePositionDao) {
        this.invoicePositionDao = invoicePositionDao;
    }

    @Override
    public @NotNull Invoice get(int id) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement invoiceStatement = connection.prepareStatement(GET_SQL)) {
            invoiceStatement.setInt(1, id);

            List<InvoicePosition> invoicePositions = invoicePositionDao.getByInvoiceId(id);
            ResultSet set = invoiceStatement.executeQuery();

            if (set.next()) {
                return new Invoice(set.getInt("id"),
                        set.getTimestamp("date"),
                        set.getInt("organisation_id"),
                        invoicePositions);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        throw new IllegalStateException("Record with id " + id + " not found");
    }

    @Override
    public @NotNull List<@NotNull Invoice> all() {
        List<Invoice> invoices = new ArrayList<>();

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(ALL_SQL);
             ResultSet set = preparedStatement.executeQuery()) {
            while (set.next()) {
                int invoiceId = set.getInt("id");
                List<InvoicePosition> invoicePositions = invoicePositionDao.getByInvoiceId(invoiceId);

                invoices.add(new Invoice(invoiceId,
                        set.getTimestamp("date"),
                        set.getInt("organisation_id"),
                        invoicePositions));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return invoices;
    }

    @Override
    public void save(@NotNull Invoice entity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL)) {
            statement.setInt(1, entity.getId());
            statement.setTimestamp(2, entity.getDate());
            statement.setInt(3, entity.getOrganisationId());

            statement.executeUpdate();

            for (InvoicePosition position : entity.getInvoicePositions()) {
                invoicePositionDao.save(position);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NotNull Invoice entity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setTimestamp(1, entity.getDate());
            statement.setInt(2, entity.getOrganisationId());
            statement.setInt(3, entity.getId());

            for (InvoicePosition position : entity.getInvoicePositions()) {
                invoicePositionDao.update(position);
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NotNull Invoice entity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
