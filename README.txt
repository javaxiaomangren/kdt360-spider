=====project description
build project

mvn clean;
mvn -Pproduction assembly:assembly  -Dmaven.test.skip=true

tar -zxvf *-bin.tar.gz

sh run.sh > /dev/null &
