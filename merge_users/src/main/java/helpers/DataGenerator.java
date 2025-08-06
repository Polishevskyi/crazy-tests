package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DataGenerator {
    private static final String DATA_DIR = Paths.get(System.getProperty("user.dir"), "data").toString();
    private static final String USERS_TXT_FILE = Paths.get(DATA_DIR, "users.txt").toString();
    private static final String USERS_JSON_FILE = Paths.get(DATA_DIR, "users.json").toString();
    private static final String USERS_CSV_FILE = Paths.get(DATA_DIR, "users.csv").toString();

    private final List<String> userLogins = new ArrayList<>();
    private final Map<String, String> userNames = new HashMap<>();
    private final Map<String, String> userEmails = new HashMap<>();

    public DataGenerator() {
        createDataDir();
    }

    private void createDataDir() {
        File directory = new File(DATA_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public DataGenerator generateUsers(int count) {
        userLogins.clear();
        userNames.clear();
        userEmails.clear();

        String[] names = { "Alice Smith", "Bob Johnson", "Carol Lee", "Dave Brown", "Eve Wilson",
                "Frank Miller", "Grace Davis", "Henry Garcia", "Ivy Rodriguez", "Jack Martinez" };
        String[] domains = { "example.com", "test.org", "demo.net", "sample.io" };

        Random random = new Random();

        for (int i = 0; i < count; i++) {
            String login = "user" + (i + 1);
            String name = names[i % names.length];
            String email = login + "@" + domains[i % domains.length];

            userLogins.add(login);
            userNames.put(login, name);
            userEmails.put(login, email);
        }
        return this;
    }

    public DataGenerator withUser(String login) {
        if (!userLogins.contains(login)) {
            userLogins.add(login);
        }
        return this;
    }

    public DataGenerator withUserName(String login, String name) {
        userNames.put(login, name);
        return this;
    }

    public DataGenerator withUserEmail(String login, String email) {
        userEmails.put(login, email);
        return this;
    }

    public DataGenerator withCompleteUser(String login, String name, String email) {
        withUser(login);
        withUserName(login, name);
        withUserEmail(login, email);
        return this;
    }

    public DataGenerator generateLargeDataset(int userCount) {
        generateUsers(userCount);

        // Додаємо кілька користувачів з неповними даними для тестування
        Random random = new Random();
        int incompleteUsers = Math.min(userCount / 10, 5);

        for (int i = 0; i < incompleteUsers; i++) {
            String login = "incomplete" + i;
            userLogins.add(login);

            if (random.nextBoolean()) {
                userNames.put(login, "Incomplete User " + i);
            }
            if (random.nextBoolean()) {
                userEmails.put(login, login + "@incomplete.com");
            }
        }

        return this;
    }

    public void writeFiles() {
        writeUsersTextFile();
        writeUsersJsonFile();
        writeUsersCsvFile();
    }

    private void writeUsersTextFile() {
        try (FileWriter writer = new FileWriter(USERS_TXT_FILE)) {
            for (String login : userLogins) {
                writer.write(login + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write users.txt file", e);
        }
    }

    private void writeUsersJsonFile() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(USERS_JSON_FILE), userNames);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write users.json file", e);
        }
    }

    private void writeUsersCsvFile() {
        try (FileWriter writer = new FileWriter(USERS_CSV_FILE)) {
            writer.write("login,email\n");
            for (Map.Entry<String, String> entry : userEmails.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write users.csv file", e);
        }
    }

    public void cleanupFiles() {
        new File(USERS_TXT_FILE).delete();
        new File(USERS_JSON_FILE).delete();
        new File(USERS_CSV_FILE).delete();
        new File(Paths.get(DATA_DIR, "full_users.csv").toString()).delete();
    }

    public int getUserCount() {
        return userLogins.size();
    }

    public List<String> getUserLogins() {
        return new ArrayList<>(userLogins);
    }

    public Map<String, String> getUserNames() {
        return new HashMap<>(userNames);
    }

    public Map<String, String> getUserEmails() {
        return new HashMap<>(userEmails);
    }
}