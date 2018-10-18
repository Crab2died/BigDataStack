#!/bin/sh

MAIN_CLASS="com.github.crab2died.mapred.wfc.WFCJobSubmit"

if [$1 == 'WFC']; then
  MAIN_CLASS = 'com.github.crab2died.mapred.wfc.WFCJobSubmit'
else
  MAIN_CLASS = 'com.github.crab2died.mapred.wfc.WFCJobSubmit'
fi

hadoop jar mapred-1.0-SNAPSHOT.jar ${MAIN_CLASS}
