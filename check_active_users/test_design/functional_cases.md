# Functional Test Cases for check_active_users.sh

## Core Functionality

### TC-F-001: Basic scenario with active users

**Description**: Verification of main active user filtering logic  
**Data**:

- users.txt: alice, bob, carol
- logins.csv: alice (5 days ago), bob (10 days ago), carol (35 days ago)
- banned.json: []
  **Expected**: active_users.csv contains alice, bob

### TC-F-002: Banned user filtering

**Description**: Banned users should not appear in results  
**Data**:

- users.txt: alice, bob, carol
- logins.csv: all logged in 5 days ago
- banned.json: ["bob"]
  **Expected**: active_users.csv contains alice, carol

### TC-F-003: Last login date filtering

**Description**: Users who haven't logged in for >30 days are excluded  
**Data**:

- users.txt: alice, bob
- logins.csv: alice (25 days ago), bob (35 days ago)
- banned.json: []
  **Expected**: active_users.csv contains only alice

## Edge Cases

### TC-F-004: Empty files

**Description**: Handling empty input files  
**Data**: All files are empty or contain only headers  
**Expected**: active_users.csv contains only header

### TC-F-005: User without login record

**Description**: Users without login information are excluded  
**Data**:

- users.txt: alice, bob
- logins.csv: only alice
- banned.json: []
  **Expected**: active_users.csv contains only alice

### TC-F-006: Exactly 30 days since login

**Description**: Verification of 30-day boundary value  
**Data**:

- users.txt: alice
- logins.csv: alice (exactly 30 days ago)
- banned.json: []
  **Expected**: alice is included in result

## Error Scenarios

### TC-F-007: Missing input files

**Description**: Script should report missing files  
**Data**: One or more files are missing  
**Expected**: Error "❌ One of the input files not found.", exit code 1

### TC-F-008: Invalid JSON in banned.json

**Description**: Handling corrupted JSON file  
**Data**: banned.json contains invalid JSON  
**Expected**: Script terminates with error

### TC-F-009: Invalid date format

**Description**: Handling invalid dates in logins.csv  
**Data**: logins.csv contains dates in wrong format  
**Expected**: Records with invalid dates are ignored

## Output Data Format

### TC-F-010: Correct CSV format

**Description**: Output file should have proper CSV format  
**Expected**:

- Header "login,last_login"
- Each line has 2 fields
- No extra spaces or characters

### TC-F-011: Result sorting

**Description**: Results should be sorted by login  
**Data**: Multiple active users  
**Expected**: Users sorted alphabetically

### TC-F-012: No duplicates

**Description**: Each user should appear in result only once  
**Data**: User has multiple records in logins.csv  
**Expected**: User appears once with latest login date