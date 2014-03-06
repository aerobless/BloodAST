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

	public String insertQueryGenerator(String tableName, ArrayList<String[]> data){
		StringBuilder strConstruct = new StringBuilder();
		strConstruct.append("INSERT INTO "+tableName+" (");
		
		//Create the fields row
		for(int i=0; i<(data.size()); i++){
			strConstruct.append("`"+data.get(i)[0]+"`");
			if(i!=(data.size()-1)){
				strConstruct.append(", ");	
			}
			else{
				strConstruct.append(") VALUES(");
			}
		}
		
		//Create the values row with "?" for the prepared statement
		for(int i=0; i<(data.size());i++){
			strConstruct.append("?");
			if(i!=(data.size()-1)){
				strConstruct.append(", ");	
			}
			else{
				strConstruct.append(");");
			}
		}
		return strConstruct.toString();
	}

	public boolean runPreparedStatement(String inputQuery, ArrayList<String[]> data) throws SQLException{
		 PreparedStatement statement = null;
		 statement =  conn.prepareStatement(inputQuery);
		 for(int i=0; i<data.size(); i++){
			 //prep statement insertion starting at 1
			 statement.setString(i+1, data.get(i)[1]); 
		 }
		return statement.execute();
	}	
	public boolean setupTables() throws ClassNotFoundException, SQLException{

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
				return true;
	}
	
	public String getDBName(){
		return databaseName;
	}
	
}