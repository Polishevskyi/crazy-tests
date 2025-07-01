package helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResultAnalyser {
    private static final String RESULT_FILE = Paths.get(System.getProperty("user.dir"), "script", "averages.csv").toString();
    private List<String[]> records = new LinkedList<>();

    public void containsRecord(String user, String month, double averageAmount) {
        if (records.isEmpty()) {
            readRecords();
        }

        String[] userRecord = records.stream().filter(record -> record[0].equals(user))
                .findFirst().get();

        assertEquals(month, userRecord[1]);
        assertEquals(averageAmount, Double.parseDouble(userRecord[2]));
    }

    private void readRecords() {
        try {
            List<String> recordStrings = Files.readAllLines(Paths.get(RESULT_FILE));

            for (String record : recordStrings.get(1).split("\\\\n")) {
                String[] parts = record.split(",");
                records.add(parts);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
