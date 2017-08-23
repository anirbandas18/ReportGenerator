@echo off
set /p mode= Provide run mode 'profile' or 'property' : 
set /p input= Input directory path : 
set /p output= Output directory path : 
set /p property= Filter on properties (csv representation optional) :
java -Xms1024m -Xmx1024m -jar target/ReportGenerator-0.1.1-SNAPSHOT.jar %mode% %input% %output% %property%
pause