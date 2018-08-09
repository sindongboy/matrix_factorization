#!/bin/bash


function usage() {
	echo "usage: $0 [options]"
	echo "-h	help"
	echo "-p	prediction"
	echo "-k	knowns"
	exit 1
}

while test $# -gt 0;
do
	case "$1" in
		-h)
			usage
			;;
		-p)
			shift
			prediction=$1
			shift ;;
		-k)
			shift
			known=$1
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

	java -Xmx4G -Dfile.encoding=UTF-8 -cp ${CP} com.skplanet.nlp.driver.MovieLensEval -p ${prediction} -k ${known}

