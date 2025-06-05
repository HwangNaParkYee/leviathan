#!/bin/bash
eval $USER_COMMANDS

# 디렉토리와 파일 존재 여부 확인
if [ -d "/home/testdir" ] && [ -f "/home/testdir/hello.txt" ]; then
  # 파일 내용 확인
  if grep "Hello, world!" "/home/testdir/hello.txt" > /dev/null; then
    echo "맞았습니다!"
    exit 0
  else
    echo "틀렸습니다."
    exit 1
  fi
else
    echo "틀렸습니다."
    exit 1
fi