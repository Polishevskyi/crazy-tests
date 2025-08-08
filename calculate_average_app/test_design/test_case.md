# Test Case Steps

1) Data preparation in CSV format
- data generation with parameter - number of rows
- setting specific data (user, date, category, amount)

2) Script execution with passed path to transaction file
- successful script execution (exit code 0)
- unsuccessful script execution (exit code 1)
- setting execution parameter (filePath)

3) Result verification in averages.csv file
- checking averages.csv file content (line by line)
- checking error messages


BDD - behaviour driven development
given (given) - when (action/steps) - then (result verification)

### Test Case "2 different users"
given
- setting specific data (user1, date, category, amount1)
- setting specific data (user2, date, category, amount2)

when
- successful script execution (exit code 0)

then
- checking averages.csv file content (user1, date, category, amount1)
- checking averages.csv file content (user2, date, category, amount2)

### Test Case "Invalid date"
given
- setting specific data (user1, invalid-date, category, amount1)

when
- unsuccessful script execution (exit code 1)

then
- checking error messages (Invalid date detected)

### Test Case "Non-existing file"
given
- data generation with parameter - number of rows (1)

when
- setting execution parameter (filePath - non-existing path)

then
- checking error messages (file not found)

TDD - test driven development