#!/bin/ksh

#Maintenance:
#==========		==================		===========================
#Date        	   Programmer                Description
#=========		==================		===========================
#12/08/2020         Ganesh           user input module to restart Pre val process

function stageProcess {
 echo 'Please select below options: '
        select option in "start" "stop" "restart" ; do
		if [ "$option" = start ]; then
		   instances=`ps -ef |grep -i "spring.profiles.active=elr-stg"|wc -l`
		    if [ "$instances" -gt "1" ]
			    then
			    echo 'eligibilityLzProcess process is already running in stg, please select option stop or resatrt'
			 else
		        echo 'Initiating eligibilityLzProcess in stg'
		        nohup eoe-prevalidation_process_ScriptLooper.ksh &
		        echo 'eligibilityLzProcess has been started'
			fi
		elif [ "$option" = stop ]; then
		   echo 'stopping eligibilityLzProcess process in stg'
		   stopEligibilitylzprocess "elr-stg"
		   echo 'eligibilityLzProcess has been stopped in stg'
		elif [ "$option" = restart ]; then
		   echo 'stopping eligibilityLzProcess in stg'
		   stopEligibilitylzprocess "elr-stg"
		   sleep 20
		   echo 'restarting eligibilityLzProcess in stg'
		   nohup sh eoe-prevalidation_process_ScriptLooper.ksh &
		fi
		done
}

function adopProcess {
 echo 'Please select below options: '
        select option in "start" "stop" "restart" ; do
		if [ "$option" = start ]; then
		   instances=`ps -ef |grep -i "spring.profiles.active=elr-adop"|wc -l`
		    if [ "$instances" -gt "1" ]
			    then
			    echo 'eligibilityLzProcess process is already running in adop, please select option stop or resatrt'
			 else
		        echo 'Initiating eligibilityLzProcess in adop'
		        nohup eoe-prevalidation_process_ScriptLooper.ksh &
		        echo 'eligibilityLzProcess has been started'
			fi
		elif [ "$option" = stop ]; then
		   echo 'stopping eligibilityLzProcess process in adop'
		   stopEligibilitylzprocess "elr-adop"
		   echo 'eligibilityLzProcess has been stopped in adop'
		elif [ "$option" = restart ]; then
		   echo 'stopping eligibilityLzProcess in adop'
		   stopEligibilitylzprocess "elr-adop"
		   sleep 20
		   echo 'restarting eligibilityLzProcess in adop'
		   nohup sh eoe-prevalidation_process_ScriptLooper.ksh &
		fi
		done
}

function prodProcess {
        echo 'Please select below options: '
        select option in "start" "stop" "restart" ; do
		if [ "$option" = start ]; then
		   instances=`ps -ef |grep -i "spring.profiles.active=elr-prod"|wc -l`
		    if [ "$instances" -gt "1" ]
			    then
			    echo 'eligibilityLzProcess process is already running in prod, please select option stop or resatrt'
			 else
		        echo 'Initiating eligibilityLzProcess in prod'
		        nohup eoe-prevalidation_process_ScriptLooper.ksh &
		        echo 'eligibilityLzProcess has been started'
			fi
		elif [ "$option" = stop ]; then
		   echo 'stopping eligibilityLzProcess process in prod'
		   stopEligibilitylzprocess "elr-prod"
		   echo 'eligibilityLzProcess has been stopped in prod'
		elif [ "$option" = restart ]; then
		   echo 'stopping eligibilityLzProcess in prod'
		   stopEligibilitylzprocess "elr-prod"
		   sleep 20
		   echo 'restarting eligibilityLzProcess in prod'
		   nohup sh eoe-prevalidation_process_ScriptLooper.ksh &
		fi
		done
}

function stopEligibilitylzprocess {
profile=$1
pid=`ps -ef |grep -i "spring.profiles.active=$profile"| grep -v grep | awk '{print$2}'`
kill -9 $pid
}

