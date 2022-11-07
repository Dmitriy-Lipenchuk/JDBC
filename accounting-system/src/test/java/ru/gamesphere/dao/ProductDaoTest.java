package ru.gamesphere.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.gamesphere.model.Product;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ProductDaoTest {

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

    static Stream<Arguments> data() {
        return Stream.of(
                arguments(1, "CPU"),
                arguments(2, "GPU"),
                arguments(3, "RAM")
        );
    }

}