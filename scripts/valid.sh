#!/bin/bash

function getLR() {
	cat ../config/movielens.properties | grep "^LEARN_RATE" | grep -o "0\.[0-9][0-9]*$"
}

function getRC() {
	cat ../config/movielens.properties | grep "^REG_CONST" | grep -o "0\.[0-9][0-9]*$"
}

function getIT() {
	cat ../config/movielens.properties | grep "^ITER" | grep -o "[0-9][0-9]*$"
}

function getFT() {
	cat ../config/movielens.properties | grep "^FEAT_NUM" | grep -o "[0-9][0-9]*$"
}

function getHalf() {
	echo "scale=4;${1}/2" | bc -l
}

function getQuater() {
	echo "scale=4;${1}/4" | bc -l
}

function add() {
	echo "${1}+${2}" | bc
}

function sub() {
	echo "${1}-${2}" | bc
}

function configUpdate() {
	if [[ ! -f ../config/movielens.properties.bak ]]; then
		cp -vf ../config/movielens.properties ../config/movielens.properties.bak
	fi
	# 1 : learning rate
	# 2 : regularization
	# 3 : iteration
	# 4 : feature num
	pLR="LEARN_RATE=$(getLR)"
	pRC="REG_CONST=$(getRC)"
	pIT="ITER=$(getIT)"
	pFT="FEAT_NUM=$(getFT)"

	cat ../config/movielens.properties | sed 's/'"${pLR}"'/LEARN_RATE='"$1"'/g' | \
		sed 's/'"${pRC}"'/REG_CONST='"$2"'/g' | \
		sed 's/'"${pIT}"'/ITER='"$3"'/g' | \
		sed 's/'"${pFT}"'/FEAT_NUM='"$4"'/g'

}

function configReport() {
	ITER=`cat ../config/movielens.properties | grep "^ITER=" | grep -o "[0-9][0-9]*$"`
	LR=`cat ../config/movielens.properties | grep "^LEARN_RATE=" | grep -o "0\.[0-9][0-9]*$"`
	RC=`cat ../config/movielens.properties | grep "^REG_CONST=" | grep -o "0\.[0-9][0-9]*$"`
	FT=`cat ../config/movielens.properties | grep "^FEAT_NUM=" | grep -o "[0-9][0-9]*$"`

	echo " - iteration : ${ITER}"
	echo " - learning rate : ${LR}"
	echo " - reg. constant : ${RC}"
	echo " - number of features : ${FT}"
}



# for learning rate
for iter in {1..3}
do
	echo "# Configuration : ${iter} #"
	configReport

	./run.sh -o ./u1
	./eval.sh -p u1.predict -k u1.test

	pLR=$(getLR)
	pRC=$(getRC)
	pIT=$(getIT)
	pFT=$(getFT)

	#nLR="0$(getHalf ${pLR})"
	#nLR="0$(getQuater ${pLR})"
	#nLR="0$(add ${pLR} 0.001)"
	nFT="0$(sub ${pFT} 5)"
	echo "$(configUpdate ${pLR} ${pRC} ${pIT} ${nFT})" > ../config/movielens.properties
done

# restore properties
cp ../config/movielens.properties.bak ../config/movielens.properties

