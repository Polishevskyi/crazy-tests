# Script: `calculate_averages.sh`

## Purpose

The script processes a CSV file with user transactions and calculates **average monthly spending**.  
The result is saved to `averages.csv` file.

---

## Input Format: `transactions.csv`

```csv
user,date,category,amount
alice,2025-01-15,food,1200
alice,2025-01-20,transport,500
bob,2025-02-03,food,800
```

* `user` — user login
* `date` — transaction date in `YYYY-MM-DD` format
* `category` — expense category
* `amount` — amount in currency units

---

## Output Format: `averages.csv`

```csv
user,month,average_spending
alice,ALL,850.0
bob,ALL,800.0
```

## ⏱ Non-functional Requirements

| Data Volume       | Execution Time |
| ----------------- | -------------- |
| ≤ 10,000 rows     | ≤ 1 second     |
| ≤ 100,000 rows    | ≤ 5 seconds    |
| ≤ 1,000,000 rows  | ≤ 20 seconds   |

---

## Example Usage

```bash
chmod +x calculate_averages.sh
./calculate_averages.sh data/transactions_100000.csv
```

---

## Project Structure (example)

```
expenses-analyzer/
├── calculate_averages.sh
├── data/
│   ├── transactions_1k.csv
│   ├── transactions_100k.csv
│   └── transactions_1M.csv
├── averages.csv                  # Execution result
├── README.md                     # This file
```