package ru.gamesphere;

import ru.gamesphere.util.FlywayInitializer;
import ru.gamesphere.util.ReportGenerator;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {

    public static void main(String[] args) {
        FlywayInitializer.initDb();

        System.out.println("������ 10 �������� �� ���������� ������������� ������:");
        ReportGenerator.getFirstTenOrganisationsByProductQuantity();
        System.out.println();

        System.out.println("���������� � ����������� ������������� ������ ���� ���������:");
        ReportGenerator.getOrganisationsByProductQuantity(66);
        System.out.println();

        System.out.println("���������� � ���� ������ �������� �� �������� ������ � �����:");
        ReportGenerator.getQuantityAndSumOfProductsByPeriodForEachDay(
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 3).getTimeInMillis())
        );
        System.out.println();

        System.out.println("������� ���� �� ������:");
        ReportGenerator.getAveragePriceByPeriod(
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 3).getTimeInMillis())
        );
        System.out.println();

        System.out.println("������ ������� �� ������:");
        ReportGenerator.getCompanyProductListByPeriod(
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 1).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2022, Calendar.OCTOBER, 3).getTimeInMillis())
        );
        System.out.println();
    }
}