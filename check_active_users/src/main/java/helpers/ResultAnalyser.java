package helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ResultAnalyser {
    private static final String RESULT_FILE = Paths.get(System.getProperty("user.dir"), "data", "active_users.csv")
            .toString();
    private List<UserRecord> records = new ArrayList<>();

    public static class UserRecord {
        public final String login;
        public final String lastLogin;

        public UserRecord(String login, String lastLogin) {
            this.login = login;
            this.lastLogin = lastLogin;
        }
    }

    public ResultAnalyser containsUser(String username) {
        loadRecordsIfNeeded();
        boolean found = records.stream().anyMatch(record -> record.login.equals(username));
        assertTrue(found, "User " + username + " should be in active users");
        return this;
    }

    public ResultAnalyser doesNotContainUser(String username) {
        loadRecordsIfNeeded();
        boolean found = records.stream().anyMatch(record -> record.login.equals(username));
        assertFalse(found, "User " + username + " should NOT be in active users");
        return this;
    }

    public ResultAnalyser containsUserWithDate(String username, String expectedDate) {
        loadRecordsIfNeeded();
        Optional<UserRecord> userRecord = records.stream()
                .filter(record -> record.login.equals(username))
                .findFirst();

        assertTrue(userRecord.isPresent(), "User " + username + " should be in active users");
        assertEquals(expectedDate, userRecord.get().lastLogin,
                "User " + username + " should have last login date " + expectedDate);
        return this;
    }

    public ResultAnalyser hasUserCount(int expectedCount) {
        loadRecordsIfNeeded();
        assertEquals(expectedCount, records.size(),
                "Active users count should be " + expectedCount);
        return this;
    }

    public ResultAnalyser allUsersLoggedInWithinDays(int maxDays) {
        loadRecordsIfNeeded();
        LocalDate cutoffDate = LocalDate.now().minusDays(maxDays);

        for (UserRecord record : records) {
            LocalDate loginDate = LocalDate.parse(record.lastLogin, DateTimeFormatter.ISO_LOCAL_DATE);
            assertTrue(loginDate.isAfter(cutoffDate) || loginDate.isEqual(cutoffDate),
                    "User " + record.login + " last login " + record.lastLogin +
                            " should be within " + maxDays + " days");
        }
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
            assertEquals("login,last_login", headerLine,
                    "CSV header should be 'login,last_login'");

            String line;
            int lineNumber = 2;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    assertEquals(2, parts.length,
                            "Line " + lineNumber + " should have exactly 2 columns");

                    assertFalse(parts[0].trim().isEmpty(),
                            "Login should not be empty on line " + lineNumber);

                    try {
                        LocalDate.parse(parts[1], DateTimeFormatter.ISO_LOCAL_DATE);
                    } catch (Exception e) {
                        fail("Invalid date format on line " + lineNumber + ": " + parts[1]);
                    }
                }
                lineNumber++;
            }
        } catch (IOException e) {
            fail("Failed to read result file: " + e.getMessage());
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
                    String[] parts = line.split(",", 2);
                    if (parts.length == 2) {
                        records.add(new UserRecord(parts[0].trim(), parts[1].trim()));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read result file: " + RESULT_FILE, e);
        }
    }
}