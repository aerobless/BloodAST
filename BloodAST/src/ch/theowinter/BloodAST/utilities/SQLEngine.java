package ch.theowinter.BloodAST.utilities;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLEngine {
	 // JDBC driver name and database URL
	//static String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static String DB_URL;
	static String USER;
	static String PASS;
	
	//Connection
	 Connection conn = null;
	 
	public SQLEngine(String host, int port, String database, String username, String password) {
		super();
		DB_URL = "jdbc:mysql://"+host+":"+port+"/"+database;
		USER = username;
		PASS = password;
	}
	
	public void setupConnection() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		 conn = DriverManager.getConnection(DB_URL,USER,PASS);
	}
	
	public int insertUpdate(String sqlQuery) throws SQLException, ClassNotFoundException{
		int changes = 0;
		if(conn.isValid(2)){
		    Statement sqlStatement = conn.createStatement();
		    changes = sqlStatement.executeUpdate(sqlQuery);
		}
		else{
			conn.close();
			setupConnection();
		}
		return changes;
	}
	
	public void setupTables(){
			try {
				//Punishment Manager
				insertUpdate("CREATE TABLE pm_warnings ("
						+ "`warn_id` INT( 11 ) NOT NULL ,"
						+ "`warned` VARCHAR( 32 ) NOT NULL ,"
						+ "`warned_by` VARCHAR( 32 ) NOT NULL ,"
						+ "`warn_reason` text NOT NULL ,"
						+ "`warn_time` INT( 11 ) NOT NULL ,"
						+ "`server` VARCHAR( 32 ) NOT NULL"
						+ ") ENGINE = INNODB DEFAULT CHARSET = latin1;");
				//Statisitics Manager
				//TODO: Add correct SQL statement
				
				
				//Testing
				insertUpdate("INSERT INTO pm_warnings VALUES ('1', 'testuser', 'notch', 'testing','1111','testserver');");
			} catch (ClassNotFoundException anEx) {
				anEx.printStackTrace();
			} catch (SQLException anEx) {
				// TODO: Add better error message that gets output through the server's console as a warning.
				anEx.printStackTrace();
			}
	}
}