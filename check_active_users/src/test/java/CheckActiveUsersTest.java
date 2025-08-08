import org.junit.jupiter.api.*;

public class CheckActiveUsersTest {

    @BeforeEach
    public void setUp() {
        new TestScenario().cleanupTestFiles();
    }

    @AfterEach
    public void tearDown() {
        new TestScenario().cleanupTestFiles();
    }

    @Test
    public void basicScenarioWithActiveUsersWorksCorrectly() {
        new TestScenario()
                .given()
                .withUser("alice")
                .withUser("bob")
                .withUser("carol")
                .withLogin("alice", 5)
                .withLogin("bob", 10)
                .withLogin("carol", 35)
                .writeTestFiles()
                .when()
                .executeScript()
                .then()
                .exitCodeIs(0)
                .fileExists()
                .hasValidCsvFormat()
                .containsUser("alice")
                .containsUser("bob")
                .doesNotContainUser("carol")
                .hasUserCount(2);
    }

    @Test
    public void bannedUsersAreExcludedFromResults() {
        new TestScenario()
                .given()
                .withUser("alice")
                .withUser("bob")
                .withUser("carol")
                .withLogin("alice", 5)
                .withLogin("bob", 5)
                .withLogin("carol", 5)
                .withBannedUser("bob")
                .writeTestFiles()
                .when()
                .executeScript()
                .then()
                .exitCodeIs(0)
                .containsUser("alice")
                .doesNotContainUser("bob")
                .containsUser("carol")
                .hasUserCount(2);
    }

    @Test
    public void usersWithOldLoginsAreExcluded() {
        new TestScenario()
                .given()
                .withUser("alice")
                .withUser("bob")
                .withLogin("alice", 25)
                .withLogin("bob", 35)
                .writeTestFiles()
                .when()
                .executeScript()
                .then()
                .exitCodeIs(0)
                .containsUser("alice")
                .doesNotContainUser("bob")
                .hasUserCount(1);
    }

    @Test
    public void userExactly30DaysAgoIsIncluded() {
        new TestScenario()
                .given()
                .withUser("alice")
                .withLogin("alice", 30)
                .writeTestFiles()
                .when()
                .executeScript()
                .then()
                .exitCodeIs(0)
                .containsUser("alice")
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
    public void userWithoutLoginRecordIsExcluded() {
        new TestScenario()
                .given()
                .withUser("alice")
                .withUser("bob")
                .withLogin("alice", 5)
                .writeTestFiles()
                .when()
                .executeScript()
                .then()
                .exitCodeIs(0)
                .containsUser("alice")
                .doesNotContainUser("bob")
                .hasUserCount(1);
    }

    @Test
    public void resultHasNoDuplicates() {
        new TestScenario()
                .given()
                .withUser("alice")
                .withUser("bob")
                .withLogin("alice", 5)
                .withLogin("bob", 10)
                .writeTestFiles()
                .when()
                .executeScript()
                .then()
                .exitCodeIs(0)
                .hasNoDuplicates();
    }

    @Test
    public void resultIsFormattedCorrectly() {
        new TestScenario()
                .given()
                .withUser("alice")
                .withLogin("alice", 5)
                .writeTestFiles()
                .when()
                .executeScript()
                .then()
                .exitCodeIs(0)
                .fileExists()
                .hasValidCsvFormat();
    }

    @Nested
    @DisplayName("Non-functional Performance Tests")
    class NonFunctionalTests {

        @Test
        public void smallDatasetExecutionTimeIsAcceptable() {
            new TestScenario()
                    .given()
                    .generateLargeDataset(10, 10)
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
                    .generateLargeDataset(100, 100)
                    .writeTestFiles()
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .executionTimeWithin(3000L);
        }

        @Test
        public void largeDatasetExecutionTimeIsAcceptable() {
            new TestScenario()
                    .given()
                    .generateLargeDataset(1000, 1000)
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
                    .generateLargeDataset(1000, 1000)
                    .writeTestFiles()
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .memoryUsageWithin(50 * 1024 * 1024L);
        }

        @Test
        public void performanceIsAcceptableForLargeDataset() {
            new TestScenario()
                    .given()
                    .generateLargeDataset(1000, 1000)
                    .writeTestFiles()
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .performanceIsAcceptable(10000L, 50 * 1024 * 1024L);
        }

        @Test
        public void multipleExecutionsProduceConsistentResults() {
            TestScenario scenario = new TestScenario()
                    .given()
                    .withUser("alice")
                    .withUser("bob")
                    .withLogin("alice", 5)
                    .withLogin("bob", 10)
                    .writeTestFiles();

            long firstExecution = scenario
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .getExecutionTime();

            long secondExecution = scenario
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .getExecutionTime();

            long thirdExecution = scenario
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .getExecutionTime();

            Assertions.assertTrue(Math.abs(firstExecution - secondExecution) < 1000,
                    "Execution times should be consistent");
            Assertions.assertTrue(Math.abs(secondExecution - thirdExecution) < 1000,
                    "Execution times should be consistent");
        }

        @Test
        public void scalabilityIsLinear() {
            TestScenario smallDataset = new TestScenario()
                    .given()
                    .generateLargeDataset(100, 100)
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
                    .generateLargeDataset(500, 500)
                    .writeTestFiles();

            long largeTime = largeDataset
                    .when()
                    .executeScript()
                    .then()
                    .exitCodeIs(0)
                    .getExecutionTime();

            double scalingFactor = (double) largeTime / smallTime;
            Assertions.assertTrue(scalingFactor <= 10.0,
                    "Scaling should be reasonable: " + scalingFactor + "x");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        public void missingUsersFileProducesError() {
            new TestScenario()
                    .given()
                    .withUser("alice")
                    .withLogin("alice", 5)
                    .writeTestFiles()
                    .when()
                    .executeScript("nonexistent.txt",
                            System.getProperty("user.dir") + "/data/logins.csv",
                            System.getProperty("user.dir") + "/data/banned.json")
                    .then()
                    .exitCodeIs(1)
                    .containsError("❌ One of the input files not found.");
        }

        @Test
        public void missingLoginsFileProducesError() {
            new TestScenario()
                    .given()
                    .withUser("alice")
                    .withLogin("alice", 5)
                    .writeTestFiles()
                    .when()
                    .executeScript(System.getProperty("user.dir") + "/data/users.txt",
                            "nonexistent.csv",
                            System.getProperty("user.dir") + "/data/banned.json")
                    .then()
                    .exitCodeIs(1)
                    .containsError("❌ One of the input files not found.");
        }

        @Test
        public void missingBannedFileProducesError() {
            new TestScenario()
                    .given()
                    .withUser("alice")
                    .withLogin("alice", 5)
                    .writeTestFiles()
                    .when()
                    .executeScript(System.getProperty("user.dir") + "/data/users.txt",
                            System.getProperty("user.dir") + "/data/logins.csv",
                            "nonexistent.json")
                    .then()
                    .exitCodeIs(1)
                    .containsError("❌ One of the input files not found.");
        }
    }
}