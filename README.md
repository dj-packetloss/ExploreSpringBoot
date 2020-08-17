# ExploreSpringBoot

<pre>
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

Preflight  - The initial run for Maven dependencies will download ~150 MB of jars and may take several minutes.
             Files will land here by default.  (Windows - 'C:\Users\{your user}\.m2\')  (Linux - '~/.m2/')

Command line
   - Within the project's base directory there is a premade "mvnw" command (thanks spring.io)
   - Ensure JAVA_HOME environment variable is set and pointing to Java 8+ JDK
   - Running "mvnw spring-boot:run" from the project's directory will download the initial set of dependencies
   
Eclipse
   - Import the project as a Maven project
   - Right-click project -> Run as -> Run Configurations...  Under Maven Build, select project.  In "Goals:"  plug in "spring-boot:run"
   - To fix eclipse dependencies, in place of "spring-boot:run", use targets "eclipse:clean" and "eclipse:eclipse"   (this can be done on command line as well)
   - Some versions of eclipse are very finicky.


Kept the "hello" mapping that came with spring-boot quickstart.

If spring-boot:run target was successful, a java tomcat container should be running.  Try this url "http://localhost:8080/hello".

Toy java problem #1 - GET "http://localhost:8080/calcRewardsForPurchase"
  Given a pretend purchase amount, return a pretend amount of rewards points.
  Does not do a whole lot outside of what we saw with the hello world example.
  
Toy java problem #2 - GET "http://localhost:8080/calcRewardsForATransaction"
  Accepts a little bit more info than problem #1.
  Example payload is commented.
  Uses standard out for error/debug logging
  Uses jackson json apis to translate request params & response to json.
  Pretends to be a microservice with http response codes
  
Toy java problem #3 - POST to "http://localhost:8080/calcBulkRewards"
  Expands input from problem #2.
  Example post payload GET "http://localhost:8080/bulk.html" - We're using static page content!
  Crossing the streams!
  Attempting to solve common summation + rollup problem using streams
  
</pre>




