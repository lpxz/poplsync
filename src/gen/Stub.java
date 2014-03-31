package gen;

import statemachine.Machine;
import statemachine.State;

public class Stub {

	
	public static void loadSM(String filenString)
	{
		Machine.loadSM(filenString);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static void updateSM(String opname) {
		 Machine.machine.updateSM(opname);
	}

}
