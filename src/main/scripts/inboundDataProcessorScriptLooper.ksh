#!/bin/ksh
#Maintenance:
#==========		==================		===========================
#Date        	Programmer              Description
#=========		==================		===========================
#03/27/2021   Radha Mohan             inbound-data-processor

function initialization {

 interval=2
 noOfIntervals=15
 #loop interval in seconds. Should be 1 if the above "interval" is in seconds;
 #60 if interval is in minutes
 snoozeInterval=60
 doneMin=99
 a=0
}

function archiveLogFile {
    log=$1
	archive_log=$2
	Time=`date '+%H%M'`
	if [ $Time -le 0030 ] && [ $Time -ge 0000 ]
	then
		date=`date '+%Y-%m-%d'`
		mv $log $archive_log.$date
	fi
}
function startInboundProcessor {
echo $1
echo $2
profile=$1
log=$2
instances=`ps -ef |grep -i "spring.application.name=inbound-data-processor"|wc -l`
echo "instances" $instances

if [ "$instances" -eq "1" ]
then
   echo "running instances count is: $instances so going to restart inbound-data-processor " >> $log
    java -jar /elig/scripts/inbound-data-processor.jar --spring.application.name=inbound-data-processor --spring.profiles.active=$profile >> $log
fi
}

function setHostVariables {
ENVIRONMENT=`hostname`
case $ENVIRONMENT in
	coldstoragevm |coldstoragevm )
	log=/cold/storage/logs/inbound-data-processor.log
	archive_log=/cold/storage/logs/archive/inbound-data-processor.log

	initialization;
	archiveLogFile $log $archive_log;

		if [[ "$ENVIRONMENT" = "coldstoragevm" ]]
		then
			profile="dev"
			startEligibilitylzprocess $profile $log

		elif [[ "$ENVIRONMENT" = "coldstoragevm" ]]
		then
			profile="sita"
			startEligibilitylzprocess $profile $log

		elif [[ "$ENVIRONMENT" = "coldstoragevm" ]]
		then
			profile="prod"
			startEligibilitylzprocess $profile $log &


		else
			srv_name="-----Invalid hostname please check-----"
			exit
		fi
	  ;;
	*)
		print "*** ERROR *** Unrecognized Environment for "
    exit 0
    ;;
esac
}

################################################################################
#	MAIN
################################################################################
set -x
setHostVariables;
