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
cp DeliveryManagerGUI/target/DeliveryManagerGUI*.war ../release/DeliveryManagerGUI.war
cp MailReceiver/target/MailReceiver*.war ../release/MailReceiver.war
cp OrderCollector/target/OrderCollector*.war ../release/OrderCollector.war
cp OrderResponseReceiver/target/OrderResponseReceiver*.war ../release/OrderResponseReceiver.war
cp OrderTransmitter/target/OrderTransmitter*.war ../release/OrderTransmitter.war
cp Archiver/target/Archiver*.war ../release/Archiver.war