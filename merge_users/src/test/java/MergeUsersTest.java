import org.junit.jupiter.api.Test;

public class MergeUsersTest {

    @Test
    public void mergeWithTwoDifferentUsers() {
        new TestScenario()
                .given()
                // .withUser("user1", "Name1", "email1@example.com")
                // .withUser("user2", "Name2", "email2@example.com")
                .when()
                // .executeScript()
                .then()
        // .containsUserInCsv("user1", "Name1", "email1@example.com")
        // .containsUserInCsv("user2", "Name2", "email2@example.com")
        ;
    }

    @Test
    public void mergeWithInvalidJsonLeadsToError() {
        new TestScenario()
                .given()
                // .withInvalidJson()
                .when()
                // .executeScript()
                .then()
        // .containsError("Ошибка: невалидный JSON")
        ;
    }

    @Test
    public void mergeWithNonExistingFileLeadsToError() {
        String nonExistingFile = "nofile.txt";
        new TestScenario()
                .given()
                // .withNonExistingFile(nonExistingFile)
                .when()
                // .executeScript(nonExistingFile)
                .then()
        // .containsError("Файл не найден: " + nonExistingFile)
        ;
    }

    @Test
    public void mergeWithLargeVolumeData() {
        new TestScenario()
                .given()
                // .generateLargeVolumeData(100_000)
                .when()
                // .executeScript()
                .then()
        // .executionTimeNotExceedsLimit()
        // .exitCodeIs(0)
        // .noDataLoss()
        ;
    }

    @Test
    public void mergeWithInjectionData() {
        new TestScenario()
                .given()
                // .withInjectionData()
                .when()
                // .executeScript()
                .then()
        // .noInjectionsInOutput()
        // .noForeignDataInOutput()
        ;
    }

    @Test
    public void mergeWithRepeatedRun() {
        new TestScenario()
                .given()
                // .withUser("user1", "Name1", "email1@example.com")
                .when()
                // .executeScript()
                .then()
        // .containsUserInCsv("user1", "Name1", "email1@example.com")
        ;
        new TestScenario()
                .given()
                // .withUser("user2", "Name2", "email2@example.com")
                .when()
                // .executeScript()
                .then()
        // .containsUserInCsv("user2", "Name2", "email2@example.com")
        ;
    }

    @Test
    public void mergeLogsErrorsCorrectly() {
        new TestScenario()
                .given()
                // .withInvalidJson()
                .when()
                // .executeScript()
                .then()
        // .logsAreInformative()
        ;
    }
}