unzip -q tdgssconfig.jar
unzip -q -o terajdbc4.jar
javac TD.java
jar cfm XTD.jar manifest.txt *.class com TdgssUserConfigFile.xml
rm -rf META-INF 
rm -rf com
rm TdgssUserConfigFile.xml
rm TD.class
