export JAVA_AGENT="-javaagent:newrelic.jar"

wget -o newrelic.jar https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic.jar
wget -o newrelic.yml https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic.yml


java ${JAVA_AGENT} -jar cashman-rest-service-0.1.0.jar
