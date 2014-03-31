package statemachine;




import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;







public class State {


	
	public static volatile int  counter = 0;
    public String name = null;
    public State(String namepara)
    {
    	name = namepara;
    }
    
    public State()
    {
    	name = "_gen_" + (counter++);
    }
    
	public String toString()
	{
		return name; 
	}
	
	
  
	public boolean safe = true; 
	public void setSafe(boolean safepara)
	{
		safe = safepara;
	}
  
	public boolean isSafe()
	{
		return safe;
	}
    
	static String getNameForState(Object v) {
		
			State state = (State)v;
	        return state.toString();
		
	}

	public static void main(String[] args) {}


}
