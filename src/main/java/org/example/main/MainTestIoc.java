package org.example.main;

import org.example.context.ClassPathXmlApplicationContext;
import org.example.test.ioc.AService;

public class MainTestIoc {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = null;
        String fileName = "beans-testIoC.xml";

        try {
            ctx = new ClassPathXmlApplicationContext(fileName);
            // 测试 Ioc
            AService aService = (AService) ctx.getBean("aService");
            aService.sayHello();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}