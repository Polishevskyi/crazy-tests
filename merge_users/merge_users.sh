#!/usr/bin/env bash

TXT_FILE=$1
JSON_FILE=$2
CSV_FILE=$3

if [[ ! -f $TXT_FILE || ! -f $JSON_FILE || ! -f $CSV_FILE ]]; then
  echo "One of the input files not found"
  exit 1
fi

# Create temporary file for email
TEMP_CSV=$(mktemp)
tail -n +2 "$CSV_FILE" > "$TEMP_CSV"

# Create data directory if it doesn't exist
mkdir -p data

# Generate header
echo "login,name,email" > data/full_users.csv

# Process TXT file
while read -r login; do
  [[ -z "$login" ]] && continue
  
  # Get name from JSON
  name=$(jq -r --arg login "$login" '.[$login] // null' "$JSON_FILE" 2>/dev/null)
  if [[ "$name" == "null" || -z "$name" ]]; then
    name=""
  fi
  
  # Find email in CSV
  email=$(grep "^$login," "$TEMP_CSV" | cut -d',' -f2 | head -1)
  
  if [[ -n "$name" && -n "$email" ]]; then
    echo "$login,$name,$email" >> data/full_users.csv
  else
    echo "⚠️  Skipped user $login: missing name or email"
  fi
done < "$TXT_FILE"

# Clean up temporary file
rm -f "$TEMP_CSV"

echo "✅ Done: data/full_users.csv"
