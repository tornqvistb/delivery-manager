cd LIMCommons
mvn clean install -DskipTests
cd ../OrderCollector
mvn clean install -DskipTests
cp OrderCollector/target/OrderCollector*.war ../release/OrderCollector.war
