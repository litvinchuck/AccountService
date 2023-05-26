package com.example.AccountService.test_utils;

public class TestConstants {

    public static class UserDetails {

        public static final String FIRST_NAME = "John";
        public static final String LAST_NAME = "Doe";
        public static final String EMAIL = "jdoe@acme.com";
        public static final String ANOTHER_EMAIL = "anotherjdoe@acme.com";
        public static final String WRONG_EMAIL = "wrong@man.com";
        public static final String PASSWORD = "secret_password";
        public static final String NEW_PASSWORD = "new_secret_password";
        public static final String NEW_PASSWORD_HASH = "$2a$12$IT3wzbFeWJJaHWNwcy9LQO4CdPjQn8Sf987IdL3M5Wu5ox/yFC4b2";

    }

    public static class Payrolls {
        public static final String CORRECT_PERIOD = "10-2002";
        public static final String WRONG_PERIOD = "2002-10";
    }
}
