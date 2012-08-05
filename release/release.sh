export JAVA_HOME=/opt/java/jdk1.5.0_22
rm -rf cq-deploy-plugin
svn co http://sourcecontrol.6dlabs.com/svn/sandbox/cq-deploy-plugin/trunk/cq-deploy-plugin/
cd cq-deploy-plugin
mvn release:clean release:prepare
mvn release:perform
cd ..