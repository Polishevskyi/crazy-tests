import helpers.DataGenerator;
import helpers.ResultAnalyser;
import helpers.ScriptRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestScenario {
    private DataGenerator dataGenerator;
    private ScriptRunner scriptRunner;
    private ResultAnalyser resultAnalyser;

    public TestScenario() {
        this.dataGenerator = new DataGenerator();
        this.scriptRunner = new ScriptRunner();
        this.resultAnalyser = new ResultAnalyser();
    }

    public TestScenario given() {

        return this;
    }

    public TestScenario generateUsers(int count) {
        dataGenerator.generateUsers(count);
        return this;
    }

    public TestScenario withUser(String username) {
        dataGenerator.withUser(username);
        return this;
    }

    public TestScenario withLogin(String username, String lastLogin) {
        dataGenerator.withLogin(username, lastLogin);
        return this;
    }

    public TestScenario withLogin(String username, int daysAgo) {
        dataGenerator.withLogin(username, daysAgo);
        return this;
    }

    public TestScenario withBannedUser(String username) {
        dataGenerator.withBannedUser(username);
        return this;
    }

    public TestScenario generateLargeDataset(int userCount, int loginRecords) {
        dataGenerator.generateLargeDataset(userCount, loginRecords);
        return this;
    }

    public TestScenario writeTestFiles() {
        dataGenerator.writeFiles();
        return this;
    }

    public TestScenario when() {

        return this;
    }

    public TestScenario executeScript() {
        scriptRunner.executeScript();
        return this;
    }

    public TestScenario executeScript(String usersFile, String loginsFile, String bannedFile) {
        scriptRunner.executeScript(usersFile, loginsFile, bannedFile);
        return this;
    }

    public TestScenario then() {

        return this;
    }

    public TestScenario containsUser(String username) {
        resultAnalyser.containsUser(username);
        return this;
    }

    public TestScenario doesNotContainUser(String username) {
        resultAnalyser.doesNotContainUser(username);
        return this;
    }

    public TestScenario containsUserWithDate(String username, String expectedDate) {
        resultAnalyser.containsUserWithDate(username, expectedDate);
        return this;
    }

    public TestScenario hasUserCount(int expectedCount) {
        resultAnalyser.hasUserCount(expectedCount);
        return this;
    }

    public TestScenario allUsersLoggedInWithinDays(int maxDays) {
        resultAnalyser.allUsersLoggedInWithinDays(maxDays);
        return this;
    }

    public TestScenario hasNoDuplicates() {
        resultAnalyser.hasNoDuplicates();
        return this;
    }

    public TestScenario isSortedByLogin() {
        resultAnalyser.isSortedByLogin();
        return this;
    }

    public TestScenario fileExists() {
        resultAnalyser.fileExists();
        return this;
    }

    public TestScenario hasValidCsvFormat() {
        resultAnalyser.hasValidCsvFormat();
        return this;
    }

    public TestScenario containsError(String expectedError) {
        String actualError = scriptRunner.getErrorMessage().trim();
        assertTrue(actualError.contains(expectedError),
                "Expected error message to contain: " + expectedError +
                        ", but got: " + actualError);
        return this;
    }

    public TestScenario executionTimeWithin(long maxTimeMs) {
        long actualTime = scriptRunner.getExecutionTimeMs();
        assertTrue(actualTime <= maxTimeMs,
                "Execution time " + actualTime + "ms should be within " + maxTimeMs + "ms");
        return this;
    }

    public TestScenario memoryUsageWithin(long maxMemoryBytes) {
        long actualMemory = Math.abs(scriptRunner.getMemoryUsedBytes());
        assertTrue(actualMemory <= maxMemoryBytes,
                "Memory usage " + actualMemory + " bytes should be within " + maxMemoryBytes + " bytes");
        return this;
    }

    public TestScenario performanceIsAcceptable(long maxTimeMs, long maxMemoryBytes) {
        boolean acceptable = scriptRunner.isPerformanceAcceptable(maxTimeMs, maxMemoryBytes);
        assertTrue(acceptable,
                "Performance should be acceptable: time=" + scriptRunner.getExecutionTimeMs() +
                        "ms (max=" + maxTimeMs + "ms), memory=" + Math.abs(scriptRunner.getMemoryUsedBytes()) +
                        "bytes (max=" + maxMemoryBytes + "bytes)");
        return this;
    }

    public TestScenario exitCodeIs(int expectedExitCode) {
        int actualExitCode = scriptRunner.getExitCode();
        assertEquals(expectedExitCode, actualExitCode,
                "Expected exit code " + expectedExitCode + " but got " + actualExitCode);
        return this;
    }

    public TestScenario cleanupTestFiles() {
        dataGenerator.cleanupFiles();
        return this;
    }

    public long getExecutionTime() {
        return scriptRunner.getExecutionTimeMs();
    }

    public int getUserCount() {
        return resultAnalyser.getUserCount();
    }
}