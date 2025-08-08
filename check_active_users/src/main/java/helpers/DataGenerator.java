package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class DataGenerator {
    private static final String DATA_DIR = Paths.get(System.getProperty("user.dir"), "data").toString();
    private static final String USERS_FILE = Paths.get(DATA_DIR, "users.txt").toString();
    private static final String LOGINS_FILE = Paths.get(DATA_DIR, "logins.csv").toString();
    private static final String BANNED_FILE = Paths.get(DATA_DIR, "banned.json").toString();

    private final List<String> users = new ArrayList<>();
    private final Map<String, String> logins = new HashMap<>();
    private final Set<String> bannedUsers = new HashSet<>();

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
        users.clear();
        Random random = new Random();
        String[] userNames = { "alice", "bob", "carol", "dave", "eve", "frank", "grace", "henry", "ivy", "jack" };

        for (int i = 0; i < count; i++) {
            String user = userNames[i % userNames.length] + (i >= userNames.length ? String.valueOf(i) : "");
            users.add(user);
        }
        return this;
    }

    public DataGenerator withUser(String username) {
        users.add(username);
        return this;
    }

    public DataGenerator withLogin(String username, String lastLogin) {
        logins.put(username, lastLogin);
        return this;
    }

    public DataGenerator withLogin(String username, int daysAgo) {
        LocalDate date = LocalDate.now().minusDays(daysAgo);
        logins.put(username, date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        return this;
    }

    public DataGenerator withBannedUser(String username) {
        bannedUsers.add(username);
        return this;
    }

    public DataGenerator generateLargeDataset(int userCount, int loginRecords) {
        generateUsers(userCount);

        Random random = new Random();
        for (int i = 0; i < loginRecords; i++) {
            String user = users.get(random.nextInt(users.size()));
            int daysAgo = random.nextInt(60);
            withLogin(user, daysAgo);
        }

        int bannedCount = userCount / 10;
        for (int i = 0; i < bannedCount; i++) {
            String user = users.get(random.nextInt(users.size()));
            withBannedUser(user);
        }

        return this;
    }

    public void writeFiles() {
        writeUsersFile();
        writeLoginsFile();
        writeBannedFile();
    }

    private void writeUsersFile() {
        try (FileWriter writer = new FileWriter(USERS_FILE)) {
            for (String user : users) {
                writer.write(user + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write users file", e);
        }
    }

    private void writeLoginsFile() {
        try (FileWriter writer = new FileWriter(LOGINS_FILE)) {
            writer.write("login,last_login\n");
            for (Map.Entry<String, String> entry : logins.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write logins file", e);
        }
    }

    private void writeBannedFile() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(BANNED_FILE), new ArrayList<>(bannedUsers));
        } catch (IOException e) {
            throw new RuntimeException("Failed to write banned file", e);
        }
    }

    public void cleanupFiles() {
        new File(USERS_FILE).delete();
        new File(LOGINS_FILE).delete();
        new File(BANNED_FILE).delete();
        new File(Paths.get(DATA_DIR, "active_users.csv").toString()).delete();
    }
}