#!/bin/sh
MNTPREFIX=/mnt/
OLDFS=oldfs
NEWFS=newfs
SEDEXPRESSION=s/$OLDFS/$NEWFS/
for oldfile in `find $MNTPREFIX$OLDFS -type d -o -type f`
do
	newfile=`echo $oldfile | sed $SEDEXPRESSION`
	oldcksum=`cksum $oldfile | cut -f 1,2 -d ' '`
	newcksum=`cksum $newfile | cut -f 1,2 -d ' '`
	if [ "$oldcksum" != "$newcksum" ]; then
		echo $oldfile \($oldcksum\) does not equal $newfile \($newcksum\)
		exit 1
	fi
done
echo all files ok
