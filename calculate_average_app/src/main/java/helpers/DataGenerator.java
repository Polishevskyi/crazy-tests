package helpers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;

public class DataGenerator {
    private static final String DIR_PATH = Paths
            .get(System.getProperty("user.dir"), "calculate_average_app", "script", "data").toString();
    private static final String FILE_PATH = Paths
            .get(System.getProperty("user.dir"), "calculate_average_app", "script", "data", "transactions.csv")
            .toString();

    private static final String[] USERS = {"alice", "bob", "charlie", "tom"};
    private static final String[] CATEGORIES = {"food", "transport", "beauty"};

    private void createDir() {
        File directory = new File(DIR_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private String generateRandomDate() {
        Random random = new Random();
        int month = random.nextInt(12) + 1; // [0,11] +1 -> [1, 12]
        int day = random.nextInt(28) + 1; // [1,29]
        return String.format("2025-%02d-%02d", month, day);
    }

    private void writeData(String data, boolean append) {
        try (FileWriter writer = new FileWriter(FILE_PATH, append)) {
            writer.write(data);

        } catch (IOException e) {
            // Handle exception silently
        }
    }

    private String generateRecordString(int num) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("user,date,category,amount\n");

        Random random = new Random();

        for (int i = 0; i < num; i++) {
            String user = USERS[random.nextInt(USERS.length)];
            String category = CATEGORIES[random.nextInt(CATEGORIES.length)];
            String date = generateRandomDate();
            double amount = 1900 * random.nextDouble() + 100;
            stringBuilder.append(String.format("%s,%s,%s,%.2f\n", user, date, category, amount));
        }
        return stringBuilder.toString();
    }

    public void generateRecords(int num) {
        createDir();
        String generatedRecords = generateRecordString(num);
        writeData(generatedRecords, false);
    }

    public void withRecord(String user, String date, String category, double amount) {
        String record = String.format("%s,%s,%s,%.2f\n", user, date, category, amount);

        writeData(record, true);
    }
}
