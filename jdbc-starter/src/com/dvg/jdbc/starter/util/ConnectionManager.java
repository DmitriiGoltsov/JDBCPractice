package com.dvg.jdbc.starter.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {

    /* Нежелательный, но возможный технически вариант хранение логина, пароля и урла для Базы Данных.
    private static final String URL = "jdbc:postgresql://localhost:5432/flight_repository";
    private static final String DB_USERNAME = "postgres";
    private static final String PASSWORD = "9as6lkwfx";
    */

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";

    private ConnectionManager() {
    }

    /* Приведённый ниже код использовался в Джава до версии 1.8. Он позволял напрямую загружать необходимый Драйвер в
    память ДжВМ. Начиная с версии 1.8 и выше его использование является излишнем и не соответствет лучшим практикам.

    static {
        loadDriver();
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    */

    public static Connection openConnection() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.getProperty(URL_KEY),
                    PropertiesUtil.getProperty(USERNAME_KEY),
                    PropertiesUtil.getProperty(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
