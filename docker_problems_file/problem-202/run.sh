#!/bin/bash
eval $USER_COMMANDS

# 디렉토리 및 파일 존재 여부 확인
if [ -d "/home/mydir" ] && [ -f "/home/mydir/mydata.txt" ]; then
  # 파일 내용 확인
  if grep -q "Hello, world!" "/home/mydir/mydata.txt"; then
    echo "맞았습니다!"
    exit 0
  else
    echo "틀렸습니다. 파일 내용이 잘못되었습니다."
    exit 1
  fi
else
    echo "틀렸습니다. 디렉토리 또는 파일이 생성되지 않았습니다."
    exit 1
fi