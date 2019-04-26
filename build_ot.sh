cd LIMCommons
mvn clean install -DskipTests
cd ../OrderTransmitter
mvn clean install -DskipTests
cd ..
cp OrderTransmitter/target/OrderTransmitter*.war /c/Projekt/lim/latestrelease/OrderTransmitter.war
