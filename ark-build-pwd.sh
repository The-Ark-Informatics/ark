#!/bin/bash

# Define global variables
#export DEV_USER=$USER   #assumes the same as current user
export WORKSPACE_DIR=`pwd` #relative to current user HOME dir
COMPILE_ALL=1

is_changed() {
	[ "$(git diff --shortstat `pwd` 2> /dev/null | tail -n1)" != "" ] || [ $COMPILE_ALL -eq 1 ]
}

printHelpAndExit() {
	printf $"Usage: \n\t -a|--all: Force compile all packages\n"
	exit
}

pointer=1
while [[ $pointer -le $# ]]; do
	param=${!pointer}
	if [[ $param != "-"* ]]; then ((pointer++))
	else
		case $param in
		    -a|--all) COMPILE_ALL=1;;
			-h|--help) printHelpAndExit;;
			*) printHelpAndExit;;
		esac

	# splice out pointer frame from positional list
	[[ $pointer -gt 1 ]] \
		&& set -- ${@:1:((pointer - 1))} ${@:((pointer + 1)):$#} \
		|| set -- ${@:((pointer + 1)):$#};
	fi
done

cd $WORKSPACE_DIR/recaptcha4j
if [ ! -d "target" ];
then
	mvn clean install
	if [ "$?" != "0" ]; then
	exit 1
	fi
fi

# Maven build/install/package the source 
cd $WORKSPACE_DIR/ark-common
if is_changed; 
then 
	mvn initialize
	mvn clean install
	if [ "$?" != "0" ]; then
	exit 1
	fi
fi

cd $WORKSPACE_DIR/ark-admin
if is_changed;
then
	mvn clean install
	if [ "$?" != "0" ]; then
	exit 1
	fi
fi

cd $WORKSPACE_DIR/ark-registry
if is_changed;
then
	mvn clean install
	if [ "$?" != "0" ]; then
	exit 1
	fi
fi

cd $WORKSPACE_DIR/ark-phenotypic
if is_changed;
then
	mvn clean install
	if [ "$?" != "0" ]; then
	exit 1
	fi
fi

cd $WORKSPACE_DIR/ark-genomics
if is_changed;
then
	mvn clean install
	if [ "$?" != "0" ]; then
	exit 1
	fi
fi

cd $WORKSPACE_DIR/ark-report
if is_changed;
then
	mvn clean install
	if [ "$?" != "0" ]; then
	exit 1
	fi
fi

cd $WORKSPACE_DIR/ark-lims
if is_changed;
then
	mvn clean install
	if [ "$?" != "0" ]; then
	exit 1
	fi
fi

cd $WORKSPACE_DIR/ark-work-tracking
if is_changed;
then
	mvn clean install
	if [ "$?" != "0" ]; then
	exit 1
	fi
fi


cd $WORKSPACE_DIR/ark-disease
if is_changed;
then
	mvn clean install
	if [ "$?" != "0" ]; then
	exit 1
	fi
fi

cd $WORKSPACE_DIR/ark-study
if is_changed;
then
	mvn clean install
	if [ "$?" != "0" ]; then
	exit 1
	fi
fi

cd $WORKSPACE_DIR/ark-container
mvn clean package
if [ "$?" != "0" ]; then
exit 1
fi


SCRIPTPATH=$(dirname $0)
echo "Ark war file built"


