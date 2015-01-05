package writers;

import java.io.File;
import java.sql.*;

public class DatabaseTranslationWriter extends TranslationWriter {

    private final String DB_FOLDER = LANGUAGE_FOLDER + File.separator + "db";

    private final String CONNECTION_FORMAT =
            String.format("jdbc:sqlite:%s" + File.separator + "quran.%s.%s.db", DB_FOLDER, languageCode, translatorName);

    public DatabaseTranslationWriter(String translatorName, String languageCode) {
        super(translatorName, languageCode);

        File fileDbFolder = new File(DB_FOLDER);
        if (!fileDbFolder.exists()) {
            fileDbFolder.mkdir();
        }

        try (Connection connection = DriverManager.getConnection(CONNECTION_FORMAT);
            Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE properties(property text, value text)");
            statement.executeUpdate("CREATE VIRTUAL TABLE verses using fts3(sura integer, ayah integer, text text, primary key(sura, ayah))");
            statement.executeUpdate("INSERT INTO properties(property, value) VALUES ('schema_version', 2)");
            statement.executeUpdate("INSERT INTO properties(property, value) VALUES ('text_version', 1)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(int sura, int ayah, String translationText) {
        if (translationText == null) {
            throw new NullPointerException("Translation text by" + translatorName + " for writing is null.");
        }
        String insertNew = "INSERT INTO verses(sura, ayah, text) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(CONNECTION_FORMAT);
             PreparedStatement preparedStatement = connection.prepareStatement(insertNew)) {
            preparedStatement.setInt(1, sura);
            preparedStatement.setInt(2, ayah);
            preparedStatement.setString(3, translationText);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
