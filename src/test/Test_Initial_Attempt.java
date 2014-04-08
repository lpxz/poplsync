package test;

import gen.Stub;
import java.util.concurrent.ConcurrentHashMap;
import statemachine.Machine;

public class Test_Initial_Attempt {

    public static ConcurrentHashMap map = new ConcurrentHashMap();

    public static Object key = new Object();

    public static void main(String[] args) throws Exception {
        Stub.loadSM("dotfile");
        Thread t1 = new Thread() {

            public void run() {
                map.put(key, key);
                Stub.updateSM("T1.put");
                synchronized (Machine.machine) {
                    Object oo = map.get(key);
                    Stub.updateSM("T1.get");
                    System.out.println(oo);
                }
            }
        };
        Thread t2 = new Thread() {

            public void run() {
                map.remove(key);
                Stub.updateSM("T2.remove");
            }
        };
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
