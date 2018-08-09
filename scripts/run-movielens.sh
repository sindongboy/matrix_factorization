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

RESOURCE="/Users/sindongboy/Dropbox/Documents/resource/matrix-factorization/movielens/100K"
CONFIG="../config"
TARGET=`find ../target/ -type f -name "*" | grep "jar$"`
CP=`find ../libs -type f -name "*" | awk '{printf("%s:", $0);}'`${TARGET}:${CONFIG}:${RESOURCE}

if [[ ! -z ${output} ]]; then
	#java -Xmx4G -Dfile.encoding=UTF-8 -cp ${CP} com.skplanet.nlp.driver.MovieLensExample -o ${output}
	java -Xmx4G -Dfile.encoding=UTF-8 -cp ${CP} com.skplanet.nlp.driver.MovieLensSVDPlusPlusExample -o ${output}
else
	#java -Xmx4G -Dfile.encoding=UTF-8 -cp ${CP} com.skplanet.nlp.driver.MovieLensExample
	java -Xmx4G -Dfile.encoding=UTF-8 -cp ${CP} com.skplanet.nlp.driver.MovieLensSVDPlusPlusExample
fi

