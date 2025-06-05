#!/bin/bash
eval $USER_COMMANDS

# 파일 생성 여부 확인
if [ -f "/home/hello.txt" ]; then
    echo "맞았습니다!"
    exit 0
else
    echo "틀렸습니다."
    exit 1
fi