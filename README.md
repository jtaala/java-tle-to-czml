# README #

### Description ###

* A library to create CZML for satellites.
* 0.1
* Author:
	Ryan Pelletier

## How To ##


The build files used are pretty simple, and are also documented well. In order to run the project follow the steps below. *First make sure node and npm are installed and up to date*

1.  **git clone** https://github.com/ryanp102694/tank-game.git
1.  **cd** tank-game
1.  **npm install**
1.  **npm start**
1.  *Browse to localhost:8080*







#If you develop locally you need to install these jars into your local maven repo
mvn install:install-file -Dfile=${basedir}\lib\cesiumlanguagewriter-2.10.0.jar -DgroupId=agi.foundation -DartifactId=cesiumlanguagewriter -Dversion=2.10.0 -Dpackaging=jar
mvn install:install-file -Dfile=${basedir}\lib\math-1.0-SNAPSHOT.jar -DgroupId=gov.sandia.phoenix -DartifactId=math -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=${basedir}\lib\astrodynamics-1.0-SNAPSHOT.jar -DgroupId=gov.sandia.phoenix -DartifactId=astrodynamics -Dversion=1.0 -Dpackaging=jar
