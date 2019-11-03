package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CardDeliveryTest {
    private String baseUrl = "http://localhost:9999/";

    @Test
    void shouldBeSuccessIfDataIsValid() {
        open(baseUrl);

        UserInfo validUser = DataGenerator.createValidUserInfo();

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=name] input").setValue(validUser.getFullName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$(".button").findBy(Condition.exactText("Запланировать")).click();
        $("[data-test-id=success-notification]").waitUntil(Condition.visible, 3000);
    }

    @Test
    void needReplanIfDateWasChanged() {
        open(baseUrl);

        UserInfo validUser = DataGenerator.createValidUserInfo();

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=name] input").setValue(validUser.getFullName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$(".button").findBy(Condition.exactText("Запланировать")).click();
        $("[data-test-id=success-notification]").waitUntil(Condition.visible, 3000);
        $("button .icon_name_calendar").click();
        $(".calendar__layout").waitUntil(Condition.visible, 2000);
        long timestamp = DataGenerator.getTimeStampString(DataGenerator.getEarliestValidDate().plusDays(1));
        $(String.format("[data-day='%d000']", timestamp)).click();
        $$(".button").findBy(Condition.exactText("Запланировать")).click();
        $("[data-test-id=replan-notification]").waitUntil(Condition.visible, 3000);
    }

    @Test
    void doNotNeedReplanIfDateWasNotChanged() {
        open(baseUrl);

        UserInfo validUser = DataGenerator.createValidUserInfo();

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=name] input").setValue(validUser.getFullName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$(".button").findBy(Condition.exactText("Запланировать")).click();
        $("[data-test-id=success-notification]").waitUntil(Condition.visible, 3000);
        $("button .icon_name_calendar").click();
        $(".calendar__layout").waitUntil(Condition.visible, 2000);
        long timestamp = DataGenerator.getTimeStampString(DataGenerator.getEarliestValidDate());
        $(String.format("[data-day='%d000']", timestamp)).click();
        $$(".button").findBy(Condition.exactText("Запланировать")).click();
        $("[data-test-id=replan-notification]").waitUntil(Condition.not(Condition.visible), 3000);
    }

    @Test
    void shouldBeErrorIfPhoneIsInvalid() {
        open(baseUrl);

        UserInfo validUser = DataGenerator.createUserInvalidPhone();

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=name] input").setValue(validUser.getFullName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$(".button").findBy(Condition.exactText("Запланировать")).click();
        $("[data-test-id=phone]").shouldHave(Condition.cssClass("input_invalid"));
        $("[data-test-id=success-notification]").waitUntil(Condition.not(Condition.visible), 3000);
    }
}
