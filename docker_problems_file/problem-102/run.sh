#!/bin/bash
touch original.txt
echo "This is original file content" > original.txt
eval $USER_COMMANDS

if [ -d "/home/new_dir" ] && [ -f "/home/new_dir/copied.txt" ] && [ $(wc -l < /home/new_dir/copied.txt) -eq 1 ]; then
    echo "맞았습니다!"
    exit 0
else
    echo "틀렸습니다."
    exit 1
fi