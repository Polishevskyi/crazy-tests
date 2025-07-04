import org.junit.jupiter.api.Test;

public class CalculateAverageTest {
    @Test
    public void calculatingAverageWithTwoUsersAndOneRecordWorksCorrectly() {
        new TestScenario()
                .given()
                .generateRecords(0)
                .withRecord("user1", "2025-01-01", "transport", 800.0)
                .withRecord("user2", "2025-01-02", "food", 100.0)
                .when().executeScript() // в 1 метод
                .then()
                .containsRecord("user1", "ALL", 800.0)
                .containsRecord("user2", "ALL", 100.0);
    }

    @Test
    public void calculatingAverageWithInvalidDataLeadsToInvalidDataError() {
        new TestScenario()
                .given()
                .generateRecords(0)
                .withRecord("user1", "invalid-date", "transport", 800.0)
                .when().executeScript() // в 1 метод
                .then()
                .containsError("❌ Обнаружена неверная дата. Завершаем.");
    }

    @Test
    public void calculatingAverageWithNonExistingInputFileLeadsToFileNotFoundError() {
        String nonExistingFile = "file.txt";
        new TestScenario()
                .given()
                .generateRecords(1)
                .when().executeScript(nonExistingFile)
                .containsError("Файл не найден: " + nonExistingFile);
    }

    @Test
    public void calculatingAverageScriptFailsIfTimeoutExceeded() {
        new TestScenario()
                .given()
                .generateLargeVolumeData(100_000)
                .when()
                .executeScriptWithTimeout()
                .then()
                .executionTimeExceedsLimit()
                .exitCodeIs(1)
                .containsError("таймаут");
    }

    @Test
    public void calculatingAverageNoTempFilesAfterFailure() {
        new TestScenario()
                .given()
                .withInvalidData()
                .when()
                .executeScript()
                .then()
                .exitCodeIs(1)
                .noTempFilesExist()
                .containsError("описание сбоя");
    }

    @Test
    public void calculatingAverageNoInjectionsInInputAndOutput() {
        new TestScenario()
                .given()
                .withInjectionData()
                .when()
                .executeScript()
                .then()
                .noInjectionsInOutput();
    }

    @Test
    public void calculatingAverageNoDataLossWithLargeVolume() {
        new TestScenario()
                .given()
                .generateLargeVolumeData(100_000)
                .when()
                .executeScript()
                .then()
                .noDataLoss()
                .containsErrorIfAny();
    }

    @Test
    public void calculatingAverageUnclearErrorMessageForMissingFile() {
        new TestScenario()
                .given()
                .withNonExistingFile()
                .when()
                .executeScript()
                .then()
                .errorMessageIsUnclear();
    }

    @Test
    public void calculatingAverageNoLogsOnError() {
        new TestScenario()
                .given()
                .withInvalidData()
                .when()
                .executeScript()
                .then()
                .logsAreMissingOrUninformative();
    }
}
