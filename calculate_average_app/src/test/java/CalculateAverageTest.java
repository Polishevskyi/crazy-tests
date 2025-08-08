import org.junit.jupiter.api.*;

public class CalculateAverageTest {
    @Test
    public void calculatingAverageWithTwoUsersAndOneRecordWorksCorrectly() {
        new TestScenario()
                .given()
                .generateRecords(0)
                .withRecord("user1", "2025-01-01", "transport", 800.0)
                .withRecord("user2", "2025-01-02", "food", 100.0)
                .when().executeScript() 
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
                .when().executeScript() 
                .then()
                .containsError("❌ Invalid date detected. Terminating.");
    }

    @Test
    public void calculatingAverageWithNonExistingInputFileLeadsToFileNotFoundError() {
        String nonExistingFile = "file.txt";
        new TestScenario()
                .given()
                .generateRecords(1)
                .when().executeScript(nonExistingFile)
                .containsError("File not found: " + nonExistingFile);
    }

    @Nested
    @DisplayName("Non-functional Performance Tests")
    class NonFunctionalTests {

        @Test
        public void smallDatasetExecutionTimeIsAcceptable() {
            new TestScenario()
                    .given()
                    .generateRecords(10)
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .executionTimeWithin(1000L);
        }

        @Test
        public void mediumDatasetExecutionTimeIsAcceptable() {
            new TestScenario()
                    .given()
                    .generateRecords(100)
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .executionTimeWithin(2000L);
        }

        @Test
        public void largeDatasetExecutionTimeIsAcceptable() {
            new TestScenario()
                    .given()
                    .generateRecords(1000)
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .executionTimeWithin(5000L);
        }

        @Test
        public void memoryUsageIsWithinLimits() {
            new TestScenario()
                    .given()
                    .generateRecords(1000)
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .memoryUsageWithin(30 * 1024 * 1024L);
        }

        @Test
        public void performanceIsAcceptableForLargeDataset() {
            new TestScenario()
                    .given()
                    .generateRecords(1000)
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .performanceIsAcceptable(5000L, 30 * 1024 * 1024L);
        }

        @Test
        public void multipleExecutionsProduceConsistentResults() {
            TestScenario scenario = new TestScenario()
                    .given()
                    .generateRecords(0)
                    .withRecord("alice", "2025-01-01", "food", 100.0)
                    .withRecord("bob", "2025-01-02", "transport", 200.0);

            long firstExecution = scenario
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .containsRecord("alice", "ALL", 100.0)
                    .containsRecord("bob", "ALL", 200.0)
                    .getExecutionTime();

            long secondExecution = scenario
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .containsRecord("alice", "ALL", 100.0)
                    .containsRecord("bob", "ALL", 200.0)
                    .getExecutionTime();

            long thirdExecution = scenario
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .containsRecord("alice", "ALL", 100.0)
                    .containsRecord("bob", "ALL", 200.0)
                    .getExecutionTime();

            Assertions.assertTrue(Math.abs(firstExecution - secondExecution) < 2000,
                    "Execution times should be consistent");
            Assertions.assertTrue(Math.abs(secondExecution - thirdExecution) < 2000,
                    "Execution times should be consistent");
        }

        @Test
        public void scalabilityIsReasonable() {
            TestScenario smallDataset = new TestScenario()
                    .given()
                    .generateRecords(10);

            long smallTime = smallDataset
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .getExecutionTime();

            TestScenario largeDataset = new TestScenario()
                    .given()
                    .generateRecords(100);

            long largeTime = largeDataset
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .getExecutionTime();

            double scalingFactor = (double) largeTime / Math.max(smallTime, 1);
            Assertions.assertTrue(scalingFactor <= 50.0,
                    "Scaling should be reasonable: " + scalingFactor + "x");
        }

        @Test
        public void veryLargeDatasetCompletesSuccessfully() {
            new TestScenario()
                    .given()
                    .generateRecords(5000)
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .executionTimeWithin(15000L);
        }
    }
}
