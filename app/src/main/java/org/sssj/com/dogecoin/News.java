package org.sssj.com.dogecoin;

public class News {
    private String Subject;

    public News(String subject, String date, String details) {
        Subject = subject;
        this.date = date;
        Details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;
    private String Details;

    public News(String Subject, String details) {
        this.Subject = Subject;
        this.Details = details;
    }

    public String getSubject() {
        return Subject;
    }

    public String getDetails() {
        return Details;
    }
}
