cd LIMCommons
mvn clean install -DskipTests
cd ../DeliveryManagerGUI
mvn clean package -DskipTests 
cd ../MailReceiver
mvn clean package -DskipTests 
cd ../OrderCollector
mvn clean package -DskipTests 
cd ../OrderResponseReceiver
mvn clean package -DskipTests 
cd ../OrderTransmitter
mvn clean package -DskipTests 
cd ../Archiver
mvn clean package -DskipTests 
cd ..
cp DeliveryManagerGUI/target/DeliveryManagerGUI*.war ../../lanteam/release/DeliveryManagerGUI.war
cp MailReceiver/target/MailReceiver*.war ../../lanteam/release/MailReceiver.war
cp OrderCollector/target/OrderCollector*.war ../../lanteam/release/OrderCollector.war
cp OrderResponseReceiver/target/OrderResponseReceiver*.war ../../lanteam/release/OrderResponseReceiver.war
cp OrderTransmitter/target/OrderTransmitter*.war ../../lanteam/release/OrderTransmitter.war
cp Archiver/target/Archiver*.war ../../lanteam/release/Archiver.war