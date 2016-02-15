#!/bin/bash

function usage {
  echo "
Usage: generate_message_jaxb_classes.sh [options]

Options:
    --version | -v  : Schema Version. Defaults to 0.2
    --project | -p  : Project folder location. Defaults to ~/code/local/ouh_lrd
    --help    | -h  : This help
"
}

SCHEMA_VERSION='0.2'
PROJECT_FOLDER=~/code/local/ouh_lrd

while [[ $# > 0 ]] ; do
  if [[ "$1" == "--verison" || "$1" == "-v" ]] ; then
    SCHEMA_VERSION="$2"
    shift
    shift
  fi
  if [[ "$1" == "--project" || "$1" == "-p" ]] ; then
    PROJECT_FOLDER="$2"
    shift
    shift
  fi
  if [[ "$1" == "--help" || "$1" == "-h" ]] ; then
    usage
    exit
  fi
done

SCHEMA_FOLDER=$PROJECT_FOLDER/lrd-schemas/core/$SCHEMA_VERSION
CORECLINICAL_SCHEMA_FOLDER=$PROJECT_FOLDER/lrd-schemas/CANCER\ SCHEMAS\ IIP
SOURCE_FOLDER=$PROJECT_FOLDER/lrd-server/src/main/groovy
ROOT_PACKAGE=uk.ac.ox.ndm.lrd.serializer.v${SCHEMA_VERSION//./_}
ROOT_FOLDER=${ROOT_PACKAGE//.//}

echo "Project folder $PROJECT_FOLDER"
echo "Reading schemas from $SCHEMA_FOLDER"
echo "Saving source to $SOURCE_FOLDER"
echo "Using root package $ROOT_PACKAGE"
echo "Using root folder $ROOT_FOLDER"

xjc -no-header -b "$SCHEMA_FOLDER/binding.xjb" -d "$SOURCE_FOLDER" -extension "$SCHEMA_FOLDER/ouh_lrd_message_schema.xsd"

echo "Finished generation. See README.md for further information"
open README.md
