/*
 * Copyright (C) 2007 J�?io Vilmar Gesser.
 * 
 * This file is part of Java 1.5 parser and Abstract Syntax Tree.
 *
 * Java 1.5 parser and Abstract Syntax Tree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java 1.5 parser and Abstract Syntax Tree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java 1.5 parser and Abstract Syntax Tree.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 05/10/2006
 */
package statemachine.extraction;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.DumpVisitor;

import java.util.Iterator;
import java.util.List;




public final class ExtractVisitor extends DumpVisitor {



	//for constraint (only one client method is hit) checking.
	public void visit(CompilationUnit n, Object arg) {
		if (n.getPackage() != null) {
			n.getPackage().accept(this, arg);
		}
		if (n.getImports() != null) {
			for (ImportDeclaration i : n.getImports()) {
				i.accept(this, arg);
			}
			printer.printLn();
		}
		if (n.getTypes() != null) {
			for (Iterator<TypeDeclaration> i = n.getTypes().iterator(); i
					.hasNext();) {
				TypeDeclaration tDeclaration= i.next();
				tDeclaration.accept(this, arg);
				printer.printLn();
				if (i.hasNext()) {
					printer.printLn();
				}
			}
		}



	}

	
	String currentCtxt = null; 
    public void visit(VariableDeclarator n, Object arg) {
        n.getId().accept(this, arg);
        if (n.getInit() != null) {
        	
        	 //lpxz:
            if(n.getInit() instanceof ObjectCreationExpr)
            {
            	ObjectCreationExpr oce = (ObjectCreationExpr)n.getInit();
            	if(oce.getType().toString().contains("Thread"))
            	{
                     if(n.getId().toString().contains("t1"))
                    	 currentCtxt = "t1";
                     else {
						currentCtxt="t2";
					}
            	}
            }
            
            
            printer.print(" = ");
            n.getInit().accept(this, arg);
            
           
        }
    }
    
	public void visit(ObjectCreationExpr n, Object arg) {
	        if (n.getScope() != null) {
	            n.getScope().accept(this, arg);
	            printer.print(".");
	        }

	        printer.print("new ");

	        printTypeArgs(n.getTypeArgs(), arg);
	        n.getType().accept(this, arg);

	        printArguments(n.getArgs(), arg);

	        if (n.getAnonymousClassBody() != null) {
	            printer.printLn(" {");
	            printer.indent();
	            printMembers(n.getAnonymousClassBody(), arg);
	            printer.unindent();
	            printer.print("}");
	        }
	    }
	 
	 
	public void visit(MethodDeclaration n, Object arg) {


		
		
		printJavadoc(n.getJavaDoc(), arg);
		printMemberAnnotations(n.getAnnotations(), arg);
		printModifiers(n.getModifiers());
		printTypeParameters(n.getTypeParameters(), arg);
		if (n.getTypeParameters() != null) {
			printer.print(" ");
		}

		n.getType().accept(this, arg);
		
		printer.print(" ");
		printer.print(n.getName());

		printer.print("(");
		if (n.getParameters() != null) {
			for (Iterator<Parameter> i = n.getParameters().iterator(); i
					.hasNext();) {
				Parameter p = i.next();
				p.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		printer.print(")");

		for (int i = 0; i < n.getArrayCount(); i++) {
			printer.print("[]");
		}

		if (n.getThrows() != null) {
			printer.print(" throws ");
			for (Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext();) {
				NameExpr name = i.next();
				name.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		if (n.getBody() == null) {
			printer.print(";");
		} else {
			printer.print(" ");
			BlockStmt body = n.getBody();
			body.accept(this, arg);

			//===========lpxz
			if(n.getName().equals("run"))
			{
				List todo ; 
				if(currentCtxt.equals("t1"))
				{
					todo = StmtsExtractor.t1list;
				}
				else {
					todo = StmtsExtractor.t2list;
				}
				
				if (body.getStmts() != null) {
					for (Statement s : body.getStmts()) {
						todo.add(s);
					}
				}
				
			}
			
			
		}
	}

	public BlockStmt	bodyInProgress	= null;

	//soot:    public static void main(java.lang.String[])
	//ast:     public static void main(String[] args) 

	// ambiguous packages.
	// If there are two classes: A.String and B.String (A and B are packages), the source code (ast) has to be explicit about which String to use.

	// about inner class.
	//soot: Employee$Internal
	//ast: Employee.Internal

	public void visit(BlockStmt n, Object arg) {

		printer.printLn("{");
		if (n.getStmts() != null) {
			printer.indent();
			for (Statement s : n.getStmts()) {
				
				s.accept(this, arg);
				printer.printLn();
			}
			printer.unindent();
		}


		printer.print("}");

	}




}
