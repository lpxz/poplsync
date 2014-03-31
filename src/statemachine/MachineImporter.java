package statemachine;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class MachineImporter {

	public static void main(String[] args) {
		
         Machine mm = importSM("dotfile");		
         MachineExporter.visualize(mm, "xxx");
         
	     
	}
	
	public static Machine importSM(String filename)
	{
		   Machine machine = new Machine();

		   StringBuilder sb = new StringBuilder();
		   
		   try
		   {
			   FileInputStream fis = new FileInputStream(filename);
			   DataInputStream dis = new DataInputStream(fis);
			   BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			   String line;
			   while ((line = br.readLine()) != null) {
//				   sb.append(line);
				   if(!line.contains(";")) continue;
				   
				   
				   int endSymbol = line.indexOf(";");
				   line = line.substring(0, endSymbol);
				   
				   line =line.replaceAll("\\s", "");
				   System.out.println(line);
				   
				   int right= line.indexOf("]");
				   int left = line.indexOf("[");
				   String attribute = null;
				   Map attributeMap = new HashMap<String, String>();
				   if(left!=-1)
				   {
					   attribute=line.substring(left+1, right);
					   parseAttrs(attribute, attributeMap);
					   line=line.substring(0, left);// remove attributes
				   }
				  
				   
				   String[] parts= line.split("->");
				   for(String part : parts)
				   {
					   if(notIn(part, machine))
						   addNode(part, machine, attributeMap);
				   }
				   
				   for(int i=0; i<= parts.length-2; i++)
				   {
					   String start = parts[i];
					   String end = parts[i+1];
					   if(notIn(start, end, machine))
						   addEdge(start, end, machine, attributeMap);
				   }
			   }
			   dis.close();
		   } 
		   catch (Exception e) {
			   System.err.println("Error: " + e.getMessage());
		   }
		   
		   return machine;
	   
	   
	
	}



	private static void removeSpace(String[] parts) {
     for(String part : parts)
     {
    	 part.replaceAll("\\s+","");
     }
		
	}

	private static void  parseAttrs(String attribute, Map<String, String> tmp) {
        String[] each = attribute.split(",");
        for(String e : each)
        {
        	String[] leftright = e.split("=");
        	tmp.put(leftright[0], leftright[1]);
         }
		
	}

	private static void addEdge(String start, String end, Machine machine, Map attributeMap) {
        machine.addEdge(start, end, attributeMap);
		
	}

	private static boolean notIn(String start, String end, Machine machine) {
         return !machine.in(start, end);
	}
	
	private static void addNode(String part, Machine machine, Map attributeMap) {
          machine.addNode(part, attributeMap);		
	}

	private static boolean notIn(String part, Machine machine) {
		// TODO Auto-generated method stub
		return !machine.in(part);
	}
	
	

}
