package com.ysma.ppt;


import java.util.*;

public class Test {
    public static void main(String[] args) {
        Student student = new Student();
        student.hello();

    }

    static abstract class Person{
        private String name;
        private int age;


        void hello(){
            say("test");
        }

        void say(String input){
            System.out.println("===>" + input);
        }
    }

    static class Student extends Person{
        void say(String input){
            System.out.println("test:===>" + input);
        }
    }
}
