package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CardDeliveryTest {
    private String baseUrl = "http://localhost:9999/";

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Отображается нотификация 'Успешно', если все поля заполнены валидными значениями")
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
    @DisplayName("Отображается нотификация 'Перепланировать', если была изменена только дата")
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
    @DisplayName("Не отображается нотификация 'Перепланировать', если данные не были изменены")
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
    @DisplayName("Не отображается нотификация 'Успешно', если номер телефона невалидный")
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
