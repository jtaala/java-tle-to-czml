# README #

### Description ###

* A library to create CZML for satellites.
* 0.1
* Author:
	Ryan Pelletier

## How To ##

### Use ###

### Develop ###
 *If you develop locally you need to install these jars from the project's lib directory into your local maven repo*
1. mvn install:install-file -Dfile=${basedir}\lib\cesiumlanguagewriter-2.10.0.jar -DgroupId=agi.foundation -DartifactId=cesiumlanguagewriter -Dversion=2.10.0 -Dpackaging=jar
1. mvn install:install-file -Dfile=${basedir}\lib\math-1.0-SNAPSHOT.jar -DgroupId=gov.sandia.phoenix -DartifactId=math -Dversion=1.0 -Dpackaging=jar
1. mvn install:install-file -Dfile=${basedir}\lib\astrodynamics-1.0-SNAPSHOT.jar -DgroupId=gov.sandia.phoenix -DartifactId=astrodynamics -Dversion=1.0 -Dpackaging=jar
