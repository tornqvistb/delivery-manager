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
cp DeliveryManagerGUI/target/DeliveryManagerGUI*.war C:/Projekt/lim/release/DeliveryManagerGUI.war
cp MailReceiver/target/MailReceiver*.war C:/Projekt/lim/release/MailReceiver.war
cp OrderCollector/target/OrderCollector*.war C:/Projekt/lim/release/OrderCollector.war
cp OrderResponseReceiver/target/OrderResponseReceiver*.war C:/Projekt/lim/release/OrderResponseReceiver.war
cp OrderTransmitter/target/OrderTransmitter*.war C:/Projekt/lim/release/OrderTransmitter.war
cp Archiver/target/Archiver*.war C:/Projekt/lim/release/Archiver.war