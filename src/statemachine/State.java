package statemachine;




import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;







public class State {

    public Pair corePair =null; 
    public void setCorePair(Pair detailsPairpara)
    {
    	corePair = detailsPairpara; 
    }
    public Pair getCorePair()
    {
    	return corePair;
    }
	
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
    
    
    public void setName(String namepara)
    {
    	name = namepara;
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
    
	public static String getNameForState(Object v) {
		
			State state = (State)v;
			return state.getName();
//	        return state.toString();
		
	}
	public  String getName()
	{
		return name;
	}

	public static void main(String[] args) {}


}
