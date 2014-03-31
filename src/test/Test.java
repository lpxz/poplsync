package test;

import gen.Stub;

import java.util.concurrent.ConcurrentHashMap;

import statemachine.Machine;

public class Test {

	// update -> control
	public static ConcurrentHashMap map = new ConcurrentHashMap();
	public static  Object key = new Object();
	public static void main(String[] args) throws Exception 
	{
	// want to see control issues? just search for "control".
		

		
		Stub.loadSM("dotfile");
		
	    Thread t1 = new Thread()
	    {
	    	public void run()
	    	{
	    		
	    		map.put(key, key);
	    		Stub.updateSM("T1.put");// can be upgraded to take more parameters.
	    		
	    		synchronized (Machine.machine) { // help the control!
	    			Object oo = map.get(key);
		    		 Stub.updateSM("T1.get");// can be upgraded to take more parameters.
		    		 System.out.println(oo);
				}
	    		
	    			
	    	    
	    	    
	    	}
	    };
	    Thread t2 = new Thread(){
	    	public void run(){
	    		
	    		map.remove(key);
	    		Stub.updateSM("T2.remove");// can be upgraded to take more parameters.
	    	}
	    };
	    
	    t1.start();
	    t2.start();
	    
	    t1.join();
	    t2.join();
		
	}

}
