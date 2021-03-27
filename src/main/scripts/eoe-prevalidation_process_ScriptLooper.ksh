#!/bin/ksh
#Maintenance:
#==========		==================		===========================
#Date        	Programmer              Description
#=========		==================		===========================
#12/20/2020     Ganesh                  eoe-prevalidation-process

function initialization {

 configUser=eniconfig
 configPwd=Phoenix2019
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
function startEligibilitylzprocess {
echo $1
echo $2
profile=$1
log=$2
instances=`ps -ef |grep -i "spring.application.name=eoe-prevalidation-process"|wc -l`
echo "instances" $instances

if [ "$instances" -eq "1" ]
then
   echo "running instances count is: $instances so going to restart eligibility-lz-process " >> $log
    java -jar /elig/scripts/eoe-prevalidation-process.jar --spring.application.name=eoe-prevalidation-process --spring.profiles.active=$profile --spring.cloud.config.name=eoe-prevalidation-process --spring.cloud.config.uri=$configUri  --spring.cloud.config.username=$configUser --spring.cloud.config.password=$configPwd >> $log
fi
}

function setHostVariables {
ENVIRONMENT=`hostname`
case $ENVIRONMENT in
	apvrs44636 |apvrp57328 |apvrp45323 )

	configUri=http://eligibility-eoe-dev.optum.com
	log=/elig/logs/eoe-prevalidation-process.log
	archive_log=/elig/logs/archive/eoe-prevalidation-process.log

	initialization;
	archiveLogFile $log $archive_log;

		if [[ "$ENVIRONMENT" = "apvrp45323" ]]
		then
			profile="elr-prod"
			startEligibilitylzprocess $profile $log

		elif [[ "$ENVIRONMENT" = "apvrp57328" ]]
		then
			profile="elr-adop"
			startEligibilitylzprocess $profile $log

		elif [[ "$ENVIRONMENT" = "apvrs44636" ]]
		then
			profile="elr-stg"
			startEligibilitylzprocess $profile $log &


		else
			srv_name="-----Invalid hostname please check-----"
			exit
		fi

	  ;;
	  apvrd31714 |apvrt82899 |apvrt82897 |apvrt82895 )
	  configUri=http://eligibility-eoe-dev.optum.com

		if [[ "$ENVIRONMENT" = "apvrd31714" ]]
		then
            configUri=http://eligibility-eoe-dev.optum.com
			devlog=/elig/logs/eoe-prevalidation-process.log
			dev_archive_log=/elig/logs/archive/eoe-prevalidation-process.log

			initialization;
			archiveLogFile $devlog $dev_archive_log &
			P1=$!
			wait $P1

			devlog=/elig/logs/eoe-prevalidation-process.log

			startEligibilitylzprocess "dev" $devlog &



		elif [[ "$ENVIRONMENT" = "apvrt82899" ]]
		then
		    configUri=http://eligibility-eoe-dev.optum.com
			uatalog=/elig/logs/eoe-prevalidation-process.log
			uata_archive_log=/elig/logs/archive/eoe-prevalidation-process.log

			initialization;
			archiveLogFile $uatalog $uata_archive_log &
			P1=$!
			wait $P1

			uatalog=/elig/logs/eoe-prevalidation-process.log

			startEligibilitylzprocess "uata" $uatalog &



		elif [[ "$ENVIRONMENT" = "apvrt82897" ]]
        then
            configUri=http://eligibility-eoe-dev.optum.com
			sitalog=/elig/logs/eoe-prevalidation-process.log
			sita_archive_logs=/elig/logs/archive/eoe-prevalidation-process.log

			initialization;
			archiveLogFile $sitalog $sita_archive_logs &
			P1=$!
            wait $P1

            sitalog=/elig/logs/eoe-prevalidation-process.log

			startEligibilitylzprocess "sita" $sitalog &

		else
			srv_name="-----Invalid hostname please check-----"
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
