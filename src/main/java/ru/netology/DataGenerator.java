package ru.netology;


import com.github.javafaker.Faker;

import java.util.Locale;

public class DataGenerator {
    private DataGenerator() {}

    public static class Registration {
        private Registration() {}

        public static RegistrationByInfo generate() {
            Faker faker = new Faker(new Locale("en"));
            return new RegistrationByInfo(
                    faker.name().username(),
                    faker.internet().password(),
                    "active");
        }
    }
}
