# Script: `check_active_users.sh`

## Purpose

The script checks which users are **active** by combining data from three sources:

1. `users.txt` — list of user logins (one login per line)
2. `logins.csv` — CSV file with last login dates (`login,last_login`)
3. `banned.json` — JSON array with banned user logins

The script saves the result to `active_users.csv`, including users who are:
- listed in `users.txt`
- **not** banned
- logged in within the **last 30 days**

---

## Example Input Files

### `users.txt`

```
alice
bob
carol
dave
```

### `logins.csv`
```csv
login,last_login
alice,2025-04-10
bob,2025-01-15
carol,2025-04-25
dave,2024-12-31
```

### `banned.json`

```
["bob", "dave"]
```

## Example Usage

```bash
chmod +x check_active_users.sh
./check_active_users.sh users.txt logins.csv banned.json
```

## Output File: active_users.csv
```
login,last_login
alice,2025-04-10
carol,2025-04-25
```

## ⚠️ Error Handling
- If a login is banned or hasn't logged in for more than 30 days, it's excluded
- For missing data or format errors — script reports to console
- Support for empty files

## How "Last 30 Days" is Determined
The script uses current date and date -d/date -r to compare day differences.

## What Can Be Tested
- Users from users.txt who are banned — excluded

- Old last login dates — excluded

- Valid and invalid JSON

- Missing login in logins.csv

- Empty files

## Dependencies
The script uses:

jq — for reading JSON

bash version 4+

date — for date processing

Make sure jq is installed:

`sudo apt install jq  # for Ubuntu`

### Project Structure

```
repo/
├── check_active_users.sh
├── users.txt
├── logins.csv
├── banned.json
├── active_users.csv
├── README.md  ← this file
```