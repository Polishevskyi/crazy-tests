# Functional Test Cases for merge_users.sh

## Core Functionality

### TC-F-001: Basic user merging

**Description**: Verification of main user data merging logic  
**Data**:

- users.txt: alice, bob, carol
- users.json: {"alice": "Alice Smith", "bob": "Bob Johnson", "carol": "Carol Lee"}
- users.csv: alice,alice@example.com; bob,bob@example.com; carol,carol@example.com
  **Expected**: full_users.csv contains all users with complete data

### TC-F-002: User with missing name

**Description**: Users without name in JSON should be skipped  
**Data**:

- users.txt: alice, bob
- users.json: {"alice": "Alice Smith"}
- users.csv: alice,alice@example.com; bob,bob@example.com
  **Expected**: full_users.csv contains only alice, bob skipped with warning

### TC-F-003: User with missing email

**Description**: Users without email in CSV should be skipped  
**Data**:

- users.txt: alice, bob
- users.json: {"alice": "Alice Smith", "bob": "Bob Johnson"}
- users.csv: alice,alice@example.com
  **Expected**: full_users.csv contains only alice, bob skipped with warning

### TC-F-004: User missing from both JSON and CSV

**Description**: Users without both name and email should be skipped  
**Data**:

- users.txt: alice, bob
- users.json: {"alice": "Alice Smith"}
- users.csv: alice,alice@example.com
  **Expected**: full_users.csv contains only alice, bob skipped with warning

## Edge Cases

### TC-F-005: Empty files

**Description**: Handling empty input files  
**Data**: All files are empty or contain only headers  
**Expected**: full_users.csv contains only header

### TC-F-006: Duplicate users in users.txt

**Description**: Duplicate logins should not create duplicate records  
**Data**:

- users.txt: alice, alice, bob
- users.json: {"alice": "Alice Smith", "bob": "Bob Johnson"}
- users.csv: alice,alice@example.com; bob,bob@example.com
  **Expected**: full_users.csv contains alice and bob only once each

## Error Scenarios

### TC-F-007: Missing input files

**Description**: Script should report missing files  
**Data**: One or more files are missing  
**Expected**: Error "One of the input files not found", exit code 1

### TC-F-008: Invalid JSON format

**Description**: Handling corrupted JSON file  
**Data**: users.json contains invalid JSON syntax  
**Expected**: Script terminates with error or skips invalid entries

### TC-F-009: Invalid CSV format

**Description**: Handling malformed CSV file  
**Data**: users.csv has incorrect number of columns or format  
**Expected**: Malformed lines are skipped, valid lines processed

## Output Data Format

### TC-F-010: Correct CSV format

**Description**: Output file should have proper CSV format  
**Expected**:

- Header "login,name,email"
- Each line has 3 fields
- No extra spaces or characters
- Proper CSV escaping if needed

### TC-F-011: Data completeness

**Description**: All required fields should be present for included users  
**Data**: Users with complete data  
**Expected**: No empty fields in output CSV

### TC-F-012: Character encoding

**Description**: Special characters in names and emails should be handled correctly  
**Data**: Users with unicode characters, special symbols  
**Expected**: All characters preserved correctly in output