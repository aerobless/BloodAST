package ch.theowinter.BloodAST;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.theowinter.BloodAST.utilities.LogicEngine;

public class BloodAST_UnitTests {
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
}
