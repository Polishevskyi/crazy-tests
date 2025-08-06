import org.junit.jupiter.api.*;

public class MergeUsersTest {

    @BeforeEach
    public void setUp() {
        new TestScenario().cleanupTestFiles();
    }

    @AfterEach
    public void tearDown() {
        new TestScenario().cleanupTestFiles();
    }

    @Test
    public void basicMergeWithCompleteUsersWorksCorrectly() {
        new TestScenario()
                .given()
                .withCompleteUser("alice", "Alice Smith", "alice@example.com")
                .withCompleteUser("bob", "Bob Johnson", "bob@example.com")
                .withCompleteUser("carol", "Carol Lee", "carol@example.com")
                .writeTestFiles()
                .when()
                .executeScript()
                .then()
                .exitCodeIs(0)
                .fileExists()
                .hasValidCsvFormat()
                .containsUserWithData("alice", "Alice Smith", "alice@example.com")
                .containsUserWithData("bob", "Bob Johnson", "bob@example.com")
                .containsUserWithData("carol", "Carol Lee", "carol@example.com")
                .hasUserCount(3)
                .allUsersHaveCompleteData();
    }

    @Test
    public void userWithMissingNameIsSkipped() {
        new TestScenario()
                .given()
                .withUser("alice")
                .withUser("bob")
                .withUserName("alice", "Alice Smith")
                .withUserEmail("alice", "alice@example.com")
                .withUserEmail("bob", "bob@example.com")
                .writeTestFiles()
                .when()
                .executeScript()
                .then()
                .exitCodeIs(0)
                .containsUserWithData("alice", "Alice Smith", "alice@example.com")
                .doesNotContainUser("bob")
                .hasUserCount(1);
    }

    @Test
    public void userWithMissingEmailIsSkipped() {
        new TestScenario()
                .given()
                .withUser("alice")
                .withUser("bob")
                .withUserName("alice", "Alice Smith")
                .withUserName("bob", "Bob Johnson")
                .withUserEmail("alice", "alice@example.com")
                .writeTestFiles()
                .when()
                .executeScript()
                .then()
                .exitCodeIs(0)
                .containsUserWithData("alice", "Alice Smith", "alice@example.com")
                .doesNotContainUser("bob")
                .hasUserCount(1);
    }

    @Test
    public void emptyFilesProduceEmptyResult() {
        new TestScenario()
                .given()
                .generateUsers(0)
                .writeTestFiles()
                .when()
                .executeScript()
                .then()
                .exitCodeIs(0)
                .fileExists()
                .hasValidCsvFormat()
                .hasUserCount(0);
    }

    @Test
    public void duplicateUsersInTextFileAreHandledCorrectly() {
        new TestScenario()
                .given()
                .withUser("alice")
                .withUser("alice")
                .withCompleteUser("alice", "Alice Smith", "alice@example.com")
                .writeTestFiles()
                .when()
                .executeScript()
                .then()
                .exitCodeIs(0)
                .containsUserWithData("alice", "Alice Smith", "alice@example.com")
                .hasUserCount(1)
                .hasNoDuplicates();
    }

    @Test
    public void resultHasValidCsvFormat() {
        new TestScenario()
                .given()
                .withCompleteUser("alice", "Alice Smith", "alice@example.com")
                .writeTestFiles()
                .when()
                .executeScript()
                .then()
                .exitCodeIs(0)
                .fileExists()
                .hasValidCsvFormat()
                .allUsersHaveCompleteData();
    }

    @Nested
    @DisplayName("Non-functional Performance Tests")
    class NonFunctionalTests {

        @Test
        public void smallDatasetExecutionTimeIsAcceptable() {
            new TestScenario()
                    .given()
                    .generateUsers(10)
                    .writeTestFiles()
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
                    .generateUsers(100)
                    .writeTestFiles()
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
                    .generateUsers(1000)
                    .writeTestFiles()
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .executionTimeWithin(10000L);
        }

        @Test
        public void memoryUsageIsWithinLimits() {
            new TestScenario()
                    .given()
                    .generateUsers(1000)
                    .writeTestFiles()
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .memoryUsageWithin(40 * 1024 * 1024L);
        }

        @Test
        public void performanceIsAcceptableForLargeDataset() {
            new TestScenario()
                    .given()
                    .generateUsers(1000)
                    .writeTestFiles()
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .performanceIsAcceptable(10000L, 40 * 1024 * 1024L);
        }

        @Test
        public void multipleExecutionsProduceConsistentResults() {
            TestScenario scenario = new TestScenario()
                    .given()
                    .withCompleteUser("alice", "Alice Smith", "alice@example.com")
                    .withCompleteUser("bob", "Bob Johnson", "bob@example.com")
                    .writeTestFiles();

            long firstExecution = scenario
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .containsUserWithData("alice", "Alice Smith", "alice@example.com")
                    .containsUserWithData("bob", "Bob Johnson", "bob@example.com")
                    .getExecutionTime();

            long secondExecution = scenario
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .containsUserWithData("alice", "Alice Smith", "alice@example.com")
                    .containsUserWithData("bob", "Bob Johnson", "bob@example.com")
                    .getExecutionTime();

            long thirdExecution = scenario
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .containsUserWithData("alice", "Alice Smith", "alice@example.com")
                    .containsUserWithData("bob", "Bob Johnson", "bob@example.com")
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
                    .generateUsers(10)
                    .writeTestFiles();

            long smallTime = smallDataset
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .getExecutionTime();

            smallDataset.cleanupTestFiles();

            TestScenario largeDataset = new TestScenario()
                    .given()
                    .generateUsers(100)
                    .writeTestFiles();

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
                    .generateUsers(2000)
                    .writeTestFiles()
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .executionTimeWithin(30000L);
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        public void missingUsersFileProducesError() {
            new TestScenario()
                    .given()
                    .withCompleteUser("alice", "Alice Smith", "alice@example.com")
                    .writeTestFiles()
                    .when()
                    .executeScript("nonexistent.txt",
                            System.getProperty("user.dir") + "/data/users.json",
                            System.getProperty("user.dir") + "/data/users.csv")
                    .then()
                    .exitCodeIs(1)
                    .containsError("not found");
        }

        @Test
        public void missingJsonFileProducesError() {
            new TestScenario()
                    .given()
                    .withCompleteUser("alice", "Alice Smith", "alice@example.com")
                    .writeTestFiles()
                    .when()
                    .executeScript(System.getProperty("user.dir") + "/data/users.txt",
                            "nonexistent.json",
                            System.getProperty("user.dir") + "/data/users.csv")
                    .then()
                    .exitCodeIs(1)
                    .containsError("not found");
        }

        @Test
        public void missingCsvFileProducesError() {
            new TestScenario()
                    .given()
                    .withCompleteUser("alice", "Alice Smith", "alice@example.com")
                    .writeTestFiles()
                    .when()
                    .executeScript(System.getProperty("user.dir") + "/data/users.txt",
                            System.getProperty("user.dir") + "/data/users.json",
                            "nonexistent.csv")
                    .then()
                    .exitCodeIs(1)
                    .containsError("not found");
        }
    }
}