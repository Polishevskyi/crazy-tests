#!/usr/bin/env bash

USERS_FILE=$1
LOGINS_FILE=$2
BANNED_FILE=$3

if [[ ! -f "$USERS_FILE" || ! -f "$LOGINS_FILE" || ! -f "$BANNED_FILE" ]]; then
  echo "❌ One of the input files not found."
  exit 1
fi

# Read banned users list
BANNED_USERS=$(jq -r '.[]' "$BANNED_FILE" | tr '\n' '|')

# Get today's date in seconds
TODAY=$(date +%s)

# Create temporary file for logins
TEMP_LOGINS=$(mktemp)
tail -n +2 "$LOGINS_FILE" > "$TEMP_LOGINS"

# Create result file in data directory
mkdir -p data
echo "login,last_login" > data/active_users.csv

# Check each user
while read -r user; do
  [[ -z "$user" ]] && continue
  
  # Skip if banned
  if [[ "$BANNED_USERS" == *"|$user|"* || "$BANNED_USERS" == "$user|"* || "$BANNED_USERS" == *"|$user" || "$BANNED_USERS" == "$user" ]]; then
    continue
  fi

  # Find last login date
  last_login=$(grep "^$user," "$TEMP_LOGINS" | cut -d',' -f2 | head -1)

  if [[ -z "$last_login" ]]; then
    continue
  fi

  # Convert login date to seconds (use -j for macOS)
  if [[ "$OSTYPE" == "darwin"* ]]; then
    login_ts=$(date -j -f "%Y-%m-%d" "$last_login" "+%s" 2>/dev/null)
  else
    login_ts=$(date -d "$last_login" +%s 2>/dev/null)
  fi
  
  if [[ -z "$login_ts" ]]; then
    continue
  fi

  # Difference in days
  days=$(( (TODAY - login_ts) / 86400 ))

  if [[ "$days" -le 30 ]]; then
    echo "$user,$last_login" >> data/active_users.csv
  fi
done < "$USERS_FILE"

# Clean up temporary file
rm -f "$TEMP_LOGINS"

echo "✅ Done: data/active_users.csv"