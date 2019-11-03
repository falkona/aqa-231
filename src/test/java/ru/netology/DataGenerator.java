package ru.netology;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;

public class DataGenerator {
    private DataGenerator() {}

    public static UserInfo createValidUserInfo () {
        Faker faker = new Faker(new Locale("ru"));
        return new UserInfo(
                faker.address().city(),
                faker.name().fullName(),
                faker.phoneNumber().phoneNumber()
        );
    }

    public static String getAdministrativeCenter() {
        return "";
    }

    public static int getMinimumDaysToDelivery() {
        return 3;
    }

    public static LocalDate getEarliestValidDate() {
        return LocalDate.now().plusDays(getMinimumDaysToDelivery());
    }

    public static long getTimeStampString(LocalDate date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return date.atStartOfDay(zoneId).toEpochSecond();
    }
}
