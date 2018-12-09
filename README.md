#If you develop locally you need to install these jars into your local maven repo
mvn install:install-file -Dfile=${basedir}\lib\cesiumlanguagewriter-2.10.0.jar -DgroupId=agi.foundation -DartifactId=cesiumlanguagewriter -Dversion=2.10.0 -Dpackaging=jar
mvn install:install-file -Dfile=${basedir}\lib\math-1.0-SNAPSHOT.jar -DgroupId=gov.sandia.phoenix -DartifactId=math -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=${basedir}\lib\astrodynamics-1.0-SNAPSHOT.jar -DgroupId=gov.sandia.phoenix -DartifactId=astrodynamics -Dversion=1.0 -Dpackaging=jar