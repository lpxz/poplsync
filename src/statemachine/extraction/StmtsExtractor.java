package statemachine.extraction;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.test.Helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;




public class StmtsExtractor {

	public static List t1list = new ArrayList();
	public static List t2list = new ArrayList();
	
	// handle one composition at once!
	// do not be too agressive.
	public static void main(String[] args) {
		File file = new File("/home/lpxz/eclipse/workspaceOmerPOPL/popl/src/test/Test.java"); 
              extract(file);
      
              System.out.println("" + t1list + t2list);
              for(Object o : t1list)
              {
            	  System.out.println( ((Statement)o).getBeginLine());
            	  
              }
              

	}

	// add synchronized("AC_id_deadlockproofcounter"){}. AC_id encodes the information of the composition.
	// if there are deadlocks, add synchronized(new Object()){}.
	public static void extract(
			File clientMethodFile) {


		try {
			String source = Helper.readFile((clientMethodFile));
			CompilationUnit cu = Helper.parserString(source);
			ExtractVisitor visitor = new ExtractVisitor();
			visitor.visit(cu, null);
			Helper.writeFile(visitor.getSource(), (clientMethodFile.getPath()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
