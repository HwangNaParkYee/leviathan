#!/bin/bash
eval $USER_COMMANDS

# 파일 생성 확인
if [ ! -f "/home/dir1/dir2/file.txt" ]; then
    echo "틀렸습니다."
    exit 1
fi

# 파일 내용 확인
file_content=$(cat /home/dir1/dir2/file.txt)
if [ "$file_content" != "Hello, world!" ]; then
    echo "틀렸습니다."
    exit 1
fi

# 현재 디렉토리 확인
current_dir=$(pwd)
if [ "$current_dir" != "/home/dir1" ]; then
    echo "틀렸습니다."
    exit 1
fi

echo "맞았습니다!"
exit 0