@echo off
java "-Djava.library.path=opencv lib\build\java\x64" -classpath "out\production\PS;opencv lib\build\java\opencv-2413.jar" com.diana.photo_shop.Main
