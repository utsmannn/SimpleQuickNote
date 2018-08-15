package com.kucingapes.simplequicknote.Model;

public class ModelHistory {

    private String text;
    private String date;
    private int color;
    private int id;
    private String timerDate;
    private long futureMilis;

    public ModelHistory(String text, String date, int color) {
        this.text = text;
        this.date = date;
        this.color = color;
    }

    public ModelHistory() {
    }

    /*public ModelHistory(String text, String date, int color, int id) {
        this.text = text;
        this.date = date;
        this.color = color;
        this.id = id;
    }*/

    /*public ModelHistory(String text, String date, int color, int id, boolean timer) {
        this.text = text;
        this.date = date;
        this.color = color;
        this.id = id;
        this.timer = timer;
    }*/

    /*public ModelHistory(String text, String date, int color, int id, String timerDate) {
        this.text = text;
        this.date = date;
        this.color = color;
        this.id = id;
        this.timerDate = timerDate;
    }*/

    public ModelHistory(String text, String date, int color, int id, String timerDate, long futureMilis) {
        this.text = text;
        this.date = date;
        this.color = color;
        this.id = id;
        this.timerDate = timerDate;
        this.futureMilis = futureMilis;
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

    public String getTimerDate() {
        return timerDate;
    }

    public void setTimerDate(String timerDate) {
        this.timerDate = timerDate;
    }

    public long getFutureMilis() {
        return futureMilis;
    }

    public void setFutureMilis(long futureMilis) {
        this.futureMilis = futureMilis;
    }
}
