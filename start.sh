#!/bin/bash

nohup java -jar /root/quiz-application-/target/api-0.0.1-SNAPSHOT.jar > /log.txt 2>&1 && echo $! > /pid.file