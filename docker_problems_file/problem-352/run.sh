#!/bin/bash
eval $USER_COMMANDS

# 디렉토리 존재 여부 확인
if [ ! -d "/home/practice" ]; then
  echo "틀렸습니다."
  exit 1
fi

# 파일 존재 여부 확인
if [ ! -f "/home/practice/hello.txt" ]; then
  echo "틀렸습니다."
  exit 1
fi

# 파일 내용 확인
file_content=$(cat /home/practice/hello.txt)
if [ "$file_content" != "Hello, world!" ]; then
  echo "틀렸습니다."
  exit 1
fi

echo "맞았습니다!"
exit 0