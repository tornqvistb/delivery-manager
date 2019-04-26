cd LIMCommons
mvn clean install -DskipTests
cd ../OrderCollector
mvn clean install -DskipTests
cp OrderCollector/target/OrderCollector*.war ../release/OrderCollector.war
cd ..
cp OrderCollector/target/OrderCollector*.war /c/Projekt/lim/latestrelease/OrderCollector.war