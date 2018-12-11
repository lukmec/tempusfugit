package de.lumdev.event_logic;

/**
 * Created by LukasSurface on 12.04.2018.
 */

public class Quick_Test {

    public static void main(String[] args){
        test1();
    }

    public static void test1(){
        BasicEvent testEvent1 = new BasicEvent(1,"Eins", "Hier steht was drin.");
        BasicEvent testEvent2 = new BasicEvent(2,"Event 2", "Hier steht auch schon wieder was drin.");
//        System.out.println(testEvent1);
        System.out.println(AllEvents.getInstance().toString());
    }

    public static void test2(){
    }
}
