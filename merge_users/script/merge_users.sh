#!/bin/bash

TXT_FILE=$1
JSON_FILE=$2
CSV_FILE=$3

if [[ ! -f $TXT_FILE || ! -f $JSON_FILE || ! -f $CSV_FILE ]]; then
  echo "Один из входных файлов не найден"
  exit 1
fi

# Временный файл для email
declare -A EMAILS

# Чтение CSV
while IFS=',' read -r login email; do
  login=$(echo "$login" | xargs)
  if [[ -z "$login" || "$login" == "login" ]]; then
    continue
  fi
  EMAILS["$login"]="$email"
done < "$CSV_FILE"

# Генерация заголовка
echo "login,name,email" > full_users.csv

# Проход по TXT
while read -r login; do
  name=$(jq -r --arg login "$login" '.[$login]' "$JSON_FILE")
  email=${EMAILS[$login]}

  if [[ -n "$name" && -n "$email" ]]; then
    echo "$login,$name,$email" >> full_users.csv
  else
    echo "⚠️  Пропущен пользователь $login: нет имени или email"
  fi
done < "$TXT_FILE"

echo "✅ Готово: full_users.csv"
