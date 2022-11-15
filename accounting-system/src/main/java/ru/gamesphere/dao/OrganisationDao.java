package ru.gamesphere.dao;

import org.jetbrains.annotations.NotNull;
import ru.gamesphere.model.Organisation;
import ru.gamesphere.model.Product;
import ru.gamesphere.util.ConnectionManager;

import java.sql.*;
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
            where id = ?
            """;

    private final static String DELETE_SQL = """
            DELETE FROM organisations
            where id = ?
            """;

    private static final String GET_ORGANISATION_PRODUCT_QUANTITY_SORTED_BY_QUANTITY_SQL = """
            SELECT organisations.name           AS org_name,
                   COALESCE(SUM(A.quantity), 0) AS quantity
            FROM (SELECT invoices.organisation_id   AS org_id,
                         invoice_positions.quantity AS quantity
                  FROM invoice_positions
                           INNER JOIN invoices ON invoice_positions.invoice_id = invoices.id) AS A
                     RIGHT JOIN organisations ON A.org_id = organisations.id
            GROUP BY org_name
            ORDER BY quantity DESC
            LIMIT 10
            """;

    private static final String GET_COMPANY_PRODUCT_LIST_BY_PERIOD_SQL = """
            SELECT B.org_name,
                COALESCE(products.name,'')  AS product_name
            FROM (SELECT organisations.name AS org_name,
                         A.id               AS invoice_id
                  FROM (SELECT id,
                               organisation_id
                        FROM invoices
                        WHERE date BETWEEN ? AND ?) AS A
                           RIGHT JOIN organisations ON a.organisation_id = organisations.id) AS B
                     LEFT JOIN invoice_positions ON B.invoice_id = invoice_positions.invoice_id
                     LEFT JOIN products ON product_id = products.id
            GROUP BY B.org_name,product_name
            ORDER BY org_name
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

    public static void getFirstTenOrganisationsByProductQuantity() {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(GET_ORGANISATION_PRODUCT_QUANTITY_SORTED_BY_QUANTITY_SQL);
             ResultSet set = statement.executeQuery()) {
            while (set.next()) {
                System.out.println(set.getString("org_name") + " " + set.getInt("quantity"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getOrganisationsByProductQuantity(List<Product> products, List<Integer> thresholds) {
        if (products.size() != thresholds.size()) {
            throw new IllegalStateException("For each product thresholds must be specified");
        }

        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(
                     generateGetOrganisationsByProductQuantitySql(products.size())
             )) {
            for (int i = 0, j = 1; i < products.size(); i++, j += 2) {
                statement.setInt(j, products.get(i).getId());
                statement.setInt(j + 1, thresholds.get(i));
            }

            ResultSet set = statement.executeQuery();

            while (set.next()) {
                int quantity = set.getInt("quantity");
                System.out.println(set.getString("org_name") + " " + quantity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getCompanyProductListByPeriod(@NotNull Timestamp start, @NotNull Timestamp end) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(GET_COMPANY_PRODUCT_LIST_BY_PERIOD_SQL)) {
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);

            ResultSet set = statement.executeQuery();
            while (set.next()) {
                System.out.println(set.getString("org_name") + "\t"
                        + set.getString("product_name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateGetOrganisationsByProductQuantitySql(int numberOfArguments) {
        String fistPart = """
                SELECT organisations.name AS org_name,
                       SUM(A.quantity)    AS quantity
                FROM (SELECT invoices.organisation_id        AS org_id,
                             SUM(invoice_positions.quantity) AS quantity
                      FROM invoice_positions
                               INNER JOIN invoices ON invoice_positions.invoice_id = invoices.id
                      GROUP BY invoice_positions.product_id, invoices.organisation_id
                      """;

        String secondPart = """
                          ) AS A
                         INNER JOIN organisations ON A.org_id = organisations.id
                          GROUP BY org_name
                          ORDER BY quantity DESC
                """;

        if (numberOfArguments == 0) {
            return fistPart + secondPart;
        }

        return fistPart +
                "HAVING (product_id = ? AND SUM(invoice_positions.quantity) > ?)\n" +
                "OR (product_id = ? AND SUM(invoice_positions.quantity) > ?)\n".repeat(numberOfArguments - 1) +
                secondPart;
    }
}
