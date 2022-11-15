package ru.gamesphere.dao;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.gamesphere.AbstractTest;
import ru.gamesphere.model.Invoice;
import ru.gamesphere.model.InvoicePosition;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class InvoiceDaoTest extends AbstractTest {

    InvoiceDao invoiceDao = new InvoiceDao(new InvoicePositionDao());

    @ParameterizedTest
    @MethodSource("data")
    void get(int id, @NotNull Timestamp date, int organisationId, List<InvoicePosition> positions) {
        Invoice invoice = new Invoice(id, date, organisationId, positions);
        assertEquals(invoice, invoiceDao.get(id));
    }

    @Test
    void all() {
        List<Invoice> invoices = data()
                .map(x -> new Invoice((int) x.get()[0], (Timestamp) x.get()[1], (int) x.get()[2], (List<InvoicePosition>) x.get()[3]))
                .collect(Collectors.toList());

        assertEquals(invoices, invoiceDao.all());
    }

    @Test
    void save() {
        Invoice invoice = new Invoice(99,
                new Timestamp(new GregorianCalendar(2077, Calendar.NOVEMBER, 27).getTimeInMillis()),
                1,
                List.of(new InvoicePosition(99, 1, 99, 500, 10),
                        new InvoicePosition(100, 1, 99, 2000, 1))
        );

        invoiceDao.save(invoice);
        assertEquals(invoice, invoiceDao.get(99));
        invoiceDao.delete(invoice);
    }

    @Test
    void update() {
        Invoice invoice = new Invoice(99,
                new Timestamp(new GregorianCalendar(2077, Calendar.NOVEMBER, 27).getTimeInMillis()),
                1,
                List.of(new InvoicePosition(99, 1, 99, 500, 10),
                        new InvoicePosition(100, 1, 99, 2000, 1))
        );

        invoiceDao.save(invoice);
        invoice.setDate(new Timestamp(new GregorianCalendar(2000, Calendar.JULY, 1).getTimeInMillis()));
        invoice.setOrganisationId(3);
        invoiceDao.update(invoice);
        assertEquals(invoice, invoiceDao.get(99));
        invoiceDao.delete(invoice);
    }

    @Test
    void delete() {
        Invoice invoice = new Invoice(99,
                new Timestamp(new GregorianCalendar(2077, Calendar.NOVEMBER, 27).getTimeInMillis()),
                1,
                List.of(new InvoicePosition(99, 1, 99, 500, 10),
                        new InvoicePosition(100, 1, 99, 2000, 1))
        );

        invoiceDao.save(invoice);
        invoiceDao.delete(invoice);

        assertThrows(RuntimeException.class, () -> invoiceDao.get(99));
    }

    static Stream<Arguments> data() {
        return Stream.of(
                arguments(1,
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                        1,
                        List.of(new InvoicePosition(1, 1, 1, 500, 10),
                                new InvoicePosition(2, 1, 1, 2000, 1))
                ),
                arguments(2,
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 2).getTimeInMillis()),
                        1,
                        List.of(new InvoicePosition(3, 2, 2, 1000, 5))
                ),
                arguments(3,
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 3).getTimeInMillis()),
                        1,
                        List.of(new InvoicePosition(4, 1, 3, 400, 50))
                ),
                arguments(4,
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                        2,
                        List.of(new InvoicePosition(5, 2, 4, 800, 10))
                ),
                arguments(5,
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 2).getTimeInMillis()),
                        2,
                        List.of(new InvoicePosition(6, 2, 5, 1000, 5),
                                new InvoicePosition(7, 2, 5, 3000, 1)
                        )
                ),
                arguments(6,
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 3).getTimeInMillis()),
                        2,
                        List.of(new InvoicePosition(8, 3, 6, 99, 56))
                ),
                arguments(7,
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                        3,
                        List.of(new InvoicePosition(9, 3, 7, 100, 10))
                ),
                arguments(8,
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 2).getTimeInMillis()),
                        3,
                        List.of(new InvoicePosition(10, 3, 8, 110, 5))
                ),
                arguments(9,
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 3).getTimeInMillis()),
                        3,
                        List.of(new InvoicePosition(11, 3, 9, 80, 50),
                                new InvoicePosition(12, 3, 9, 110, 5))
                ),
                arguments(10,
                        new Timestamp(new GregorianCalendar(2023, Calendar.OCTOBER, 1).getTimeInMillis()),
                        4,
                        List.of()
                ),
                arguments(11,
                        new Timestamp(new GregorianCalendar(2023, Calendar.OCTOBER, 1).getTimeInMillis()),
                        5,
                        List.of()
                ),
                arguments(12,
                        new Timestamp(new GregorianCalendar(2023, Calendar.OCTOBER, 1).getTimeInMillis()),
                        6,
                        List.of()
                ),
                arguments(13,
                        new Timestamp(new GregorianCalendar(2023, Calendar.OCTOBER, 1).getTimeInMillis()),
                        7,
                        List.of()
                ),
                arguments(14,
                        new Timestamp(new GregorianCalendar(2023, Calendar.OCTOBER, 1).getTimeInMillis()),
                        8,
                        List.of()
                ),
                arguments(15,
                        new Timestamp(new GregorianCalendar(2023, Calendar.OCTOBER, 1).getTimeInMillis()),
                        9,
                        List.of()
                ),
                arguments(16,
                        new Timestamp(new GregorianCalendar(2023, Calendar.OCTOBER, 1).getTimeInMillis()),
                        10,
                        List.of()
                ),
                arguments(17,
                        new Timestamp(new GregorianCalendar(2023, Calendar.OCTOBER, 1).getTimeInMillis()),
                        11,
                        List.of()
                )
        );
    }
}