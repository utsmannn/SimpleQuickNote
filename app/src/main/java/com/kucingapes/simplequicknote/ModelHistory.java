package com.kucingapes.simplequicknote;

public class ModelHistory {

    private String text;
    private String date;

    public ModelHistory(String text, String date) {
        this.text = text;
        this.date = date;
    }

    public ModelHistory() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
