package ru.netology;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {}

    public static UserInfo createValidUserInfo () {
        Faker faker = new Faker(new Locale("ru"));
        return new UserInfo(
                getAdministrativeCenter(),
                faker.name().fullName(),
                faker.phoneNumber().phoneNumber()
        );
    }

    public static UserInfo createUserInvalidPhone () {
        Faker faker = new Faker(new Locale("ru"));
        return new UserInfo(
                getAdministrativeCenter(),
                faker.name().fullName(),
                getInvalidPhoneNumber()
        );
    }

    public static String getAdministrativeCenter() {
        List<String> administrativeCenters = new ArrayList<>();
        administrativeCenters.add("Москва");
        administrativeCenters.add("Волгоград");
        administrativeCenters.add("Санкт-Петербург");
        administrativeCenters.add("Ростов-на-Дону");
        administrativeCenters.add("Краснодар");
        administrativeCenters.add("Владивосток");
        administrativeCenters.add("Новосибирск");

        Random random = new Random();
        int index = random.nextInt(7);

        return administrativeCenters.get(index);
    }

    public static String getInvalidPhoneNumber() {
        List<String> invalidPhones = new ArrayList<>();
        invalidPhones.add("+19025668899");
        invalidPhones.add("+790256688");

        Random random = new Random();
        int index = random.nextInt(2);

        return invalidPhones.get(index);
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
