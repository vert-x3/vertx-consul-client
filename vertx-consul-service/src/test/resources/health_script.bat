@echo off
for /f "delims=" %%x in (%STATUS_FILE%) do exit %%x
