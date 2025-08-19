#!/usr/bin/env bash

BASE_PATH="/opt/tomcat/webapps"
UPLOAD_BASE_PATH="/home/was/deploy"
BACKUP_BASE_PATH="/home/was/deploy"
LOG_BASE_PATH="/opt/log"
APP_NAME="$1"

if [[ $# == 0 ]]; then
	echo "NO ARGUMENT: [happytalk|happybot|api-hook]"
	exit 1
fi

if [[ $1 != "happytalk" && $1 != "happybot" && $1 != "api-hook" ]]; then
	echo "WRONG ARGUMENT: [happytalk|happybot|api-hook]"
	exit 1
fi

if [[ $1 == "happytalk" ]]; then
	echo ">> HAPPYTALK NEED RESTART TOMCAT"
	echo ">> TRY SHUTDOWN TOMCAT, WAIT 5 SEC"
	/opt/tomcat/bin/shutdown.sh
	sleep 5

	echo ">> CHECK TOMCAT PROCESS"
	# TOMCAT_PID=`ps -eaf |grep tomcat |grep -v grep |awk '{print $2}'`
	TOMCAT_PID_FILE="/opt/tomcat/tomcat.pid"
	read TOMCAT_PID < $TOMCAT_PID_FILE

	ps -p $TOMCAT_PID
	if [[ $? == 1 ]]; then
		echo ">> GRACEFUL SHUTDOWN SUCCEED"
	else
		echo ">> TRY TERM TOMCAT, WAIT 5 SEC"
		kill -TERM $TOMCAT_PID
		sleep 5
	fi

	ps -p $TOMCAT_PID
	if [[ $? == 1 ]]; then
		echo ">> TERM TOMCAT SUCCEED"
		cat /dev/null > $TOMCAT_PID_FILE
	else
		echo ">> TRY KILL TOMCAT, WAIT 5 SEC"
		kill -KILL $TOMCAT_PID
		sleep 5
	fi

	ps -p $TOMCAT_PID
	if [[ $? == 1 ]]; then
		echo ">> KILL TOMCAT SUCCEED"
		cat /dev/null > $TOMCAT_PID_FILE
	else
		echo ">> FAILED KILL F**KING TOMCAT"
		exit 1
	fi
fi

# COMPARE MOFIFICATION TIME
if [[ "$UPLOAD_BASE_PATH/$APP_NAME.war" -nt "$BASE_PATH/$APP_NAME.war" ]]; then
	echo ">> DEPLOYING $APP_NANE APPLICATION ..."
	if [[ -f "$BASE_PATH/$APP_NAME.war" ]]; then
		cp -ua "$BASE_PATH/$APP_NAME.war" "$BACKUP_BASE_PATH/$APP_NAME.war.bak"
	fi

	cp -u "$UPLOAD_BASE_PATH/$APP_NAME.war" "$BASE_PATH/$APP_NAME.war"

	if [[ $1 == "happytalk" ]]; then
		echo ">> RESTART TOMCAT, WAIT 5 SEC"
		/opt/tomcat/bin/startup.sh
		sleep 5
	fi

	( tail -f -n0 $LOG_BASE_PATH/$APP_NAME/spring.log & ) | sed "/Started ServletInitializer in/q"

	echo ">> DEPLOYMENT SUCCEED"
	exit 0
else
	echo ">> NO NEWER FILE TO DEPLOY"
	exit 1
fi
