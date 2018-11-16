package com.wtjr.lqg.base;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "AAAAAAA")
public class AAAAAAA {
    @Column(name = "bName")
    public int age;
    @Column(name = "uId", isId = true, autoGen = false)
    public String name;
}
