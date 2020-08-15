# ExploreSpringBoot

Preflight - You need Java JDK 8+ installed.  I used Windows x64 Java 8u261

First Spring Boot project
First Maven Project

Base project created from Spring community's quickstart - https://spring.io/quickstart

Explore some built in features of Spring + spring-boot
Explore some features of Java 8 streams

settings.xml 
  - Intended as Maven settings.xml  (Windows - 'C:\Users\{your user}\.m2\settings.xml')  (Linux - '~/.m2/settings.xml')
  - Ran into an issue with the quickstart project where I needed to set maven central repo to "https://"  instead of default "http://"
  - This is a minimalist file.


Downloading dependencies with Maven

Command line
   - Within the project's base directory there is a premade "mvnw" command (thanks spring.io)
   - Running "mvnw spring-boot:run" from the project's directory will download the initial set of dependencies
   
Eclipse
   - Import the project as a Maven project
   - Right-click project -> Run as -> Run Configurations...  Under Maven Build, select project.  In "Goals:"  plug in "spring-boot:run"
   - To fix eclipse dependencies, in place of "spring-boot:run", use targets "eclipse:clean" and "eclipse:eclipse"   (this can be done on command line as well)





