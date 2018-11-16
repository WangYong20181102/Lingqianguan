package com.wtjr.lqg.base;

import java.io.Serializable;
import java.util.List;

public class Register implements Serializable {
    private static final long serialVersionUID = -3645991831471600767L;

    public String name;

    public String age;

    @Override
    public String toString() {
        return "Register [name=" + name + ", age=" + age + "]";
    }
}