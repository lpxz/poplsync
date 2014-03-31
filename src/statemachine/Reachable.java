package statemachine;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;


// collective entries, or single entry
// simple traversal, or bounded traversal

public class Reachable {
	public static List emptyList = new ArrayList();
	public static Stack systemStack = new Stack();
    public static Set visited = new HashSet();   
    
	   static Set reachable = new HashSet();
	   static Set backreachable = new HashSet();
	   static Set intersectSet  = new HashSet();

		public static boolean safe(Object o, Set unsafe) {
             return ((State )o).isSafe();
		}
	

	public static void reachableSafe(Machine csG, Object unit, Set unsafe , Set toretSet) {
		
        systemStack.clear();
        visited.clear();
    	if(!visited.contains(unit)&& safe(unit, unsafe))
    	{
    		systemStack.push(unit);	
    	    visited.add(unit);
    	}
	
		while(!systemStack.isEmpty())
		{
		    Object pop =systemStack.pop();	
		    List children =getSafeSuccs(csG, pop ,unsafe);//csG.getLocalSuccs(pop);// ug.getSuccsOf(pop);
		    for(int i = children.size()-1; i>=0; i--)
		    {
		    	Object child = children.get(i);	
		    		    	
		    	
		    	if(!visited.contains(child))
		    	{
		    		//System.out.println(((Place)pop).petriName + "->" + ((Place)child).petriName );		    		
		    	    visited.add(child);
		    		systemStack.push(child);				    		
		    	}
		    }
		    
		}
		toretSet.addAll(visited);
		
	}



	public static List getSafeSuccs(Machine csG, Object pop, Set unsafe) {
		List list = emptyList;// new ArrayList();
		for(Object o : csG.getSuccs(pop))
		{
			if(safe(o, unsafe))
			{
				if(list.equals(emptyList))// the first time, do it
				{
					list = new ArrayList();// lazy initialization, for memory
				}
				
				list.add(o);
			}
		}
		return list;
		
		
	}
	

	public static void backreachableSafe(Machine csG, Object unit, Set unsafe, Set toretSet) {
		
        systemStack.clear();
        visited.clear();
		
    	if(!visited.contains(unit) && safe(unit, unsafe))
    	{
    		systemStack.push(unit);	
    	    visited.add(unit);
    	}
	
		while(!systemStack.isEmpty())
		{
		    Object pop =systemStack.pop();	
		    List children =getSafePrecs(csG, pop, unsafe);//csG.getLocalSuccs(pop);// ug.getSuccsOf(pop);
		    for(int i = children.size()-1; i>=0; i--)
		    {
		    	Object child = children.get(i);			    	
		    	if(!visited.contains(child))
		    	{
		    	    visited.add(child);
		    		systemStack.push(child);				    		
		    	}
		    }
		    
		}
		toretSet.addAll(visited);
		
	}

	public static List getSafePrecs(Machine csG, Object pop,
			Set unsafe) {
		List list = emptyList;// new ArrayList();
		for(Object o : csG.getPrecs(pop))
		{
			if(safe(o, unsafe))
			{
				if(list.equals(emptyList))// the first time, do it
				{
					list = new ArrayList();// lazy initialization, for memory
				}
				
				list.add(o);
			}
		}
		return list;

	}
	
	
	

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	public static State findRoot(Machine machine) {
        for(Object vertex : machine.coreG.vertexSet())
        {
        	if(machine.coreG.incomingEdgesOf(vertex).isEmpty())
        		return (State) vertex;
        }
		
		return null;
	}

}
