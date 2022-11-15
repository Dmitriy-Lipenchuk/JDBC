package ru.gamesphere.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.gamesphere.AbstractTest;
import ru.gamesphere.model.InvoicePosition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class InvoicePositionDaoTest extends AbstractTest {

    InvoicePositionDao invoiceDao = new InvoicePositionDao();

    @ParameterizedTest
    @MethodSource("data")
    void get(int id, int productId, int invoiceId, int price, int quantity) {
        InvoicePosition invoicePosition = new InvoicePosition(id, productId, invoiceId, price, quantity);
        assertEquals(invoicePosition, invoiceDao.get(id));
    }

    @Test
    void all() {
        List<InvoicePosition> positions = data()
                .map(x -> new InvoicePosition((int) x.get()[0], (int) x.get()[1], (int) x.get()[2], (int) x.get()[3], (int) x.get()[4]))
                .collect(Collectors.toList());

        assertEquals(positions, invoiceDao.all());
    }

    @Test
    void save() {
        InvoicePosition position = new InvoicePosition(999, 2, 3, 1000, 1);
        invoiceDao.save(position);

        assertEquals(position, invoiceDao.get(999));
        invoiceDao.delete(position);
    }

    @Test
    void update() {
        InvoicePosition position = new InvoicePosition(999, 2, 3, 1000, 1);
        invoiceDao.save(position);

        position.setPrice(99999);
        invoiceDao.update(position);

        assertEquals(position, invoiceDao.get(999));
        invoiceDao.delete(position);
    }

    @Test
    void delete() {
        InvoicePosition position = new InvoicePosition(999, 2, 3, 1000, 1);
        invoiceDao.save(position);
        invoiceDao.delete(position);

        assertThrows(RuntimeException.class, () -> invoiceDao.get(999));
        invoiceDao.delete(position);
    }

    @Test
    void getByInvoiceId() {
        List<InvoicePosition> positions = new ArrayList<>();
        positions.add(invoiceDao.get(1));
        positions.add(invoiceDao.get(2));
        assertEquals(positions, invoiceDao.getByInvoiceId(1));
    }

    static Stream<Arguments> data() {
        return Stream.of(
                arguments(1, 1, 1, 500, 10),
                arguments(2, 1, 1, 2000, 1),
                arguments(3, 2, 2, 1000, 5),
                arguments(4, 1, 3, 400, 50),
                arguments(5, 2, 4, 800, 10),
                arguments(6, 2, 5, 1000, 5),
                arguments(7, 2, 5, 3000, 1),
                arguments(8, 3, 6, 99, 56),
                arguments(9, 3, 7, 100, 10),
                arguments(10, 3, 8, 110, 5),
                arguments(11, 3, 9, 80, 50),
                arguments(12, 3, 9, 110, 5)
        );
    }
}