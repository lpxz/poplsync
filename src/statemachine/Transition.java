package statemachine;

import org.jgrapht.graph.DefaultEdge;



public class Transition extends DefaultEdge{

	public Transition()
	{
		super();
	}
    public String opName= null; 
    
    public Transition(String namepara)
    {
    	opName = namepara;
    }

    public void setOpName(String opNamePara)
    {
    	opName = opNamePara;
    }
    public String getOpName()
    {
    	return opName;
    }
    
	


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
