package com.kucingapes.simplequicknote.Model;

public class ModelHistory {

    private String text;
    private String date;
    private int color;
    private int id;

    public ModelHistory(String text, String date, int color) {
        this.text = text;
        this.date = date;
        this.color = color;
    }

    public ModelHistory() {
    }

    public ModelHistory(String text, String date, int color, int id) {
        this.text = text;
        this.date = date;
        this.color = color;
        this.id = id;
    }

    public ModelHistory(int id) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
