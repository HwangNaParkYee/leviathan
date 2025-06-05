#!/bin/bash
eval $USER_COMMANDS

# problem 디렉토리 존재 여부 확인.  존재하지 않으면 틀렸습니다.
if [ ! -d "/home/problem" ]; then
    echo "틀렸습니다."
    exit 1
fi

# problem.txt 파일 존재 여부 확인
if [ -f "/home/problem/problem.txt" ]; then
    echo "맞았습니다!"
    exit 0
else
    echo "틀렸습니다."
    exit 1
fi