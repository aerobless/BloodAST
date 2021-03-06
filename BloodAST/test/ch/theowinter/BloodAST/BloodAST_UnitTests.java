package ch.theowinter.BloodAST;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import ch.theowinter.BloodAST.modules.PunishmentManager;
import ch.theowinter.BloodAST.utilities.LogicEngine;
import ch.theowinter.BloodAST.utilities.SQLEngine;

public class BloodAST_UnitTests {
	//Switches
	boolean runDBTests = true;
	
		@Test
		public void testConcatenateArgs(){
			LogicEngine command = new LogicEngine();
			
			//Test with array longer then what we want.
			String[] inputArgsTest1 = new String[6];
			inputArgsTest1[0] = "herobrine";
			inputArgsTest1[1] = "Warned";	
			inputArgsTest1[2] = "for";
			inputArgsTest1[3] = "griefing";
			inputArgsTest1[4] = "a";
			inputArgsTest1[5] = "house.";
			String[] controlArray = new String[2];
			controlArray[0] = "herobrine";
			controlArray[1] = "Warned for griefing a house.";
			String[] outputArgs = command.concatenateArgs(inputArgsTest1, 2);	
			assertArrayEquals(controlArray, outputArgs);
			
			//Test with array same length we want.
			String[] inputArgsTest2 = new String[2];
			inputArgsTest2[0] = "aerobless";
			inputArgsTest2[1] = "tests his methods thoroughly :D.";
			assertArrayEquals(inputArgsTest2, command.concatenateArgs(inputArgsTest2, 2));
			
			//Test with array shorter then what we want.
			String[] inputArgsTest3 = new String[1];
			inputArgsTest3[0] = "MySpacebarIsBroken";
			assertArrayEquals(inputArgsTest3, command.concatenateArgs(inputArgsTest3, 2));
		}
		
		/**
		 * To run this test you need to have a local mysql-server running on port 8889
		 * with a database "test", and a user "test with password "test". (You can use
		 * MAMP on OS X to easily run a mysql server for this.)
		 */
		@Test
		public void testSQLEngine(){
			if(runDBTests){
				SQLEngine sql;
				int insertedRows = 0;
				try {
					sql = new SQLEngine("localhost", 8889, "test", "testUser", "password");
					sql.insertUpdate("DROP TABLE IF EXISTS test; ");
					sql.insertUpdate("CREATE TABLE  test ("
							+ "`Test1` INT( 11 ) NOT NULL ,"
							+ "`test2` INT( 11 ) NOT NULL ,"
							+ "`teste3` INT( 11 ) NOT NULL ,"
							+ "`test4` INT( 11 ) NOT NULL"
							+ ") ENGINE = INNODB DEFAULT CHARSET = latin1;");
					insertedRows = sql.insertUpdate("INSERT INTO test VALUES ('1', '2', '3', '4');");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				assertEquals(insertedRows, 1);
			}
			else{
				System.out.println("You've disabled the database tests.");
			}
		}
		
		/**
		 * Testing the SQL table setup routine.
		 */
		@Test
		public void testTableSetup(){
			if(runDBTests){
				boolean success = false;
				SQLEngine sql;
				try {
					sql = new SQLEngine("localhost", 8889, "test", "testUser", "password");
					success = sql.setupTables();
				} catch (Exception e) {
					e.printStackTrace();
				}
				assertTrue(success);
			}
			else{
				System.out.println("You've disabled the database tests.");
			}
		}
		
		@Test
		public void testLogWarningToDB(){
			PunishmentManager punish;
			try {
				punish = new PunishmentManager(null, new SQLEngine("localhost", 8889, "test", "testUser", "password"));
				punish.logWarning("aerobless", "Herobrine", "not griefing fast enough", "bCloud");
			} catch (Exception anEx) {
				// TODO Auto-generated catch block
				anEx.printStackTrace();
			}
		}
		
		@Test
		public void testQueryCreator(){
			if(runDBTests){
				SQLEngine sql;
				try {
					sql = new SQLEngine("localhost", 8889, "test", "testUser", "password");
					ArrayList<String[]> testData = new ArrayList<String[]>();
					testData.add(new String[] {"name","aerobless"});
					assertEquals("INSERT INTO test-table (`name`) VALUES(?);", sql.insertQueryGenerator("test-table", testData));
					
					ArrayList<String[]> testData2 = new ArrayList<String[]>();
					testData2.add(new String[] {"name","aerobless"});
					testData2.add(new String[] {"name2","eletiy"});
					assertEquals("INSERT INTO test-table (`name`, `name2`) VALUES(?, ?);", sql.insertQueryGenerator("test-table", testData2));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		@Test
		public void testRunPreparedStmnt(){
			if(runDBTests){
				SQLEngine sql;
				try {
					sql = new SQLEngine("localhost", 8889, "test", "testUser", "password");
					ArrayList<String[]> testData2 = new ArrayList<String[]>();
					testData2.add(new String[] {"Test1","11"});
					testData2.add(new String[] {"Test2","22"});
					testData2.add(new String[] {"Teste3","33"});
					testData2.add(new String[] {"Test4","44"});
					String sqlQuery = sql.insertQueryGenerator("test", testData2);
					sql.runPreparedStatement(sqlQuery, testData2);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
}
