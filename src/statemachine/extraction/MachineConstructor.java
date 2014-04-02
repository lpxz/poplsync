package statemachine.extraction;

import japa.parser.ast.stmt.Statement;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import statemachine.Keywords;
import statemachine.Machine;
import statemachine.MachineExporter;
import statemachine.Pair;
import statemachine.State;
import statemachine.Transition;

public class MachineConstructor {

	public static HashMap<Pair, State> visited = new HashMap<Pair, State>();
	public static int visitorOrder = 0; 
	public static void main(String[] args) {

		File file = new File("./src/test/Test.java"); 
        StmtsExtractor.extract(file);

        System.out.println("" + StmtsExtractor.t1list + StmtsExtractor.t2list);
        for(Object o : StmtsExtractor.t1list)
        {
      	  System.out.println( ((Statement)o).getBeginLine());
      	  
        }
        
//        Queue t1queue = new LinkedBlockingQueue();
//        t1queue.addAll(StmtsExtractor.t1list);
//        Queue t2queue = new LinkedBlockingQueue();
//        t2queue.addAll(StmtsExtractor.t2list);
        
        Machine machine = new Machine();
        
        // add root
        Pair rootPair = new Pair(0,0); // after line 0, line 0
        if(!visited.containsKey(rootPair))
        {
        	State state= new State();
        	state.setName(""+ (visitorOrder++));
        	state.setCorePair(rootPair);
        	visited.put(rootPair, state);
        	machine.addNodeIfAbsent(state.getName(), state);
        	visitState(machine, state, StmtsExtractor.t1list, StmtsExtractor.t2list);
        }
        
        MachineExporter.visualize(machine, "xxx");
	}
	

	public static int getLine(Object arg)
	{
		try {
		return 	((Statement)arg).getEndLine();
		} catch (Exception e) {
			throw new RuntimeException("give the arg of the type Statement");
//			return -1;
		}
	
	}

	private static void visitState(Machine machine, State state, List t1queue, List t2queue) {
		// visit itself
		Pair currentPair = state.getCorePair();
		System.out.println(currentPair.t1part + " "+ currentPair.t2part);
		
		Statement t1Next = search4Next(t1queue, currentPair.t1part);
		Statement t2Next = search4Next(t2queue, currentPair.t2part);

		if(t1Next!=null)
		{
			Pair t1NextPair = new Pair(getLine(t1Next), currentPair.t2part);
			if(!visited.containsKey(t1NextPair))
			{
	             State t1NextState = new State(); 		
	             t1NextState.setName(""+ (visitorOrder++));
	             t1NextState.setCorePair(t1NextPair);
	         	 visited.put(t1NextPair, t1NextState);
	         	 machine.addNodeIfAbsent(t1NextState.getName(), t1NextState);// invariant: if visited has it, machine has it.
	         	 
	         	 //build the link
	         	 Transition t1NextTransition =machine.addEdgeIfAbsent(state.getName(), t1NextState.getName());
	         	 if(t1NextTransition!=null )t1NextTransition.setOpName(Keywords.T1 + Keywords.connector+ extractOp(t1Next) );
	         	 
	         	 visitState(machine, t1NextState, t1queue, t2queue);
	         	
			}
			else {
				State t1NextState = visited.get(t1NextPair);// it must be in the machine now. invariant
				Transition t1NextTransition =machine.addEdgeIfAbsent(state.getName(), t1NextState.getName());
	         	 if(t1NextTransition!=null )t1NextTransition.setOpName(Keywords.T1 + Keywords.connector+   extractOp(t1Next) );
			}
		}
		if(t2Next!=null)
		{
			Pair t2NextPair = new Pair(currentPair.t1part, getLine(t2Next));
			if(!visited.containsKey(t2NextPair))
			{
				State t2NextState = new State();
				t2NextState.setName(""+ (visitorOrder++));
				t2NextState.setCorePair(t2NextPair);
				visited.put(t2NextPair, t2NextState);
				machine.addNodeIfAbsent(t2NextState.getName(), t2NextState);

				Transition t2NextTransition = machine.addEdgeIfAbsent(state.getName(),t2NextState.getName());
				if(t2NextTransition!=null) t2NextTransition.setOpName(Keywords.T2+ Keywords.connector+   extractOp(t2Next));
				
				visitState(machine, t2NextState, t1queue, t2queue);
			}
			else {
				State t2NextState = visited.get(t2NextPair);
				Transition t2NextTransition = machine.addEdgeIfAbsent(state.getName(), t2NextState.getName());
				if(t2NextTransition!=null) t2NextTransition.setOpName(Keywords.T2 + Keywords.connector+  extractOp(t2Next));
				
			}
		}
			
		
	}


	private static Statement search4Next(List t1queue, Object t1part) {
		if(t1queue.isEmpty()) return null;
		
		if(t1part.equals(0)) // special case. after 0 
			return (Statement)t1queue.get(0);
		
		for(int i=0; i< t1queue.size(); i++)
		{
			Statement iStatement = (Statement) t1queue.get(i);
		    if(t1part.equals(getLine(iStatement)))
		    {
		    	if(i+1<t1queue.size())
		    	   return (Statement)t1queue.get(i+1);
		    	
		    }
 		}
		return null;
	}


	private static String extractOp(Statement t1Next) {
		// TODO Auto-generated method stub
		int dot = t1Next.toString().indexOf(".");
		int left = t1Next.toString().indexOf("(");
		return t1Next.toString().substring(dot+1, left);
		
	}

}
