#!/usr/bin/bash

RAWFILEDIR=/prod/rawdata

export CLASSPATH=/home/ec2-user/.m2/repository/com/google/code/gson/gson/2.8.6/gson-2.8.6.jar:/home/ec2-user/.m2/repository/org/json/json/20190722/json-20190722.jar:/prod/medline/target/medline-1.0-SNAPSHOT.jar

aws s3 ls s3://medlinexmlzip | head -10 |awk '{print $4}' > $RAWFILEDIR/zipFileList.txt

message () {
        timestamp=`date +"%Y-%m-%d %H:%M:%S"`
        echo "[$timestamp] - $1"
}

while IFS= read -r line
do
    file=$line

    message "start parseing - $file"
    aws s3 cp s3://medlinexmlzip/$file $RAWFILEDIR/$file >/dev/null 2>&1
    message "pulled from s3"
    gunzip $RAWFILEDIR/$file 2>/dev/null
    message "gunzip completed"

    xmlFile=`echo $RAWFILEDIR/$file | cut -d'.' -f1-2`

    message  "Parsing $xmlFile starting..."
    java -Xms3g -Xmx3g parser.parseMedlineFile $xmlFile
    status=$?
    if [ $status -ne 0 ] ; then
            message "Parsing Exception in $xmlFil."
            rm -rf $xmlFile
            rm -rf $RAWFILEDIR/$file
    fi
    message "Parsing - $xmlFile completed."
    jsonDIRFile=`echo $RAWFILEDIR/$file | cut -d'.' -f1`.json
    jsonFile=`echo $jsonDIRFile | cut -d'/' -f4`

    message "$jsonFile is  created"

    gzip -c $jsonDIRFile > $jsonDIRFile.gz
    aws s3 cp  $jsonDIRFile.gz  s3://medlinejson/$jsonFile.gz >/dev/null 2>&1
    status=$?
    if [ $status -eq 0 ] ; then
            message "Copy to S3 - $jsonFile.gz completed."
    fi
    rm -rf $xmlFile
    rm -rf $RAWFILEDIR/$file
    rm -rf $jsonDIRFile
    rm -rf $jsonDIRFile.gz

    message "parssing $file is completed"
    message "Index ready gzipped json - $jsonFile.gz is generated in s3"


done < $RAWFILEDIR/zipFileList.txt