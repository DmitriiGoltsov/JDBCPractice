package com.dvg.jdbc.starter;

import com.dvg.jdbc.starter.util.ConnectionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.*;

public class BlobRunner {

    public static void main(String[] args) throws SQLException, IOException {

        getImage();
    }

    private static void getImage() throws SQLException, IOException {

        String query = """
                SELECT image FROM aircraft
                WHERE id = ?
                """;

        try (Connection connection = ConnectionManager.openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, 1);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                byte[] imageBytes = resultSet.getBytes("image");
                Files.write
                        (Path.of(
                                "resources", "boing777_new.jpg"),
                                imageBytes,
                                StandardOpenOption.CREATE);
            }
        }
    }

    private static void saveImage() throws SQLException, IOException {

        String query = """
                UPDATE aircraft
                SET image = ?
                WHERE id = 1
                """;

        try (Connection connection = ConnectionManager.openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBytes(1, Files.readAllBytes(
                    Path.of("resources",
                            "Boeing_777.jpg")));

            preparedStatement.executeUpdate();
        }
    }

    /* Пример реализации Блоба. В Постгрес работать не будет по причине отсутствия соответствующей сущности.
    Но в других БД/СУБД сработает. Здесь не указано управление транзакциями, но посмотреть его можно в БатчРаннере

    private static void saveImage() throws SQLException, IOException {

        String query = """
                UPDATE aircraft
                SET image = ? WHERE id = 1
                """;


        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            Blob blob = connection.createBlob();

            blob.setBytes(1, Files.readAllBytes(Path.of(
                    "resources",
                    "Boeing_777.jpg")
            ));

            preparedStatement.setBlob(1, blob);
            preparedStatement.executeUpdate();
        }
    }*/
}
