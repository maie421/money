package com.example.admin.memorynew;

public class detailsItem {
    String day,story,money,name;
    int number;

    public detailsItem(String day, String story, String money, int number,String name){
        this.day=day;
        this.story=story;
        this.money=money;
        this.number= number;
        this.name=name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
