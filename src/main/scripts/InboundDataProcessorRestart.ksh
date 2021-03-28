#!/bin/ksh

#Maintenance:
#==========		==================		===========================
#Date        	   Programmer                Description
#=========		==================		===========================
#03/27/2021   Radha Mohan             inbound-data-processor
function prodProcess {
        echo 'Please select below options: '
        select option in "start" "stop" "restart" ; do
		if [ "$option" = start ]; then
		   instances=`ps -ef |grep -i "spring.profiles.active=prod"|wc -l`
		    if [ "$instances" -gt "1" ]
			    then
			    echo 'InboundDataProcessor is already running in prod, please select option stop or resatrt'
			 else
		        echo 'Initiating InboundDataProcessor in prod'
		        nohup inbound_data_processor_ScriptLooper.ksh &
		        echo 'InboundDataProcessor has been started'
			fi
		elif [ "$option" = stop ]; then
		   echo 'stopping InboundDataProcessor process in prod'
		   stopInboundDataProcessor "prod"
		   echo 'InboundDataProcessor has been stopped in prod'
		elif [ "$option" = restart ]; then
		   echo 'stopping InboundDataProcessor in prod'
		   stopInboundDataProcessor "prod"
		   sleep 20
		   echo 'restarting InboundDataProcessor in prod'
		   nohup sh inbound_data_processor_ScriptLooper.ksh &
		fi
		done
}

function stopInboundDataProcessor {
profile=$1
pid=`ps -ef |grep -i "spring.profiles.active=$profile"| grep -v grep | awk '{print$2}'`
kill -9 $pid
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

function devProcess {
        echo 'Please select below options: '
        select option in "start" "stop" "restart" ; do
		if [ "$option" = start ]; then
		   instances=`ps -ef |grep -i "spring.profiles.active=dev"|wc -l`
		    if [ "$instances" -gt "1" ]
			    then
			    echo 'InboundDataProcessor is already running in dev, please select option stop or resatrt'
			 else
		        echo 'Initiating InboundDataProcessor in dev'
		        nohup inbound_data_processor_ScriptLooper.ksh &
		        echo 'InboundDataProcessor has been started'
			fi
		elif [ "$option" = stop ]; then
		   echo 'stopping InboundDataProcessor in dev'
		   stopInboundDataProcessor "dev"
		   echo 'InboundDataProcessor has been stopped in dev'
		elif [ "$option" = restart ]; then
		   echo 'stopping InboundDataProcessor in dev'
		   stopInboundDataProcessor "dev"
		   sleep 20
		   echo 'restarting InboundDataProcessor in dev'
		   nohup sh inbound_data_processor_ScriptLooper.ksh &
		fi
		done
}

function sitaProcess {
        echo 'Please select below options: '
        select option in "start" "stop" "restart" ; do
		if [ "$option" = start ]; then
		   instances=`ps -ef |grep -i "spring.profiles.active=sita"|wc -l`
		    if [ "$instances" -gt "1" ]
			    then
			    echo 'InboundDataProcessor is already running in sita, please select option stop or resatrt'
			 else
		        echo 'Initiating InboundDataProcessor in sita'
		        nohup inbound_data_processor_ScriptLooper.ksh &
		        echo 'eligibilityLzProcess has been started'
			fi
		elif [ "$option" = stop ]; then
		   echo 'stopping InboundDataProcessor in sita'
		   stopInboundDataProcessor "sita"
		   echo 'InboundDataProcessor has been stopped in sita'
		elif [ "$option" = restart ]; then
		   echo 'stopping InboundDataProcessor in sita'
		   stopInboundDataProcessor "sita"
		   sleep 20
		   echo 'restarting InboundDataProcessor in sita'
		   nohup sh inbound_data_processor_ScriptLooper.ksh &
		fi
		done
}

################################################################################
#	MAIN
################################################################################
setHostVariables;
