package com.jsyh.buyer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mo on 17-4-13.
 */

public class BaseModel<T> {

    private int code;
    private String msg;


    @SerializedName(value = "data",alternate = "datas")
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
