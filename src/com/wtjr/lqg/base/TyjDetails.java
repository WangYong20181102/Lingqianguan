package com.wtjr.lqg.base;

import java.io.Serializable;
import java.util.Comparator;

public class TyjDetails implements Serializable{
    private static final long serialVersionUID = -8589137818780411034L;

    public double capital;

    public long experLogTime;

    public String explain;


    @Override
    public String toString() {
        return "TyjDetails [capital=" + capital + ", experLogTime="
                + experLogTime + ", explain=" + explain + "]";
    }
}