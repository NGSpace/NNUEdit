::Go out to the main directory
cd ..




echo:
echo Building windows JAR

call mvn clean compile assembly:single -P windows
move target\NNUEdit.jar build\jars\windows.jar

echo Finished building windows JAR



echo Building linux JAR

call mvn clean compile assembly:single -P linux
move target\NNUEdit.jar build\jars\linux.jar

echo Finished building linux JAR



echo Building macos JAR

call mvn clean compile assembly:single -P macos
move target\NNUEdit.jar build\jars\macos.jar

echo Finished building macos JAR



echo Building universal JAR

call mvn clean compile assembly:single -P universal
move target\NNUEdit.jar build\jars\universal.jar

echo Finished building universal JAR




::Go back
cd build