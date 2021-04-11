package com.example.moinitoring;

public class SMSData {
    private String date;
    private String sendNumber;
    private String content;
    private int warning;

    public SMSData(String date, String sendNumber, String content, int warning){
        this.date = date;
        this.sendNumber = sendNumber;
        this.content = content;
        this.warning = warning;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSendNumber() {
        return sendNumber;
    }

    public void setSendNumber(String sendNumber) {
        this.sendNumber = sendNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getWarning() {
        return warning;
    }

    public void setWarning(int warning) {
        this.warning = warning;
    }
}
