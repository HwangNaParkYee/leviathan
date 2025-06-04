#!/bin/bash
eval $USER_COMMANDS

# 결과 파일 확인
if [ -f "/home/documents/report/result.txt" ]; then
    echo "맞았습니다!"
    exit 0
else
    echo "틀렸습니다."
    exit 1
fi