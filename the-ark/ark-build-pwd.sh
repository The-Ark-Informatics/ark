#!/bin/bash

# Define global variables
#export DEV_USER=$USER   #assumes the same as current user
export WORKSPACE_DIR=`pwd` #relative to current user HOME dir

# Maven build/install/package the source 
cd $WORKSPACE_DIR/ark-common
mvn clean install
if [ "$?" != "0" ]; then
exit 1
fi

cd $WORKSPACE_DIR/ark-admin
mvn clean install
if [ "$?" != "0" ]; then
exit 1
fi

cd $WORKSPACE_DIR/ark-study
mvn clean install
if [ "$?" != "0" ]; then
exit 1
fi

cd $WORKSPACE_DIR/ark-registry
mvn clean install
if [ "$?" != "0" ]; then
exit 1
fi

cd $WORKSPACE_DIR/ark-phenotypic
mvn clean install
if [ "$?" != "0" ]; then
exit 1
fi

#cd $WORKSPACE_DIR/ark-geno
#mvn clean install
#if [ "$?" != "0" ]; then
#exit 1
#fi

cd $WORKSPACE_DIR/ark-report
mvn clean install
if [ "$?" != "0" ]; then
exit 1
fi

cd $WORKSPACE_DIR/ark-lims
mvn clean install
if [ "$?" != "0" ]; then
exit 1
fi

cd $WORKSPACE_DIR/ark-container
mvn clean package
if [ "$?" != "0" ]; then
exit 1
fi


SCRIPTPATH=$(dirname $0)
echo "Ark war file built"
