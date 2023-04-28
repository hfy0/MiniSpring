package org.example;

import org.example.context.ClassPathXmlApplicationContext;
import org.example.test.AService;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = null;
        String fileName = "beans.xml";

        try {
            ctx = new ClassPathXmlApplicationContext(fileName);
            AService aService = (AService) ctx.getBean("aservice");
            aService.sayHello();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}