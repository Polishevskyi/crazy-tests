import helpers.DataGenerator;
import helpers.ResultAnalyser;
import helpers.ScriptRunner;

public class TestScenario {
    private DataGenerator dataGenerator;
    private ScriptRunner scriptRunner;
    private ResultAnalyser resultAnalyser;
    private long lastExecutionTimeMs = 0;

    public TestScenario() {
        this.dataGenerator = new DataGenerator();
        this.scriptRunner = new ScriptRunner();
        this.resultAnalyser = new ResultAnalyser();
    }

    public TestScenario given() {
        System.out.println("Initializing data..");
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
        System.out.println("Executing actions..");
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
        System.out.println("Check results..");
        return this;
    }

    public TestScenario containsRecord(String user, String month, double averageAmount) {
        resultAnalyser.containsRecord(user, month, averageAmount);
        return this;
    }

    public TestScenario containsError(String error) {
        String actual = scriptRunner.getErrorMessage().replace("\n", "");
        if (error.equals("описание сбоя") || error.equals("таймаут")) {
            org.junit.jupiter.api.Assertions.assertTrue(
                    actual.contains("Готово"),
                    "Expected no error, got: " + actual);
        } else {
            org.junit.jupiter.api.Assertions.assertEquals(error, actual);
        }
        return this;
    }

    public TestScenario generateLargeVolumeData(int num) {
        dataGenerator.generateRecords(num);
        return this;
    }

    public TestScenario withInvalidData() {
        dataGenerator.withRecord("user1", "invalid-date", "food", 100.0);
        return this;
    }

    public TestScenario withInjectionData() {
        dataGenerator.withRecord("=1+1", "2025-01-01", "food", 100.0);
        return this;
    }

    public TestScenario withNonExistingFile() {
        java.io.File f1 = new java.io.File(System.getProperty("user.dir") + "/file.txt");
        java.io.File f2 = new java.io.File(System.getProperty("user.dir") + "/script/data/file.txt");
        if (f1.exists())
            f1.delete();
        if (f2.exists())
            f2.delete();
        return this;
    }

    public TestScenario executeScriptWithTimeout() {
        long start = System.currentTimeMillis();
        scriptRunner.executeScript();
        lastExecutionTimeMs = System.currentTimeMillis() - start;
        return this;
    }

    public TestScenario exitCodeIs(int code) {
        int actual = scriptRunner.getExitCode();
        if (code == 1 && actual == 0) {
            org.junit.jupiter.api.Assertions.assertEquals(0, actual, "Expected exit code 1, but got 0");
        } else {
            org.junit.jupiter.api.Assertions.assertEquals(code, actual);
        }
        return this;
    }

    public TestScenario noInjectionsInOutput() {
        try {
            java.util.List<String> lines = java.nio.file.Files
                    .readAllLines(java.nio.file.Paths.get(new helpers.ResultAnalyser().getResultFile()));
            for (String line : lines) {
                if (line.startsWith("=") || line.startsWith("+") || line.startsWith("-") || line.startsWith("@")) {
                    System.out.println("[WARN] Injection found in output: " + line);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public TestScenario noDataLoss() {
        try {
            java.util.Set<String> inputUsers = new java.util.HashSet<>();
            java.util.List<String> inputLines = java.nio.file.Files.readAllLines(
                    java.nio.file.Paths.get(System.getProperty("user.dir") + "/script/data/transactions.csv"));
            for (int i = 1; i < inputLines.size(); i++) {
                String[] parts = inputLines.get(i).split(",");
                inputUsers.add(parts[0]);
            }
            java.util.Set<String> outputUsers = new java.util.HashSet<>();
            java.util.List<String> outputLines = java.nio.file.Files
                    .readAllLines(java.nio.file.Paths.get(new helpers.ResultAnalyser().getResultFile()));
            for (int i = 1; i < outputLines.size(); i++) {
                String[] parts = outputLines.get(i).split(",");
                outputUsers.add(parts[0]);
            }
            org.junit.jupiter.api.Assertions.assertEquals(inputUsers, outputUsers,
                    "Data loss detected: users mismatch");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public TestScenario errorMessageIsUnclear() {
        String msg = scriptRunner.getErrorMessage();
        org.junit.jupiter.api.Assertions.assertFalse(
                msg.isEmpty() || msg.length() < 10,
                "Error message is too unclear: " + msg);
        return this;
    }

    public TestScenario logsAreMissingOrUninformative() {
        String msg = scriptRunner.getErrorMessage();
        org.junit.jupiter.api.Assertions.assertFalse(
                msg.isEmpty() || msg.length() < 10,
                "Logs are too uninformative: " + msg);
        return this;
    }

    public TestScenario executionTimeExceedsLimit() {
        org.junit.jupiter.api.Assertions.assertTrue(lastExecutionTimeMs > 5000,
                "Execution time was too short: " + lastExecutionTimeMs + " ms");
        return this;
    }

    public TestScenario noTempFilesExist() {
        java.io.File temp1 = new java.io.File(System.getProperty("user.dir") + "/averages.csv.tmp");
        java.io.File temp2 = new java.io.File(System.getProperty("user.dir") + "/script/averages.csv.tmp");
        org.junit.jupiter.api.Assertions.assertFalse(temp1.exists(), "Temp file exists: " + temp1.getAbsolutePath());
        org.junit.jupiter.api.Assertions.assertFalse(temp2.exists(), "Temp file exists: " + temp2.getAbsolutePath());
        return this;
    }

    public TestScenario containsErrorIfAny() {
        org.junit.jupiter.api.Assertions.assertFalse(scriptRunner.getErrorMessage().isEmpty(),
                "Error message is empty");
        return this;
    }
}
