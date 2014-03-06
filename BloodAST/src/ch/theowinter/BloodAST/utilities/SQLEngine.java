package ch.theowinter.BloodAST.utilities;

//TODO: not sure if sql or jdbc imports are better.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class SQLEngine {
	 // JDBC driver name and database URL
	//static String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static String DB_URL;
	static String USER;
	static String PASS;
	static String databaseName;
	
	//Connection
	 Connection conn = null;
	 
	public SQLEngine(String host, int port, String database, String username, String password) throws Exception {
		super();
		DB_URL = "jdbc:mysql://"+host+":"+port+"/"+database;
		USER = username;
		PASS = password;
		databaseName = database;
		setupConnection();
	}
	
	private void setupConnection() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		 conn = DriverManager.getConnection(DB_URL,USER,PASS);
	}
	
	public int insertUpdate(String sqlQuery) throws SQLException{
		int changes = 0;
		if(conn.isValid(2)){
		    Statement sqlStatement = conn.createStatement();
		    changes = sqlStatement.executeUpdate(sqlQuery);
		}
		else{
			conn.close();
			try {
				setupConnection();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return changes;
	}

	public String queryCreator(String tableName, ArrayList<String[]> data){
		
		//TODO: make inserted variables injection-safe
		
		StringBuilder strConstruct = new StringBuilder();
		strConstruct.append("INSERT INTO '"+databaseName+"'.'"+tableName+"' (");
		
		for(int i=0; i<(data.size()); i++){
			strConstruct.append("'"+data.get(i)[0]+"'");
			if(i!=(data.size()-1)){
				strConstruct.append(", ");	
			}
		}
		
		strConstruct.append(") VALUES(");

		for(int i=0; i<(data.size());i++){
			strConstruct.append("?");
			if(i!=(data.size()-1)){
				strConstruct.append(", ");	
			}
		}
		strConstruct.append(");");	
		
		System.out.println(strConstruct);
		
		return strConstruct.toString();
	}
	
	public int insertIntoTable(String inputQuery) throws SQLException{
		PreparedStatement insertStatement;
		
		insertStatement = conn.prepareStatement(inputQuery);
		//insertStatement.set

		//TODO: Query Builder
		//TODO: convert map into query
		return 1;
	}
	
	public void setupTables(){
			try {
				//Punishment Manager
				insertUpdate("CREATE TABLE IF NOT EXISTS pm_warnings ("
						+ "`warn_id` INT( 11 ) NOT NULL AUTO_INCREMENT,"
						+ "`warned` VARCHAR( 32 ) NOT NULL,"
						+ "`warned_by` VARCHAR( 32 ) NOT NULL,"
						+ "`warn_reason` text NOT NULL,"
						+ "`warn_time` INT( 11 ) NOT NULL,"
						+ "`server` VARCHAR( 32 ) NOT NULL,"
						+ "PRIMARY KEY (`warn_id`)"
						+ ") ENGINE = INNODB DEFAULT CHARSET = latin1 AUTO_INCREMENT=5;");
				//Statisitics Manager
				//TODO: Add correct SQL statement
				//Testing
				//insertUpdate("INSERT INTO pm_warnings VALUES ('' 'testuser', 'notch', 'testing','1111','testserver');");
			} catch (SQLException anEx) {
				// TODO: Add better error message that gets output through the server's console as a warning.
				anEx.printStackTrace();
			}
	}
	
	public String getDBName(){
		return databaseName;
	}
	
}