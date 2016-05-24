#!/bin/sh

mvn install:install-file -Dfile=./localdep/jms-1.1.jar \
    -DgroupId=javax.jms -DartifactId=jms -Dversion=1.1 \
    -Dpackaging=jar
