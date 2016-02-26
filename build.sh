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
cd ..
