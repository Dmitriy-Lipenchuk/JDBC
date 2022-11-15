package ru.gamesphere.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.gamesphere.AbstractTest;
import ru.gamesphere.model.Product;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ProductDaoTest extends AbstractTest {

    ProductDao productDao = new ProductDao();

    @ParameterizedTest
    @MethodSource("data")
    void get(int id, String productName) {
        assertEquals(new Product(id, productName), productDao.get(id));
    }

    @Test
    void all() {
        List<Product> products = data()
                .map(x -> new Product((int) x.get()[0], (String) x.get()[1]))
                .collect(Collectors.toList());

        assertEquals(products, productDao.all());
    }

    static Stream<Arguments> data() {
        return Stream.of(
                arguments(1, "CPU"),
                arguments(2, "GPU"),
                arguments(3, "RAM")
        );
    }

    @Test
    void save() {
        Product product = new Product(9898, "Test product");

        productDao.save(product);
        assertEquals(product, productDao.get(9898));
        productDao.delete(product);
    }

    @Test
    void update() {
        Product product = new Product(9898, "Test product");

        productDao.save(product);
        product.setName("Changed name");
        productDao.update(product);
        assertEquals(product, productDao.get(9898));
        productDao.delete(product);
    }

    @Test
    void delete() {
        Product product = new Product(9898, "Test product");

        productDao.save(product);
        assertEquals(product, productDao.get(9898));
        productDao.delete(product);
        assertThrows(RuntimeException.class, () -> productDao.get(9898));
    }

    @ParameterizedTest
    @MethodSource("dataForQuantityAndSumOfProductsByPeriodForEachDay")
    void getQuantityAndSumOfProductsByPeriodForEachDay(Timestamp start, Timestamp end, String toCompare) {
        ProductDao.getQuantityAndSumOfProductsByPeriodForEachDay(start, end);

        assertEquals(toCompare, outputStream.toString().replaceAll("\\s+", ""));
    }

    static Stream<Arguments> dataForQuantityAndSumOfProductsByPeriodForEachDay() {
        return Stream.of(
                arguments(new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                        "2022-10-01" + "00:00:00.0" + "CPU" + "2500" + "11" +
                                "2022-10-01" + "00:00:00.0" + "GPU" + "800" + "10" +
                                "2022-10-01" + "00:00:00.0" + "RAM" + "100" + "10" +
                                "Total:" + "3400" + "31"),
                arguments(new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 2).getTimeInMillis()),
                        "2022-10-01" + "00:00:00.0" + "CPU" + "2500" + "11" +
                                "2022-10-01" + "00:00:00.0" + "GPU" + "800" + "10" +
                                "2022-10-01" + "00:00:00.0" + "RAM" + "100" + "10" +
                                "2022-10-02" + "00:00:00.0" + "GPU" + "5000" + "11" +
                                "2022-10-02" + "00:00:00.0" + "RAM" + "110" + "5" +
                                "Total:" + "8510" + "47"),
                arguments(new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 3).getTimeInMillis()),
                        "2022-10-01" + "00:00:00.0" + "CPU" + "2500" + "11" +
                                "2022-10-01" + "00:00:00.0" + "GPU" + "800" + "10" +
                                "2022-10-01" + "00:00:00.0" + "RAM" + "100" + "10" +
                                "2022-10-02" + "00:00:00.0" + "GPU" + "5000" + "11" +
                                "2022-10-02" + "00:00:00.0" + "RAM" + "110" + "5" +
                                "2022-10-03" + "00:00:00.0" + "CPU" + "400" + "50" +
                                "2022-10-03" + "00:00:00.0" + "RAM" + "289" + "111" +
                                "Total:" + "9199" + "208"),
                arguments(new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 10).getTimeInMillis()),
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 20).getTimeInMillis()),
                        "Total:" + "0" + "0")

        );
    }

    @ParameterizedTest
    @MethodSource("dataForAveragePriceByPeriod")
    void getAveragePriceByPeriod(Timestamp start, Timestamp end, String toCompare) {
        ProductDao.getAveragePriceByPeriod(start, end);

        assertEquals(toCompare, outputStream.toString().replaceAll("\\s+", ""));
    }

    static Stream<Arguments> dataForAveragePriceByPeriod() {
        return Stream.of(
                arguments(new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                        "CPU" + "1250" +
                                "GPU" + "800" +
                                "RAM" + "100"),
                arguments(new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 2).getTimeInMillis()),
                        "CPU" + "1250" +
                                "GPU" + "1450" +
                                "RAM" + "105"),
                arguments(new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 3).getTimeInMillis()),
                        "CPU" + "966" +
                                "GPU" + "1450" +
                                "RAM" + "99"),
                arguments(new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 10).getTimeInMillis()),
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 20).getTimeInMillis()),
                        "")

        );
    }
}