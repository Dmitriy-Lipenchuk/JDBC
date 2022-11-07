package ru.gamesphere.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.gamesphere.model.Organisation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class OrganisationDaoTest {

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

    static Stream<Arguments> data() {
        return Stream.of(
                arguments(1, "Intel", 100100100, 200200200),
                arguments(2, "Nvidia", 300300300, 400400400),
                arguments(3, "Kingston", 500500500, 600600600)
        );
    }
}