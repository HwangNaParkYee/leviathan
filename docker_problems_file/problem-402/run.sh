#!/bin/bash
mkdir -p problem
eval $USER_COMMANDS

if [ -f "/home/problem/solution.txt" ]; then
  if grep -q "Success!" /home/problem/solution.txt; then
    echo "맞았습니다!"
    exit 0
  else
    echo "틀렸습니다. 파일 내용이 잘못되었습니다."
    exit 1
  fi
else
    echo "틀렸습니다. 파일이 생성되지 않았습니다."
    exit 1
fi