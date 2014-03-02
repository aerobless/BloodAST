package ch.theowinter.BloodAST.utilities;
import java.sql.*;

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
}