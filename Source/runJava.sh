#! /bin/bash

javac *.java

testspath='../Tests/Tests/'
checkerpath='../Tests/Checking/'
partlistpath='../Tests/'

errorList=""

cat "${partlistpath}partList" | while read line; do
	part=($line)
	testfolder="$testspath${part[0]}"
	programname="${part[1]}"
	checkerprogram="$checkerpath${part[2]}"

	read args
	read chargs

	if [ -f "$programname.java" ]; then
		echo "Running tests for $programname"
		for fd in "$testfolder"*.dat; do
			f=${fd%.dat}
			echo -n "${f##*/}: "

			if [ -f "$f.out" ]; then
				rm $f.out
			fi

			arglist=()
			for arg in $args; do
				arglist+=($f.$arg)
			done

			chlist=()
			for arg in $chargs; do
				chlist+=($f.$arg)
			done

			java $programname ${arglist[@]} > /dev/null


			result=`$checkerprogram ${chlist[@]}`
			echo $result

			if [ $result != "Correct" ]; then
				errorList="$errorList$f"
			fi
		done
	else
		errorList="$errorList$programname"
		echo "Program $programname missing"
	fi
done

if [ -n $errorList ]; then
	exit 1
fi