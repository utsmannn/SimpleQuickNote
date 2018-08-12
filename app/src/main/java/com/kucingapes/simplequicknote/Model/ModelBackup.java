package com.kucingapes.simplequicknote.Model;

public class ModelBackup {
    private String json;
    private String date;
    private String size;

    public ModelBackup(){
    }

    public ModelBackup(String json, String date, String size) {
        this.json = json;
        this.date = date;
        this.size = size;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
