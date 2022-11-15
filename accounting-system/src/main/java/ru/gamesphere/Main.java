package ru.gamesphere;

import ru.gamesphere.dao.OrganisationDao;
import ru.gamesphere.dao.ProductDao;
import ru.gamesphere.model.Product;
import ru.gamesphere.util.FlywayInitializer;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        FlywayInitializer.initDb();

        System.out.println("Первые 10 компаний по количеству поставленного товара:");
        OrganisationDao.getFirstTenOrganisationsByProductQuantity();
        System.out.println();

        System.out.println("Поставщики с количеством поставленного товара выше заданного:");
        OrganisationDao.getOrganisationsByProductQuantity(List.of(
                        new Product(1, "CPU"),
                        new Product(2, "GPU"),
                        new Product(3, "RAM")
                ),
                List.of(10, 10, 10));
        System.out.println();

        System.out.println("Количество и цена товара подневно за заданный период и итоги:");
        ProductDao.getQuantityAndSumOfProductsByPeriodForEachDay(
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 3).getTimeInMillis())
        );
        System.out.println();

        System.out.println("Средняя цена за период:");
        ProductDao.getAveragePriceByPeriod(
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 3).getTimeInMillis())
        );
        System.out.println();

        System.out.println("Список товаров за период:");
        OrganisationDao.getCompanyProductListByPeriod(
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 3).getTimeInMillis())
        );
        System.out.println();
    }
}