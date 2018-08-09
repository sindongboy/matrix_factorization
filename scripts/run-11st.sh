#!/bin/bash


function usage() {
	echo "usage: $0 [options]"
	echo "-h	help"
	echo "-o	output path [optional]"
	exit 1
}

while test $# -gt 0;
do
	case "$1" in
		-h)
			usage
			;;
		-o)
			shift
			output=$1
			shift ;;
		*)
			break
			;;
	esac
done

RESOURCE="/Users/sindongboy/Dropbox/Documents/resource/11st"
CONFIG="../config"
TARGET=`find ../target/ -type f -name "*" | grep "jar$"`
CP=`find ../libs -type f -name "*" | awk '{printf("%s:", $0);}'`${TARGET}:${CONFIG}:${RESOURCE}

if [[ ! -z ${output} ]]; then
	java -Xmx4G -Dfile.encoding=UTF-8 -cp ${CP} com.skplanet.nlp.driver.ElevenStreetExample -o ${output}
else
	java -Xmx16G -Dfile.encoding=UTF-8 -cp ${CP} com.skplanet.nlp.driver.ElevenStreetExample
fi

