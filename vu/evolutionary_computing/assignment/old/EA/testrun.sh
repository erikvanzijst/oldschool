#!/bin/bash
LOGFILE=testrun.log

rm $LOGFILE 2>/dev/null
for a in 1 2 3 4 5 6 7 8 9 10
do
	java -classpath EA2002.jar:. MyEA MyEA.config >> $LOGFILE
	echo
	tail -1 $LOGFILE | cut -c10- | cut -f1 -d '.'
done                          
echo
echo "Results are: "
cat $LOGFILE | grep bestval | cut -c10- | cut -f1 -d '.'
echo
echo "Throw away the smallest one and average result to get your grade"
