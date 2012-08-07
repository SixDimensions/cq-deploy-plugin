#
# Release creation script for CQ Deploy Plugin
# 
# Author: dklco
#
export JAVA_HOME=/opt/java/jdk1.5.0_22
echo "Cleaning Workspace..."
rm -rf cq-deploy-plugin
echo "Checking out TRUNK..."
svn co http://sourcecontrol.6dlabs.com/svn/sandbox/cq-deploy-plugin/trunk/cq-deploy-plugin/
cd cq-deploy-plugin
echo "Preparing release..."
mvn release:clean release:prepare
echo "Performing release..."
mvn release:perform
echo "Release Complete!"
cd ..