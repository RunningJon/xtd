import java.sql.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.Calendar;

public class TD
{

	private static String myclass = "TD";
	public static boolean debug = false;

	public static void main(String[] args) throws SQLException
	{
		String method = "main";
		int location = 1000;
		String sourceServer = args[0];
		String sourceDatabase = args[1];
		String sourceUser = args[2];
		String sourcePass = args[3];
		String sql = args[4];
		String fastExport = args[5];
		

		if (debug)
		{
			System.out.println("sourceServer:" + sourceServer);
			System.out.println("sourceDatabase:" + sourceDatabase);
			System.out.println("sourceUser: " + sourceUser);
			System.out.println("sourcePass " + sourcePass);
			System.out.println("sql: " + sql);
			System.out.println("fastexport: " + fastExport);
		}
	
		try
		{
			location = 2000;	
			if (debug)	
				printMsg("trying....");	

			location = 2100;
			Connection conn = connectTD(sourceServer, sourceDatabase, sourceUser, sourcePass, fastExport);

			location = 2200;
			//execute the SQL Statement
			outputData(conn, sql);

			location = 2300;
			conn.close();

			if (debug)
				printMsg("it worked!");
		}
		catch (SQLException ex)
		{
			throw new SQLException("(" + myclass + ":" + method + ":" + location + ":" + ex.getMessage() + ")");
		}
	}

	private static void outputData(Connection conn, String strSQL) throws SQLException
	{
		String method = "outputData";
		int location = 1000;

		try
		{
			location = 2000;
			if (debug)
				printMsg("Executing SQL: " + strSQL);

			location = 2100;
			if (debug)
				printMsg("preparing statement");
			PreparedStatement ps = conn.prepareStatement(strSQL);

			location = 2200;
			if (debug)
				printMsg("getting result set");
			ResultSet rs = ps.executeQuery();

			//Statement stmt = conn.createStatement();
			//ResultSet rs = stmt.executeQuery(strSQL);

			location = 2300;
			if (debug)
				printMsg("getting metadata");
			ResultSetMetaData rsmd = rs.getMetaData();

			location = 2400;
			if (debug)
				printMsg("getting number of columns");
			int numberOfColumns = rsmd.getColumnCount();

			String output;
			String columnValue = "";

			location = 2500;
			if (debug)
				printMsg("start looping through results");
			while (rs.next())
			{
				output="";
				// Get the column names; column indices start from 1
				for (int i=1; i<numberOfColumns+1; i++)
				{
					//Filter out \ and | from the columnValue for not null records.  the rest will default to "null"
					columnValue = rs.getString(i);
					if (columnValue != null)
					{
						columnValue = columnValue.replace("\\", "\\\\");
						columnValue = columnValue.replace("|", "\\|");
						columnValue = columnValue.replace("\r", " ");
						columnValue = columnValue.replace("\n", " ");
						columnValue = columnValue.replace("\0", "");
					}

					if (i == 1)
						output = columnValue;
					else
						output = output + "|" + columnValue;
				}

				System.out.println(output);
			}
		}

		catch (SQLException ex)
		{

			//throw new SQLException("(" + myclass + ":" + method + ":" + location + ":" + ex.getMessage() + ")");

			String exceptionMessage = ex.getMessage();
			SQLException nextException = ex.getNextException();

			while (nextException != null)
			{
				exceptionMessage = exceptionMessage + " " + nextException.getMessage();
				nextException = nextException.getNextException();
			}

			throw new SQLException("(" + myclass + ":" + method + ":" + location + ":" + exceptionMessage + ")");
		}

	}

	private static Connection connectTD(String sourceServer, String sourceDatabase, String sourceUser, String sourcePass, String fastExport) throws SQLException
	{
		String method = "connectTD";
		int location = 1000;

		try
		{
			try
			{
				location = 2000;
				Class.forName("com.teradata.jdbc.TeraDriver");
			}
			catch (Exception e)
			{
				throw new SQLException("(" + myclass + ":" + method + ":" + location + ":" + e.getMessage() + ")");
			}

			String connectionUrl = null;

			location = 3000;
			connectionUrl = "jdbc:teradata://" + sourceServer + "/database=" + sourceDatabase + ",charset=UTF8";

			location = 3100;

			if (fastExport.equals("1")) 
			{
				connectionUrl = connectionUrl + ",type=FASTEXPORT";
			}

			location = 4000;
			Connection conn = connect(connectionUrl, sourceUser, sourcePass);

			location = 4100;
			int transactionIsolationLevel = Connection.TRANSACTION_READ_UNCOMMITTED;

			location = 4200;
			conn.setTransactionIsolation(transactionIsolationLevel);
	
			location = 4300;
			return conn;
		}
		catch (SQLException ex)
		{
			throw new SQLException("(" + myclass + ":" + method + ":" + location + ":" + ex.getMessage() + ")");
		}
	}

	private static Connection connect (String connectionUrl, String userName, String password) throws SQLException
	{
		String method = "connect";
		int location = 1000;

		try
		{
			location = 2000;
			Connection conn = DriverManager.getConnection(connectionUrl, userName, password);

			location = 2100;
			return conn;
		}
		catch (SQLException ex)
		{
			throw new SQLException("(" + myclass + ":" + method + ":" + location + ":" + ex.getMessage() + ")");
		}
	}

	private static void printMsg(String myMsg) 
	{
		Calendar now = Calendar.getInstance();
			
		System.out.println(now.getTime() + ";" + myMsg);

	}
}


