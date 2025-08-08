# Script: `merge_users.sh`

## 📌 Purpose

The script merges user information from three sources:

1. `users.txt` — list of user logins (one login per line)
2. `users.json` — JSON dictionary: login → name
3. `users.csv` — CSV file: login, email

The output creates a `full_users.csv` file with merged data:

```csv
login,name,email
```

---

## Example Input Files

### `users.txt`

```
alice
bob
carol
```

### `users.json`

```json
{
  "alice": "Alice Smith",
  "bob": "Bob Johnson",
  "carol": "Carol Lee"
}
```

### `users.csv`

```csv
login,email
alice,alice@example.com
bob,bob@example.com
carol,carol@example.com
```

---

## Example Usage

```bash
chmod +x merge_users.sh
./merge_users.sh users.txt users.json users.csv
```

---

## Output File: `full_users.csv`

```csv
login,name,email
alice,Alice Smith,alice@example.com
bob,Bob Johnson,bob@example.com
carol,Carol Lee,carol@example.com
```

---

## ⚠️ Error Handling

If a login exists in `users.txt` but is missing from `users.json` or `users.csv`, the user is skipped and a warning is printed to console:

```
⚠️  Skipped user bob: missing name or email
```

---

## What Can Be Tested

* Users with complete data are merged correctly
* Missing data in `users.json` or `users.csv` leads to warning logging
* Script doesn't crash on empty files
* Duplicate logins are not duplicated in output file
* Final CSV starts with correct header

---

## Dependencies

The script uses:

* `jq` — for reading JSON
* `bash` version 4+ — for associative arrays

Make sure `jq` is installed:

```bash
sudo apt install jq  # for Ubuntu
```

---

## Project Structure (example)

```
repo/
├── merge_users.sh
├── users.txt
├── users.json
├── users.csv
├── full_users.csv
├── README.md  ← this file
```