function setHostVariables {
ENVIRONMENT=`hostname`
case $ENVIRONMENT in
	apvrs44636 |apvrp57328 |apvrp45323 )

if [ "$ENVIRONMENT" = "apvrp45323" ]; then
   prodProcess
elif [ "$ENVIRONMENT" = "apvrp57328" ]; then
   adopProcess
elif [ "$ENVIRONMENT" = "apvrs44636" ]; then
  stageProcess
else
  srv_name="-----Invalid hostname please check-----"
exit
fi
;;
	  apvrd31714)
if [ "$ENVIRONMENT" = "apvrd31714" ]; then
  devProcess
elif ["$ENVIRONMENT" = "apvrt82899"]; then
  uataProcess
elif ["$ENVIRONMENT" = "apvrt82897"]; then
  sitaProcess
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

function devProcess {
        echo 'Please select below options: '
        select option in "start" "stop" "restart" ; do
		if [ "$option" = start ]; then
		   instances=`ps -ef |grep -i "spring.profiles.active=dev"|wc -l`
		    if [ "$instances" -gt "1" ]
			    then
			    echo 'eligibilityLzProcess process is already running in dev, please select option stop or resatrt'
			 else
		        echo 'Initiating eligibilityLzProcess in dev'
		        nohup eoe-prevalidation_process_ScriptLooper.ksh &
		        echo 'eligibilityLzProcess has been started'
			fi
		elif [ "$option" = stop ]; then
		   echo 'stopping eligibilityLzProcess process in dev'
		   stopEligibilitylzprocess "dev"
		   echo 'eligibilityLzProcess has been stopped in dev'
		elif [ "$option" = restart ]; then
		   echo 'stopping eligibilityLzProcess in dev'
		   stopEligibilitylzprocess "dev"
		   sleep 20
		   echo 'restarting eligibilityLzProcess in dev'
		   nohup sh eoe-prevalidation_process_ScriptLooper.ksh &
		fi
		done
}

function uataProcess {
        echo 'Please select below options: '
        select option in "start" "stop" "restart" ; do
		if [ "$option" = start ]; then
		   instances=`ps -ef |grep -i "spring.profiles.active=uata"|wc -l`
		    if [ "$instances" -gt "1" ]
			    then
			    echo 'eligibilityLzProcess process is already running in uata, please select option stop or resatrt'
			 else
		        echo 'Initiating eligibilityLzProcess in uata'
		        nohup sh eoe-prevalidation_process_ScriptLooper.ksh &
		        echo 'eligibilityLzProcess has been started'
			fi
		elif [ "$option" = stop ]; then
		   echo 'stopping eligibilityLzProcess process in uata'
		   stopEligibilitylzprocess "uata"
		   echo 'eligibilityLzProcess has been stopped in uata'
		elif [ "$option" = restart ]; then
		   echo 'stopping eligibilityLzProcess in uata'
		   stopEligibilitylzprocess "uata"
		   sleep 20
		   echo 'restarting eligibilityLzProcess in uata'
		   nohup sh eoe-prevalidation_process_ScriptLooper.ksh &
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
			    echo 'eligibilityLzProcess process is already running in sita, please select option stop or resatrt'
			 else
		        echo 'Initiating eligibilityLzProcess in sita'
		        nohup eoe-prevalidation_process_ScriptLooper.ksh &
		        echo 'eligibilityLzProcess has been started'
			fi
		elif [ "$option" = stop ]; then
		   echo 'stopping eligibilityLzProcess process in sita'
		   stopEligibilitylzprocess "sita"
		   echo 'eligibilityLzProcess has been stopped in sita'
		elif [ "$option" = restart ]; then
		   echo 'stopping eligibilityLzProcess in sita'
		   stopEligibilitylzprocess "sita"
		   sleep 20
		   echo 'restarting eligibilityLzProcess in sita'
		   nohup sh eoe-prevalidation_process_ScriptLooper.ksh &
		fi
		done
}

################################################################################
#	MAIN
################################################################################
setHostVariables;
