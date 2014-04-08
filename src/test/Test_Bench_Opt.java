package test;

import java.util.Random;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;




public class Test_Bench_Opt {

    public static ConcurrentHashMap map = new ConcurrentHashMap();

    public static int increaseRemoveTime = 3;

    
    // note that I change the remove() method so that it is longer than usual :)
    public static void main(String[] args) throws Exception {
// smart: 40199
// traditional:  3953

    	final boolean useSmartSync = false; 
    	final int numberOfT2 = 10; 
    	final int numberOfIterations =1000;// 
    	final int internalCompute = 40; 
    	final int randomRange = 50; // should not be sparse. If it is sparse, the traditional way is better.
    	final Random random = new Random(randomRange); 
        Thread t1 = new Thread() {

            public void run() {

            	for(int i=0; i< numberOfIterations;i++){
            		    int ss = random.nextInt(); 
            		    String key = (""+ss).intern(); // intern() is important, without it, two "1" strings may correspond to two different objects. 
            			// atomic:
            		    if(useSmartSync){
                        map.putWithMonitor(key, key);
                        
                        // timing:
                        long tmp = System.currentTimeMillis(); 
                        while( System.currentTimeMillis()-tmp  < internalCompute){
                            ;
                        }
                        
                        Object oo = map.getWithMonitor(key);
//                        System.out.println(oo);
                    	}else {
                    		synchronized (key) {
								
							
                    		 map.put(key, key);
                             
                             for(int j = 0; j <internalCompute ; j++ )
                             {
                             	System.out.print("");
                             }
                             
                             Object oo = map.get(key);
                    		}
						}
            	}
            	
            }
        };
        Thread[] t2s = new Thread[numberOfT2];
        for(int i=0;i< numberOfT2; i++)
        {
        	t2s[i] = new Thread() {

                public void run() {
                	for(int i=0; i< numberOfIterations;i++){
                		  int ss = random.nextInt(); 
              		    String key = (""+ss).intern(); // this is important: otherwise, two "1" strings may correspond to two different objects. 
              			if(useSmartSync)
                         map.removeWithMonitor(key);
              			else {
							synchronized (key) {
								 map.remove(key);
							}
						}
                	}
                }
            };
        }
        
//        System.out.println(t1.getId()); // 8 can we set it mechanically?
//        System.out.println(t2.getId()); // 9
        
       
        long starttime = System.currentTimeMillis(); 
        t1.start();
        for(Thread t2: t2s) t2.start();
        
        t1.join();
        for(Thread t2: t2s) t2.join();
        long endtime = System.currentTimeMillis();
        System.out.println("duration: " + (endtime-starttime));
        
    }
}
