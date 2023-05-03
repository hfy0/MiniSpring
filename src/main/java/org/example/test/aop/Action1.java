package org.example.test.aop;

public class Action1 implements IAction {

    @Override
    public void doAction() {
        System.out.println("really do action");
    }

    @Override
    public void doSomething() {
        System.out.println("really do something");
    }
}
