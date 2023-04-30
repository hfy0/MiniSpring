package org.example.main;

import org.example.context.ClassPathXmlApplicationContext;
import org.example.test.aop.IAction;
import org.example.test.ioc.AService;

public class MainTestAOP {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = null;
        String fileName = "beans-testAOP.xml";

        try {
            ctx = new ClassPathXmlApplicationContext(fileName);

            // 测试 AOP
            IAction action = (IAction) ctx.getBean("action");
            action.doAction();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}