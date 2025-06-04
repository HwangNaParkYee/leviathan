#!/bin/bash
touch source.txt
echo "This is source file." > source.txt

eval $USER_COMMANDS

if [ -f "/home/dir1/dir2/target.txt" ] && cmp /home/source.txt /home/dir1/dir2/target.txt; then
    echo "맞았습니다!"
    exit 0
else
    echo "틀렸습니다."
    exit 1
fi