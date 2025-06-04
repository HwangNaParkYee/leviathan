#!/bin/bash
eval $USER_COMMANDS

# 디렉토리 및 파일 존재 여부 확인
if [ -d "/home/documents" ] && [ -f "/home/documents/report.txt" ]; then
  # 파일 내용 확인
  report_content=$(cat /home/documents/report.txt)
  if [[ "$report_content" == "이것은 보고서입니다." ]]; then
    # 현재 디렉토리 확인
    current_dir=$(pwd)
    if [[ "$current_dir" == "/home" ]]; then
      echo "맞았습니다!"
      exit 0
    else
      echo "틀렸습니다. 마지막으로 /home 디렉토리로 이동해야 합니다."
      exit 1
    fi
  else
    echo "틀렸습니다. report.txt 파일의 내용이 올바르지 않습니다."
    exit 1
  fi
else
  echo "틀렸습니다. documents 디렉토리 또는 report.txt 파일이 존재하지 않습니다."
  exit 1
fi