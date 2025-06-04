#!/bin/bash
eval $USER_COMMANDS

#problem 디렉토리 존재 여부 확인 및 problem.txt 파일 존재 여부 확인
if [ -d "/home/problem" ] && [ -f "/home/problem/problem.txt" ]; then
    echo "맞았습니다!"
    exit 0
else
    echo "틀렸습니다."
    exit 1
fi