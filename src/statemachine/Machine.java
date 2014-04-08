package statemachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;




import org.jgrapht.graph.DirectedPseudograph;

import test.Test_Initial_Attempt;

public class Machine {

	public static  Machine machine = null;
	
	
    public State currentState = null;
	
	
	public DirectedPseudograph<Object, Transition> coreG = null;
	public Machine()
	{
		coreG = new DirectedPseudograph<Object, Transition>(
				Transition.class);
	}
	
	public static void loadSM(String filenString)
	{
       machine = MachineImporter.importSM(filenString);
       State root =Reachable.findRoot(machine);
	   machine.currentState = root; 
	}
	
	
	// should be atomic!
	public synchronized void updateSM(String op)
	{
		State curr = currentState; 
		Set<Transition> outTs =coreG.outgoingEdgesOf(curr);
		for(Transition t: outTs)
		{
			if(t.getOpName().equals(op))
			{
				currentState = (State)t.getTarget();
				System.out.println("" + curr + " updated to " + currentState);
				
				//manual hack for control
				if(currentState.toString().equals("E"))
				{
					Test_Initial_Attempt.map.put(Test_Initial_Attempt.key, Test_Initial_Attempt.key);
					currentState = this.mappings.get("F"); 
				}
				// manual control
				
				
		        return ;
			}
		}
		return ;
		
	}
	
	
	
	
	public List list = new ArrayList();

	public List getSuccs(Object pop) {
		list.clear();
		Set<Transition> edges  =this.coreG.outgoingEdgesOf(pop);
		for(Transition edge : edges)
		{
			{ list.add(edge.getTarget());}
		}			
		return list;
	}
	
	public List getPrecs(Object pop)
	{
		list.clear();
		Set<Transition> edges  =this.coreG.incomingEdgesOf(pop);
		for(Transition edge : edges)
		{
			{ list.add(edge.getSource());}
		}			
		return list;
	}
	
	
	
	public static void main(String[] args) {
		
	}

	// assertion: the key must already be there!
	public Transition addEdge(String start, String end, Map attributeMap) {
		State startS = mappings.get(start);
		State endS = mappings.get(end);
		Transition transition = coreG.addEdge_edgetype_lpxz(startS, endS,  Transition.class);

		if(attributeMap!=null){
			Object opname = attributeMap.get(Keywords.op); 
			if(opname!=null)
				transition.setOpName((String)opname);
		}
		return transition;
	}

	public boolean in(String start, String end) {
		State startS = mappings.get(start);
		State endS = mappings.get(end);
		return coreG.containsEdge(startS, endS);
	}
	
	public Transition addEdgeIfAbsent(String start, String end)// return the edge that you add, different from j.u.c semantic
	{
		if(!in(start,end))
		{
			return addEdge(start, end, null);
		}
		return null;
	}

	public HashMap<String,State> mappings  = new HashMap<String, State>();
	public void addNode(String part, State state , Map attributeMap) {
        

         if(attributeMap!=null){
	         Object safeorNot = attributeMap.get(Keywords.safe);
	         if(safeorNot!=null)
	         {
	        	 
	        	 if(safeorNot.equals("false"))
	        		 state.setSafe(false);
	        	 else {
					state.setSafe(true);
				}
	         }
         }
         
         mappings.put(part, state);
         coreG.addVertex(state);
	}
	

	public boolean in(String part) {
		return mappings.containsKey(part);
	}

	public void addNodeIfAbsent(String part, State state) // shortcut
	{
		if(!in(part))
		{
			addNode(part, state, null);
		}
	}
}
