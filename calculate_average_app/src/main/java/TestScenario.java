import helpers.DataGenerator;
import helpers.ResultAnalyser;
import helpers.ScriptRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        System.out.println("Initializing data..");
        return this;
    }

    public TestScenario generateRecords(int num) {
        dataGenerator.generateRecords(num);
        return this;
    }

    public TestScenario withRecord(String user, String date, String category, double amount) {
        dataGenerator.withRecord( user,  date,  category,  amount);
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

    public  TestScenario then() {
        System.out.println("Check results..");
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
}
