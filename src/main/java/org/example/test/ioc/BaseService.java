package org.example.test.ioc;

import org.example.beans.annotation.Autowired;

public class BaseService {
    @Autowired
    private BaseBaseService baseBaseService;

    public BaseBaseService getBbs() {
        return baseBaseService;
    }

    public void setBbs(BaseBaseService bbs) {
        this.baseBaseService = bbs;
    }

    public BaseService() {
    }

    public void sayHello() {
        System.out.print("Base Service says hello");
        baseBaseService.sayHello();
    }
}
