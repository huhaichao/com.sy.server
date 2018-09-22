#!/bin/sh
TODAY=`date +%Y%m%d`
LOG_DIR=/pushserver/logs
LOG_FILE=$LOG_DIR/catalina.out
if [ -f $LOG_FILE ];then
	cp $LOG_FILE $LOG_DIR/catalina.out.$TODAY
	echo "" >$LOG_FILE
fi

# crontab set this expression: 59 23 * * * sh /transadapter/bin/splitlog.sh