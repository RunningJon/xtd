********************************************************************************
** Author: Jon Roberts
** Email: jon.roberts@emc.com and greenplumguru@gmail.com
** Date: 2011-12-01
** Application Name: XTD
** Description: eXtract TeraData data using FASTEXPORT via JDBC driver

********************************************************************************
** Support
********************************************************************************
XTD is a free application with zero support from the author or from EMC.  The 
application is intended to demonstrate a way to get data from TeraData to be 
used with Greenplum External Web Tables.  

********************************************************************************
** Full Description
********************************************************************************
XTD uses TeraData's JDBC driver to connect to a TeraData instance.  The
connection is done using UTF8 and with the FASTEXPORT option.

The SQL query provided is then executed resulting in a Java ResultSet.  This 
ResultSet is then opened and each column has some filtering done to it to 
ensure proper output.  The filtering includes adding an escape "\" before each
"\" and "|" character, carriage return and new line characters are replaced with
a space and lastly, NULL characters are removed.

After the data cleansing, output is created by delimiting each field with a "|"
character.

********************************************************************************
** Files Included
********************************************************************************
1. README.txt: this file
2. TD.java: Java code
3. manifest.txt
4. compile.sh

********************************************************************************
** File Created
********************************************************************************
1. XTD.jar

********************************************************************************
** Installation
********************************************************************************
1. Download TeraData JDBC drivers from here:
http://downloads.teradata.com/download/connectivity/jdbc-driver
2. Extract the two Jar files from the zip or tar.
3. Put the Jar files (tdgssconfig.jar and terajdbc4.jar) in the same directory
as the files included.
4. Execute compile.sh
5. Copy the new XTD.jar file created to /data on the MASTER server.

********************************************************************************
** Usage
********************************************************************************
XTD expects 6 parameters in this order:
1. Server name
2. Database name
3. User name
4. Password
5. SQL Query
6. FastExport

Note: use double quotes as needed and escape special characters with \.

********************************************************************************
** Java Example
********************************************************************************
Notes for example: 
	server (host) name is edw
	database name is prod
	user name is scott
	password it tiger
	sql is "SELECT * FROM CUSTOMER"
	1 for FastExport and 0 to be disabled

java -jar /data/XTD.jar edw prod scott tiger "SELECT * FROM CUSTOMER" 1

********************************************************************************
** Greenplum Example
********************************************************************************
CREATE EXTERNAL WEB TABLE TD_CUSTOMER
(
  id INT,
  fname VARCHAR,
  lname VARCHAR
)

 EXECUTE E'java -jar /data/XTD.jar edw prod scott tiger "SELECT id, fname, lname FROM CUSTOMER" 1' 
 ON MASTER FORMAT 'text' (delimiter '|' null 'null' escape E'\\');
