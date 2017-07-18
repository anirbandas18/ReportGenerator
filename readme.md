## Java Installation [Skip this if JDK is already installed]
1. Download and install latest [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) according to 32 Bit or 64 Bit architecture which confronts to 32 Bit -> x86 and 64 Bit -> x64
2. Copy the JDK installation directory path which will be along the lines of `[Drive letter]:\Program Files (x86)\Java\jdk1.[version number].[sub version number]_[build number]` for 32 Bit machines and `[Drive letter]:\Program Files\Java\jdk1.[version number].[sub version number]_[build number]` for 64 Bit machines
3. Right click on **My Computer**. Got to **Properties**.
4. Click on **Advanced System Settings** option.
5. Click on **Environment Variables** button.
6. Add a new **System variable** entry with key `JAVA_HOME` and value as the JDK installation directory path from step 2
7. Click on OK
8. Open command prompt.
9. Type `java -version` at the command prompt and hit Enter. It Should show the Java version and build number. This is to verify Java installation on windows. 
	
## Maven Installation
1. Download [Apache Maven](http://www-us.apache.org/dist/maven/maven-3/3.5.0/binaries/apache-maven-3.5.0-bin.zip)
2. Unzip the downloaded binary to a location. 
3. Copy the path to the location of the unzipped maven binaries which will be along the lines of `[Drive letter]:\download\path\apache-maven-[x.y.z]-bin\apache-maven-[x.y.z]`
4. Right click on **My Computer**. Got to **Properties**.
5. Click on **Advanced System Settings** option.
6. Click on **Environment Variables** button.
7. Add a new **System variable** entry with key `M2_HOME` and value as the Maven unzipped binary location path from step 3
8. Add a new **System variable** entry with key `MAVEN_HOME` and value as the Maven unzipped binary location path from step 3
9. Select `Path` enrty under **System variables** 
10. Click on Edit.
11. Append `%M2_HOME%\bin` to the value for `Path` variable's entry
12. Click on OK
13. Open command prompt
14. Type `mvn -version` at the command prompt and hit Enter. It Should show the Maven version. This is to verify Maven installation on windows.

##  Running the Application
1. Open command prompt. Browse to the root directory of the project at command prompt. 
2. Type `mvn -package`. It should start maven build and show at `BUILD SUCCESS` at the bottom of the console output. 
3. Type `run` at the command prompt and follow the on screen instructions to run the application. 

# NOTE : 
1. Enter `mode` value as either **profile** to dump csv reports by Profile names and as **properties** to dump csv reports by properties name.
1. Use the path to the directory `profiles` under the root directory of the project as **Input Directory** value
2. Create a directory as `[mode]_output` before running the `run.bat` file and use the path to `[mode]_output` as **Output Directory** value where `mode` is the value specified in step 1