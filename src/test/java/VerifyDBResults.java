import java.io.File;
import java.sql.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VerifyDBResults {
    static final int[] ayahQuantity =
            {7, 286, 200, 176, 120, 165, 206, 75, 129, 109, 123, 111, 43, 52, 99, 128, 111, 110, 98, 135, 112, 78, 118, 64, 77, 227
                    , 93, 88, 69, 60, 34, 30, 73, 54, 45, 83, 182, 88, 75, 85, 54, 53, 89, 59, 37, 35, 38, 29, 18, 45, 60, 49, 62, 55, 78
                    , 96, 29, 22, 24, 13, 14, 11, 11, 18, 12, 12, 30, 52, 52, 44, 28, 28, 20, 56, 40, 31, 50, 40, 46, 42, 29, 19, 36, 25
                    , 22, 17, 19, 26, 30, 20, 15, 21, 11, 8, 8, 19, 5, 8, 8, 11, 11, 8, 3, 9, 5, 4, 7, 3, 6, 3, 5, 4, 5, 6};

    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);


    public static void main(String[] args) {
        File resultFolder = new File(System.getProperty("user.dir") + File.separator + "result");
        Queue<File> filesAtResultDirectory = new ArrayDeque<>();
        if (!resultFolder.exists()) {
            System.out.println("No results to verify.");
        }
        File[] listFiles = resultFolder.listFiles();
        if (listFiles != null) {
            filesAtResultDirectory.addAll(Arrays.asList(listFiles));
        }
        while (!filesAtResultDirectory.isEmpty()) {
            File file = filesAtResultDirectory.poll();
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    filesAtResultDirectory.addAll(Arrays.asList(files));
                }
                continue;
            }
            if (file.getName().endsWith(".db")) {
                threadPool.execute(new DBVerify(file.getAbsolutePath()));
            }
        }
        threadPool.shutdown();
    }


    private static class DBVerify implements Runnable {
        private String pathToDBFile;

        public DBVerify(String pathToDBFile) {
            this.pathToDBFile = pathToDBFile;
        }

        @Override
        public void run() {
            final String CONNECTION_FORMAT = "jdbc:sqlite:" + pathToDBFile;
            String ayahSelector = "SELECT text FROM verses WHERE sura=? ORDER BY ayah";

            for (int suraCount = 1; suraCount <= ayahQuantity.length; suraCount++) {
                try (Connection connection = DriverManager.getConnection(CONNECTION_FORMAT);
                     PreparedStatement selectStatement = connection.prepareStatement(ayahSelector)) {
                    selectStatement.setInt(1, suraCount);
                    ResultSet resultSet = selectStatement.executeQuery();
                    int ayahCount = 0;
                    while (resultSet.next()) {
                        ayahCount++;
                        String text = resultSet.getString("text");
                        if (text == null) {
                            System.out.println(pathToDBFile + " - ayah " + suraCount + ":" + ayahCount + " is null!");
                        } else if (text.isEmpty()) {
                            System.out.println(pathToDBFile + " - ayah " + suraCount + ":" + ayahCount + " is empty!");
                        }
                    }
                    if (ayahCount != ayahQuantity[suraCount - 1]) {
                        System.out.println(pathToDBFile + " - sura " + suraCount + " isn't full!");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(pathToDBFile + " verified.");
        }

    }
}
