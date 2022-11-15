package ru.gamesphere.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.gamesphere.AbstractTest;
import ru.gamesphere.model.Organisation;
import ru.gamesphere.model.Product;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class OrganisationDaoTest extends AbstractTest {

    OrganisationDao organisationDao = new OrganisationDao();

    @ParameterizedTest
    @MethodSource("data")
    void get(int id, String companyName, int inn, int bankAccount) {
        assertEquals(new Organisation(id, companyName, inn, bankAccount), organisationDao.get(id));
    }

    @Test
    void all() {
        List<Organisation> organisations = data()
                .map(x -> new Organisation((int) x.get()[0], (String) x.get()[1], (int) x.get()[2], (int) x.get()[3]))
                .collect(Collectors.toList());

        assertEquals(organisations, organisationDao.all());
    }

    static Stream<Arguments> data() {
        return Stream.of(
                arguments(1, "Intel", 100100100, 200200200),
                arguments(2, "Nvidia", 300300300, 400400400),
                arguments(3, "Kingston", 500500500, 600600600),
                arguments(4, "Mail", 1, 2),
                arguments(5, "Tinkoff", 3, 3),
                arguments(6, "Sber", 5, 6),
                arguments(7, "Yandex", 7, 8),
                arguments(8, "EPAM", 9, 10),
                arguments(9, "HeadHunter", 10, 11),
                arguments(10, "OK", 12, 13),
                arguments(11, "Alfa_bank", 14, 15)
        );
    }

    @Test
    void save() {
        Organisation organisation = new Organisation(999, "Mail.ru", 111222333, 444555666);
        organisationDao.save(organisation);

        assertEquals(organisation, organisationDao.get(999));
        organisationDao.delete(organisation);
    }

    @Test
    void update() {
        Organisation organisation = new Organisation(999, "Mail.ru", 111222333, 444555666);
        organisationDao.save(organisation);

        organisation.setInn(0);
        organisationDao.update(organisation);

        assertEquals(organisation, organisationDao.get(999));
        organisationDao.delete(organisation);
    }

    @Test
    void delete() {
        Organisation organisation = new Organisation(999, "Mail.ru", 111222333, 444555666);
        organisationDao.save(organisation);
        organisationDao.delete(organisation);

        assertThrows(RuntimeException.class, () -> organisationDao.get(999));
    }

    @Test
    void getFirstTenOrganisationsByProductQuantity() {
        OrganisationDao.getFirstTenOrganisationsByProductQuantity();
        String topTenOrganisations = "Nvidia72" +
                "Kingston" + "70" +
                "Intel" + "66" +
                "Yandex" + "0" +
                "EPAM" + "0" +
                "Alfa_bank" + "0" +
                "Tinkoff" + "0" +
                "HeadHunter" + "0" +
                "OK" + "0" +
                "Mail" + "0";

        assertEquals(topTenOrganisations, outputStream.toString().replaceAll("\\s+", ""));
    }

    @ParameterizedTest
    @MethodSource("dataForOrganisationsByProductQuantity")
    void getOrganisationsByProductQuantity(List<Product> products, List<Integer> thresholds, String toCompare) {
        OrganisationDao.getOrganisationsByProductQuantity(products, thresholds);

        assertEquals(toCompare, outputStream.toString().replaceAll("\\s+", ""));
    }

    static Stream<Arguments> dataForOrganisationsByProductQuantity() {
        return Stream.of(
                arguments(List.of(
                                new Product(1, "CPU"),
                                new Product(2, "GPU"),
                                new Product(3, "RAM")
                        ),
                        List.of(0, 0, 0),
                        "Nvidia72" + "Kingston70" + "Intel66"),
                arguments(List.of(
                                new Product(1, "CPU"),
                                new Product(2, "GPU"),
                                new Product(3, "RAM")
                        ),
                        List.of(10, 10, 10),
                        "Nvidia72" + "Kingston70" + "Intel61"),
                arguments(List.of(
                                new Product(1, "CPU"),
                                new Product(2, "GPU"),
                                new Product(3, "RAM")
                        ),
                        List.of(100, 100, 100),
                        ""),
                arguments(List.of(
                                new Product(1, "CPU"),
                                new Product(2, "GPU")
                        ),
                        List.of(0, 0),
                        "Intel66" + "Nvidia16"),
                arguments(List.of(
                                new Product(3, "RAM")
                        ),
                        List.of(0),
                        "Kingston70" + "Nvidia56"),
                arguments(List.of(
                                new Product(3, "RAM")
                        ),
                        List.of(69),
                        "Kingston70"),
                arguments(List.of(), List.of(), "Nvidia72" + "Kingston70" + "Intel66")
        );
    }

    @ParameterizedTest
    @MethodSource("dataCompanyProductListByPeriod")
    void getCompanyProductListByPeriod(Timestamp start, Timestamp end, String toCompare) {
        OrganisationDao.getCompanyProductListByPeriod(start, end);

        assertEquals(toCompare, outputStream.toString().replaceAll("\\s+", ""));
    }

    static Stream<Arguments> dataCompanyProductListByPeriod() {
        return Stream.of(
                arguments(new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                        "Alfa_bank" + "" +
                                "EPAM" + "" +
                                "HeadHunter" + "" +
                                "Intel" + "CPU" +
                                "Kingston" + "RAM" +
                                "Mail" + "" +
                                "Nvidia" + "GPU" +
                                "OK" + "" +
                                "Sber" + "" +
                                "Tinkoff" + "" +
                                "Yandex" + ""),
                arguments(new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 2).getTimeInMillis()),
                        "Alfa_bank" + "" +
                                "EPAM" + "" +
                                "HeadHunter" + "" +
                                "Intel" + "CPU" +
                                "Intel" + "GPU" +
                                "Kingston" + "RAM" +
                                "Mail" + "" +
                                "Nvidia" + "GPU" +
                                "OK" + "" +
                                "Sber" + "" +
                                "Tinkoff" + "" +
                                "Yandex" + ""),
                arguments(new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 3).getTimeInMillis()),
                        "Alfa_bank" + "" +
                                "EPAM" + "" +
                                "HeadHunter" + "" +
                                "Intel" + "CPU" +
                                "Intel" + "GPU" +
                                "Kingston" + "RAM" +
                                "Mail" + "" +
                                "Nvidia" + "GPU" +
                                "Nvidia" + "RAM" +
                                "OK" + "" +
                                "Sber" + "" +
                                "Tinkoff" + "" +
                                "Yandex" + ""),
                arguments(new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 10).getTimeInMillis()),
                        new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 20).getTimeInMillis()),
                        "Alfa_bank" + "" +
                                "EPAM" + "" +
                                "HeadHunter" + "" +
                                "Intel" + "" +
                                "Kingston" + "" +
                                "Mail" + "" +
                                "Nvidia" + "" +
                                "OK" + "" +
                                "Sber" + "" +
                                "Tinkoff" + "" +
                                "Yandex" + "")
        );
    }
}