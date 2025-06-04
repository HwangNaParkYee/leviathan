#!/bin/bash
mkdir -p /home/problem

eval $USER_COMMANDS

if [ -f "/home/problem/solution.txt" ]; then
  if grep -q "Hello, world!" /home/problem/solution.txt; then
    echo "맞았습니다!"
    exit 0
  else
    echo "틀렸습니다. 파일 내용이 올바르지 않습니다."
    exit 1
  fi
else
    echo "틀렸습니다. 파일이 생성되지 않았습니다."
    exit 1
fi