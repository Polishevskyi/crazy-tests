# Test Case Grouping

## Data Format Processing and Validation

Example input data:

```csv
user,date,category,amount
alice,2025-01-15,food,1200
alice,2025-01-20,transport,500
bob,2025-02-03,food,800
```

1) Separators ,
   Positive scenario: ,
   Negative scenario: any other separator

2) First column - user name
   Positive scenario: name
   Negative scenario: non-existing user

3) Second column - date
   Positive scenario: date in correct format
   Negative scenario: date in incorrect format

4) Third column - category
   Positive scenario: existing category
   Negative scenario: non-existing category

5) Fourth column - spent amount
   Positive scenario: integer, decimal number
   Negative scenario: negative number, non-number

## Average Calculation Testing

## By User Spending
1) User spent money only 1 month
2) User spent money all months
3) User spent money 1 time per month
4) User spent money many times per month
5) User didn't spend money for a month

## By Number of Users
1) Single user
2) Multiple users

## Calculation Precision
1) User spent decimal values multiple times

# Output Format Testing
1) CSV format ,
2) First column: user
3) Second column: month
4) Third column: average spending amount
5) Negative case handling: clear error message