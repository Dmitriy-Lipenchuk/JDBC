package ru.gamesphere;

import ru.gamesphere.util.FlywayInitializer;
import ru.gamesphere.util.ReportGenerator;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {

    public static void main(String[] args) {
        FlywayInitializer.initDb();

        System.out.println("Первые 10 компаний по количеству поставленного товара:");
        ReportGenerator.getFirstTenOrganisationsByProductQuantity();
        System.out.println();

        System.out.println("Поставщики с количеством поставленного товара выше заданного:");
        ReportGenerator.getOrganisationsByProductQuantity(66);
        System.out.println();

        System.out.println("Количество и цена товара подневно за заданный период и итоги:");
        ReportGenerator.getQuantityAndSumOfProductsByPeriodForEachDay(
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 3).getTimeInMillis())
        );
        System.out.println();

        System.out.println("Средняя цена за период:");
        ReportGenerator.getAveragePriceByPeriod(
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 3).getTimeInMillis())
        );
        System.out.println();

        System.out.println("Список товаров за период:");
        ReportGenerator.getCompanyProductListByPeriod(
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 3).getTimeInMillis())
        );
        System.out.println();
    }
}