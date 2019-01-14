package com.example.admin.memorynew;

public class SingerItem {

    String name;
    String mobile;
    int resId;

    public SingerItem(String name, String mobile){
        this.name = name;
        this.mobile = mobile;
    }

    public SingerItem(String name, String mobile, int resId){
        this.resId = resId;
        this.name = name;
        this.mobile = mobile;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

