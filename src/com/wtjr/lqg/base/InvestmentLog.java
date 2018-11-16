package com.wtjr.lqg.base;

import java.io.Serializable;

public class InvestmentLog implements Serializable {
    private static final long serialVersionUID = -1812727487551456647L;

    public long it;

    public double im;

    public String up;

    @Override
    public String toString() {
        return "InvestmentLog [it=" + it + ", im=" + im + ", up=" + up + "]";
    }
}