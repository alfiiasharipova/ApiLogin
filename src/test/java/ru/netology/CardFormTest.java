package ru.netology;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.DataGenerator.Registration.generate;

public class CardFormTest {
    private Faker faker;

    private void fillForm(RegistrationByInfo user){
        $("[name=login]").setValue(user.getLogin());
        $("[name=password]").setValue(user.getPassword());
        $(byText("Продолжить")).click();
    }

    @BeforeEach
    void serUp() {
        open("http://localhost:9999");
        faker = new Faker(new Locale("en"));
    }

    @Test
    void shouldSignInUserExist() {
        RegistrationByInfo user = generate();
        ApiHelper.signUp(user);
        fillForm(user);
        $(byText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldNotSignInUserDoesNotExist() {
        RegistrationByInfo user = generate();
        fillForm(user);
        $(".notification__content").shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotSignInBlockedUser() {
        RegistrationByInfo user = generate();
        user.setStatus("blocked");
        ApiHelper.signUp(user);
        fillForm(user);
        $(".notification__content").shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    void shouldNotSignInWrongLogin() {
        RegistrationByInfo user = generate();
        ApiHelper.signUp(user);
        user.setLogin(faker.name().username());
        fillForm(user);
        $(".notification__content").shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotSignInWrongPassword() {
        RegistrationByInfo user = generate();
        ApiHelper.signUp(user);
        user.setPassword(faker.internet().password());
        fillForm(user);
        $(".notification__content").shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldSignInUserCanUpdatePassword() {
        RegistrationByInfo user = generate();
        ApiHelper.signUp(user);
        RegistrationByInfo user2 = new RegistrationByInfo(
                user.getLogin(),
                faker.internet().password(),
                "active");
        ApiHelper.signUp(user2);
        fillForm(user);
        $(".notification__content").shouldHave(text("Неверно указан логин или пароль"));
        refresh();
        fillForm(user2);
        $(byText("Личный кабинет")).shouldBe(visible);

    }
}
