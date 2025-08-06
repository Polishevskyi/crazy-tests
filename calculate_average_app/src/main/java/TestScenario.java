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

    public TestScenario generateRecords(int num) {
        dataGenerator.generateRecords(num);
        return this;
    }

    public TestScenario withRecord(String user, String date, String category, double amount) {
        dataGenerator.withRecord(user, date, category, amount);
        return this;
    }

    public TestScenario when() {

        return this;
    }

    public TestScenario executeScript() {
        scriptRunner.executeScript();
        return this;
    }

    public TestScenario executeScript(String filePath) {
        scriptRunner.executeScript(filePath);
        return this;
    }

    public TestScenario then() {

        return this;
    }

    public TestScenario containsRecord(String user, String month, double averageAmount) {
        resultAnalyser.containsRecord(user, month, averageAmount);
        return this;
    }

    public TestScenario containsError(String error) {
        assertEquals(error, scriptRunner.getErrorMessage().replace("\n", ""));
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

    public long getExecutionTime() {
        return scriptRunner.getExecutionTimeMs();
    }
}
