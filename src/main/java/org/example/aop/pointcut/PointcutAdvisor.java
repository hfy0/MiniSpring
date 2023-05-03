package org.example.aop.pointcut;

import org.example.aop.advisor.Advisor;

public interface PointcutAdvisor extends Advisor {
    Pointcut getPointcut();
}