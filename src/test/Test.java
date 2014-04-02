package test;

import gen.Stub;
import java.util.concurrent.ConcurrentHashMap;
import statemachine.Machine;

public class Test {

    public static ConcurrentHashMap map = new ConcurrentHashMap();

    public static Object key = new Object();

    public static void main(String[] args) throws Exception {
        Stub.loadSM("dotfile");
        Thread t1 = new Thread() {

            public void run() {
                map.put(key, key);
                Object oo = map.get(key);
            }
        };
        Thread t2 = new Thread() {

            public void run() {
                map.remove(key);
                map.remove(key);
            }
        };
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
