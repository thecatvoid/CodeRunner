#!/bin/bash
echo User: "$(whoami)"
echo Shell: "$(basename $SHELL)"
echo Uptime: "$(uptime -p)"
echo
echo Disk Usage: 
df -h | grep -E ' /storage' | sed 's/      / /g'
echo
echo Bonus:
fortune | head -4 | cowsay -W 40
