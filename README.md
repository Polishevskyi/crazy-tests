## CRAZY TESTS

<p>
  <img alt="Java" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" height="48" />
  <img alt="Maven" src="https://raw.githubusercontent.com/devicons/devicon/master/icons/apache/apache-original-wordmark.svg" height="48" />
  <img alt="JUnit" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/junit/junit-original-wordmark.svg" height="48" />
  <img alt="Bash" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/bash/bash-original.svg" height="48" />
  <img alt="Apple" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/apple/apple-original.svg" height="48" />
  <img alt="Linux" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/linux/linux-original.svg" height="48" />
</p>

### Description

Practical scenarios for testing shell scripts with performance checks, format validations, and result assertions via JUnit 5. The repository contains 3 independent cases, each with its own scripts and tests.

### Structure

```
crazy-tests/
  calculate_average_app/   # compute average spend per user
  check_active_users/      # filter active users (last 30 days)
  merge_users/             # merge users from TXT/JSON/CSV into a single CSV
  TEST_RESULTS.md          # test run report
  pom.xml                  # root (wired to calculate_average_app module)
```

### Requirements

- Java 17
- Maven 3.9+
- Bash 5+
- jq 1.6+
- awk, grep, cut, tail, wc, mktemp, date (macOS or Linux)

macOS (Homebrew): `brew install jq`

### Quick start

- Clone and enter the project root:
  - `git clone <repo>`
  - `cd crazy-tests`
- Run tests per module:
  - calculate_average_app: `mvn test`
  - check_active_users: `mvn -f check_active_users/pom.xml test`
  - merge_users: `mvn -f merge_users/pom.xml test`

### Scenarios

#### 1) calculate_average_app

- Script: `calculate_average_app/script/calculate_averages.sh`
- Input CSV schema: `user,date,category,amount`
  ```csv
  user,date,category,amount
  alice,2025-01-05,food,120.50
  bob,2025-02-10,transport,75.00
  ```
- Run:
  - `bash calculate_average_app/script/calculate_averages.sh calculate_average_app/script/data/transactions.csv`
- Output: `calculate_average_app/script/averages.csv`
  - Format: `user,month,average_spending` (month=ALL for aggregated average)
- Error handling:
  - Missing file → exit code 1 and message "File not found: ..."
  - Invalid date → `❌ Invalid date detected. Terminating.`

#### 2) check_active_users

- Script: `check_active_users/check_active_users.sh`
- Dependency: `jq`
- Inputs:
  - `USERS_FILE` (txt): one login per line
    ```
    alice
    bob
    ```
  - `LOGINS_FILE` (csv with header): `login,last_login`
    ```csv
    login,last_login
    alice,2025-01-12
    bob,2025-01-30
    ```
  - `BANNED_FILE` (json array): `["user1","user2"]`
- Run:
  - `bash check_active_users/check_active_users.sh users.txt logins.csv banned.json`
- Output: `check_active_users/data/active_users.csv`
  - Contains users who logged in within ≤30 days and are not banned
- macOS compatibility: uses `date -j` for parsing dates

#### 3) merge_users

- Script: `merge_users/merge_users.sh`
- Dependency: `jq`
- Inputs:
  - `TXT_FILE` (txt): one login per line
  - `JSON_FILE` (json object): key is login, value is name
    ```json
    { "alice": "Alice A.", "bob": "Bob B." }
    ```
  - `CSV_FILE` (csv with header): `login,email`
    ```csv
    login,email
    alice,alice@example.com
    bob,bob@example.com
    ```
- Run:
  - `bash merge_users/merge_users.sh users.txt names.json emails.csv`
- Output: `merge_users/data/full_users.csv`
  - Format: `login,name,email` (missing fields are reported as warnings)

### Testing

- Framework: JUnit 5
- Commands:
  - Root (only `calculate_average_app` wired in root pom): `mvn test`
  - `check_active_users`: `mvn -f check_active_users/pom.xml test`
  - `merge_users`: `mvn -f merge_users/pom.xml test`

### Notes

- See `TEST_RESULTS.md` for test run details
- Input/output data are stored in each module's `.../data` directory
- For large CSVs (`>100k` rows) `calculate_averages.sh` deliberately slows down to exercise perf tests
