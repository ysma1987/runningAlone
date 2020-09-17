package com.ysma.ppt;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    private static final String pattern =
            "(\\s|\\S)*(CREATE|INSERT|ALTER|UPDATE|DROP|TRUNCATE|DELETE|GRANT|REVOKE|DECLARE)(\\s|\\S)*";
    public static void main(String[] args) {
        /*Student student = new Student();
        student.hello();*/
        String line = " \r\nDECLARE select 1 `code` from  where id=#{id}";

        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(line.toUpperCase());

        if(m.matches()){
            System.out.println("====>"+m.group(2));
        } else {
            System.out.println("<======");
        }
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
