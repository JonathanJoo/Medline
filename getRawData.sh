#!/usr/bin/bash
input="index.txt"
while IFS= read -r line
do
        filename=`basename $line`
        wget -O /prod/rawdata/temp $line
        aws s3 cp /prod/rawdata/temp s3://medlinexmlzip/$filename
done < $input