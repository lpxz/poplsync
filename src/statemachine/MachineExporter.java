package statemachine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class MachineExporter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static void visualize(Machine csGraph, String fileName) { 
		File file = new File(fileName);
		FileWriter fw;
		try {
			fw = new FileWriter(file);	
			MachineExporter.export4StateGraph(fw, csGraph);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void export4StateGraph(Writer writer, Machine g)
	{
	    PrintWriter out = new PrintWriter(writer);
	
	    
	    String indent = "  ";
	    String connector;
	    
	
	    out.println("digraph G {");
	    connector = " -> ";
	
	
	    for (Object v : g.coreG.vertexSet()) {
	    	State state = (State )v;
	        out.print(indent +state.toString());     
	        if(state.isSafe())
	           out.print("["+ Keywords.safe+ "=true]");
	        else {
	        	out.print("["+ Keywords.safe+ "=false]");
			}
	        
	        out.println(";");
	    }
	
	    for (Object e : g.coreG.edgeSet()) {
	    	Transition eTransition = (Transition ) e ; 
	    	State source =  (State)g.coreG.getEdgeSource(eTransition); 
	    	State target = (State )g.coreG.getEdgeTarget(eTransition); 
	        out.print(indent + source + connector + target);  
	        if(eTransition.getOpName()!=null)
	        {
	        	out.print("["+ Keywords.op+ "="+eTransition.getOpName()+  ",label=" +eTransition.getOpName() + "]");
	        }
	        
	        out.println(";");
	    }
	
	    out.println("}");
	    out.flush();
	
	}

}
