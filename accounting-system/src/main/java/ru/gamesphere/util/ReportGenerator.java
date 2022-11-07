package ru.gamesphere.util;import org.jetbrains.annotations.NotNull;import java.sql.*;public class ReportGenerator {    private static final String GET_ORGANISATION_PRODUCT_QUANTITY_SORTED_BY_QUANTITY_SQL = """            SELECT organisations.name AS org_name,                   SUM(A.quantity)    AS quantity            FROM (SELECT invoices.organisation_id   AS org_id,                         invoice_positions.quantity AS quantity                  FROM invoice_positions                           INNER JOIN invoices ON invoice_positions.invoice_id = invoices.id) AS A                     INNER JOIN organisations ON A.org_id = organisations.id            GROUP BY org_name            ORDER BY quantity DESC            LIMIT 10            """;    private static final String GET_GET_ORGANISATION_PRODUCT_QUANTITY_SQL_BY_THRESHOLD = """            SELECT organisations.name AS org_name,                    SUM(A.quantity)    AS quantity            FROM (SELECT invoices.organisation_id   AS org_id,                          invoice_positions.quantity AS quantity            FROM invoice_positions                            INNER JOIN invoices ON invoice_positions.invoice_id = invoices.id) AS A                      INNER JOIN organisations ON A.org_id = organisations.id            GROUP BY org_name            ORDER BY quantity DESC            """;    private static final String GET_PRICE_AND_QUANTITY_BY_PERIOD_SQL = """            SELECT invoices.date   AS date,                   A.name          AS product_name,                   SUM(A.price)    AS sum_price,                   SUM(A.quantity) AS sum_quantity            FROM (SELECT products.name                AS name,                         invoice_positions.price      AS price,                         invoice_positions.quantity   AS quantity,                         invoice_positions.invoice_id AS invoice_id                  FROM invoice_positions                           INNER JOIN products ON invoice_positions.product_id = products.id) AS A                     INNER JOIN invoices ON invoices.id = A.invoice_id            WHERE date BETWEEN ? AND ?            GROUP BY invoices.date, name            ORDER BY invoices.date            """;    private static final String GET_AVERAGE_PRICE_BY_PERIOD_SQL = """            SELECT A.name          AS product_name,                   AVG(A.price)    AS average_price            FROM (SELECT products.name                AS name,                         invoice_positions.price      AS price,                         invoice_positions.invoice_id AS invoice_id                  FROM invoice_positions                           INNER JOIN products ON invoice_positions.product_id = products.id)                     AS A                     INNER JOIN invoices ON invoices.id = A.invoice_id            WHERE date BETWEEN ? AND ?            GROUP BY name            ORDER BY name            """;    private static final String GET_COMPANY_PRODUCT_LIST_BY_PERIOD_SQL = """            SELECT B.org_name,                   products.name AS product_name\s            FROM (SELECT organisations.name AS org_name,                         A.id               AS invoice_id                  FROM (SELECT id,                               organisation_id                        FROM invoices                        WHERE date BETWEEN ? AND ?) AS A                           RIGHT JOIN organisations ON a.organisation_id = organisations.id) AS B                     LEFT JOIN invoice_positions ON B.invoice_id = invoice_positions.invoice_id                     LEFT JOIN products ON product_id = products.id            GROUP BY B.org_name,product_name            ORDER BY org_name            """;    private ReportGenerator() {    }    public static void getFirstTenOrganisationsByProductQuantity() {        try (Connection connection = ConnectionManager.open();             PreparedStatement statement = connection.prepareStatement(GET_ORGANISATION_PRODUCT_QUANTITY_SORTED_BY_QUANTITY_SQL);             ResultSet set = statement.executeQuery()) {            while (set.next()) {                System.out.println(set.getString("org_name") + " " + set.getInt("quantity"));            }        } catch (SQLException e) {            throw new RuntimeException(e);        }    }    public static void getOrganisationsByProductQuantity(int threshold) {        try (Connection connection = ConnectionManager.open();             PreparedStatement statement = connection.prepareStatement(GET_GET_ORGANISATION_PRODUCT_QUANTITY_SQL_BY_THRESHOLD);             ResultSet set = statement.executeQuery()) {            while (set.next()) {                int quantity = set.getInt("quantity");                if (quantity > threshold) {                    System.out.println(set.getString("org_name") + " " + quantity);                }            }        } catch (SQLException e) {            throw new RuntimeException(e);        }    }    public static void getQuantityAndSumOfProductsByPeriodForEachDay(@NotNull Timestamp start, @NotNull Timestamp end) {        int totalSum = 0;        int totalQuantity = 0;        try (Connection connection = ConnectionManager.open();             PreparedStatement statement = connection.prepareStatement(GET_PRICE_AND_QUANTITY_BY_PERIOD_SQL)) {            statement.setTimestamp(1, start);            statement.setTimestamp(2, end);            ResultSet set = statement.executeQuery();            while (set.next()) {                int currentSum = set.getInt("sum_price");                int currentQuantity = set.getInt("sum_quantity");                totalSum += currentSum;                totalQuantity += currentQuantity;                System.out.println(set.getTimestamp("date") + "\t"                        + set.getString("product_name") + "\t"                        + currentSum + " "                        + currentQuantity);            }        } catch (SQLException e) {            throw new RuntimeException(e);        }        System.out.println("Total: " + totalSum + " " + totalQuantity);    }    public static void getAveragePriceByPeriod(@NotNull Timestamp start, @NotNull Timestamp end) {        try (Connection connection = ConnectionManager.open();             PreparedStatement statement = connection.prepareStatement(GET_AVERAGE_PRICE_BY_PERIOD_SQL)) {            statement.setTimestamp(1, start);            statement.setTimestamp(2, end);            ResultSet set = statement.executeQuery();            while (set.next()) {                System.out.println(set.getString("product_name") + "\t"                        + set.getInt("average_price"));            }        } catch (SQLException e) {            throw new RuntimeException(e);        }    }    public static void getCompanyProductListByPeriod(@NotNull Timestamp start, @NotNull Timestamp end) {        try (Connection connection = ConnectionManager.open();             PreparedStatement statement = connection.prepareStatement(GET_COMPANY_PRODUCT_LIST_BY_PERIOD_SQL)) {            statement.setTimestamp(1, start);            statement.setTimestamp(2, end);            ResultSet set = statement.executeQuery();            while (set.next()) {                String productName = set.getString("product_name") == null ? "" : set.getString("product_name");                System.out.println(set.getString("org_name") + "\t"                        + productName);            }        } catch (SQLException e) {            throw new RuntimeException(e);        }    }}