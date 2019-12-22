export JAVA_AGENT="-javaagent:newrelic.jar"

ap-get install curl

curl -o newrelic.jar https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic.jar
curl -o newrelic.yml https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic.yml


java ${JAVA_AGENT} -jar cashman-rest-service-0.1.0.jar
