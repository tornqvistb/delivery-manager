cd DeliveryManagerGUI
mvn clean package -DskipTests 
cd ..
cp DeliveryManagerGUI/target/DeliveryManagerGUI*.war C:/Tools/apache-tomcat-8.0.30/apache-tomcat-8.0.30/webapps/DeliveryManagerGUI.war
