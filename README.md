# Introduction #
Simplex solver is an experimental open source standalone desktop application written in Java and is released under the Apache License 2.0. The application uses the [Simplex](https://en.wikipedia.org/wiki/Simplex_algorithm)'s algorithm geometry in order to emulate the solving process of a given linear problem both in 2D and 3D dimensions. Source code and other utilities are included in this repository. This document contains only a short brief summary of the project structure as also some tutorials in how to build and use this software. For more up to date information about the project, changelog and issues, please check the links below.

* [Repository](https://github.com/tzeikob/simplex-solver)
* [Bug Reports](https://github.com/tzeikob/simplex-solver/issues)
* [Contributors](https://github.com/tzeikob/simplex-solver/graphs/contributors)
* [Simplex Algorithm](https://en.wikipedia.org/wiki/Simplex_algorithm)

This software can be used as an educational tool in order to help students understand the theory the Simplex algorithm based on, in order to solve large in scale linear problems. Given a linear problem in textual form, it provides a set of various features like,

* parsing 2D and 3D linear problems,
* solving and modeling linear problems,
* rendering the graphics of linear problem geometry,
* animating the solving process

# Building from Source #
## Prerequisities ##

In order to build this project you need the following software pre installed in your system,

* Java JDK 8+
* Apache Maven 3+
* Git

## External Dependencies ##
Before build the project you must install some required external dependecies into your local maven repository,

```
git clone https://github.com/tzeikob/simplex-solver.git
cd simplex-solver

mvn install:install-file\
   -Dfile=lib/jxl-1.0.jar\
   -DgroupId=com.tkb.lib\
   -DartifactId=jxl\
   -Dversion=1.0\
   -Dpackaging=jar

mvn install:install-file\
   -Dfile=lib/encoder-1.0.jar\
   -DgroupId=com.tkb.lib\
   -DartifactId=encoder\
   -Dversion=1.0\
   -Dpackaging=jar
```

## Build as an Executable ##
Simplex solver currently does not offering any option to download binaries, so in case you want to used it as an executable you have to clone and build it in your system. So following the previous step,

```
mvn clean package
```

In the `target/` forlder you will find the `simplex-solver-<version>.jar` file as well as a folder `lib/`, the classpath containing all the external libraries the project depends on.

## Build as a Library ##
Dispite that the Simplex Solver is aimed to be a standalone desktop application you can use it as an library into your projects as well, so in order to do that you have to clone and install it in your local maven repository. Assuming you already clone it in your disk,

```
mvn clean install
```

for now on you can add it as dependency into other projects, just by adding into the `pom.xml` file the following snippet,

```
<dependency>
 <groupId>com.tkb.lib</groupId>
 <artifactId>simplex-solver</artifactId>
 <version>${version}</version>
</dependency>
```

in the case you want to add it as binary file in the classpath of your project instead as a maven dependency, you will find in the `target/` folder the `simplex-solver-<version>.jar` binary file, just copy and paste it in the classpath of your project, but beaware in that case you have to add also all the binaries the library depends on in the folder `lib/`. It's recommended always to use maven dependencies to avoid any complications.

# How to Use #
## Prerequisities ##
In order to use this software in any platform (win, mac, unix) you must have installed the JRE (Java Runtime Enviroment) into the system and set the JAVA_HOME path as well.

## How to Run ##
Assuming you have already installed a JRE into your system and set the JAVA_HOME, you can run the software by executing the following command into the terminal,

```
java -jar simplex-solver-<version>.jar
```

in the very rare case you did not set the JAVA_HOME enviroment variable, you must locate the folder where the JRE is installed in the hard disk and execute the following command into the terminal,

```
path/to/jre/bin/java -jar simplex-solver-<version>.jar
```

## Examples ##
In the folder `data/` of the repository you can find various examples of linear problems in textual form in order to parse them into the software.
