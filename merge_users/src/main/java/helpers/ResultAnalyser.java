package helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ResultAnalyser {
    private static final String RESULT_FILE = Paths.get(System.getProperty("user.dir"), "data", "full_users.csv")
            .toString();
    private List<UserRecord> records = new ArrayList<>();

    public static class UserRecord {
        public final String login;
        public final String name;
        public final String email;

        public UserRecord(String login, String name, String email) {
            this.login = login;
            this.name = name;
            this.email = email;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            UserRecord that = (UserRecord) obj;
            return Objects.equals(login, that.login) &&
                    Objects.equals(name, that.name) &&
                    Objects.equals(email, that.email);
        }

        @Override
        public int hashCode() {
            return Objects.hash(login, name, email);
        }

        @Override
        public String toString() {
            return String.format("UserRecord{login='%s', name='%s', email='%s'}", login, name, email);
        }
    }

    public ResultAnalyser containsUser(String login) {
        loadRecordsIfNeeded();
        boolean found = records.stream().anyMatch(record -> record.login.equals(login));
        assertTrue(found, "User " + login + " should be in merged users");
        return this;
    }

    public ResultAnalyser doesNotContainUser(String login) {
        loadRecordsIfNeeded();
        boolean found = records.stream().anyMatch(record -> record.login.equals(login));
        assertFalse(found, "User " + login + " should NOT be in merged users");
        return this;
    }

    public ResultAnalyser containsUserWithData(String login, String name, String email) {
        loadRecordsIfNeeded();
        Optional<UserRecord> userRecord = records.stream()
                .filter(record -> record.login.equals(login))
                .findFirst();

        assertTrue(userRecord.isPresent(), "User " + login + " should be in merged users");
        UserRecord record = userRecord.get();
        assertEquals(name, record.name, "User " + login + " should have name " + name);
        assertEquals(email, record.email, "User " + login + " should have email " + email);
        return this;
    }

    public ResultAnalyser hasUserCount(int expectedCount) {
        loadRecordsIfNeeded();
        assertEquals(expectedCount, records.size(),
                "Merged users count should be " + expectedCount);
        return this;
    }

    public ResultAnalyser hasNoDuplicates() {
        loadRecordsIfNeeded();
        Set<String> usernames = new HashSet<>();
        List<String> duplicates = new ArrayList<>();

        for (UserRecord record : records) {
            if (!usernames.add(record.login)) {
                duplicates.add(record.login);
            }
        }

        assertTrue(duplicates.isEmpty(),
                "Found duplicate users: " + String.join(", ", duplicates));
        return this;
    }

    public ResultAnalyser isSortedByLogin() {
        loadRecordsIfNeeded();
        List<String> usernames = records.stream()
                .map(record -> record.login)
                .collect(Collectors.toList());

        List<String> sortedUsernames = new ArrayList<>(usernames);
        sortedUsernames.sort(String::compareTo);

        assertEquals(sortedUsernames, usernames,
                "Users should be sorted alphabetically by login");
        return this;
    }

    public ResultAnalyser fileExists() {
        Path resultPath = Paths.get(RESULT_FILE);
        assertTrue(Files.exists(resultPath),
                "Result file " + RESULT_FILE + " should exist");
        return this;
    }

    public ResultAnalyser hasValidCsvFormat() {
        loadRecordsIfNeeded();

        try (BufferedReader reader = new BufferedReader(new FileReader(RESULT_FILE))) {
            String headerLine = reader.readLine();
            assertNotNull(headerLine, "CSV file should have a header");
            assertEquals("login,name,email", headerLine,
                    "CSV header should be 'login,name,email'");

            String line;
            int lineNumber = 2;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",", 3);
                    assertEquals(3, parts.length,
                            "Line " + lineNumber + " should have exactly 3 columns");

                    assertFalse(parts[0].trim().isEmpty(),
                            "Login should not be empty on line " + lineNumber);
                    assertFalse(parts[1].trim().isEmpty(),
                            "Name should not be empty on line " + lineNumber);
                    assertFalse(parts[2].trim().isEmpty(),
                            "Email should not be empty on line " + lineNumber);

                    // Перевіряємо формат email
                    assertTrue(parts[2].contains("@"),
                            "Email should contain @ on line " + lineNumber);
                }
                lineNumber++;
            }
        } catch (IOException e) {
            fail("Failed to read result file: " + e.getMessage());
        }

        return this;
    }

    public ResultAnalyser allUsersHaveCompleteData() {
        loadRecordsIfNeeded();

        for (UserRecord record : records) {
            assertNotNull(record.login, "Login should not be null");
            assertNotNull(record.name, "Name should not be null for user " + record.login);
            assertNotNull(record.email, "Email should not be null for user " + record.login);

            assertFalse(record.login.trim().isEmpty(), "Login should not be empty");
            assertFalse(record.name.trim().isEmpty(), "Name should not be empty for user " + record.login);
            assertFalse(record.email.trim().isEmpty(), "Email should not be empty for user " + record.login);

            assertTrue(record.email.contains("@"), "Email should be valid for user " + record.login);
        }

        return this;
    }

    public int getUserCount() {
        loadRecordsIfNeeded();
        return records.size();
    }

    public List<String> getUsernames() {
        loadRecordsIfNeeded();
        return records.stream()
                .map(record -> record.login)
                .collect(Collectors.toList());
    }

    public List<UserRecord> getAllUsers() {
        loadRecordsIfNeeded();
        return new ArrayList<>(records);
    }

    private void loadRecordsIfNeeded() {
        if (records.isEmpty()) {
            loadRecords();
        }
    }

    private void loadRecords() {
        records.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(RESULT_FILE))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return;
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",", 3);
                    if (parts.length == 3) {
                        records.add(new UserRecord(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read result file: " + RESULT_FILE, e);
        }
    }
}