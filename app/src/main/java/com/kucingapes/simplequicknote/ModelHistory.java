package com.kucingapes.simplequicknote;

public class ModelHistory {

    private String text;
    private String date;
    private int color;

    public ModelHistory(String text, String date, int color) {
        this.text = text;
        this.date = date;
        this.color = color;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
