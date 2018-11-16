package com.wtjr.lqg.base;

import java.io.Serializable;
import java.util.Comparator;

public class LqbDetails implements Serializable{
    private static final long serialVersionUID = -8589137818780411034L;
    
    public double optMoney;

    public int optType;

    public long optTime = System.currentTimeMillis();

    @Override
    public String toString() {
        return "LqbDetails [optMoney=" + optMoney + ", optType=" + optType
                + ", optTime=" + optTime + "]";
    }
}