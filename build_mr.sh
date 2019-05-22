cd LIMCommons
mvn clean install -DskipTests
cd ../MailReceiver
mvn clean install -DskipTests
cp MailReceiver/target/MailReceiver*.war ../release/MailReceiver.war
cd ..
cp MailReceiver/target/MailReceiver*.war /c/Projekt/lim/latestrelease/MailReceiver.war