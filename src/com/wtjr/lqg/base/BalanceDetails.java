package com.wtjr.lqg.base;

import java.io.Serializable;
import java.util.Comparator;

public class BalanceDetails implements Serializable{
    private static final long serialVersionUID = -8589137818780411034L;

    public int type;
    
    public double capital;

    public long detailOptTime;

    public String explain;

    @Override
    public String toString() {
        return "BalanceDetails [type=" + type + ", capital=" + capital
                + ", detailOptTime=" + detailOptTime + ", explain=" + explain
                + "]";
    }
}