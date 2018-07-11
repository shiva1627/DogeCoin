package org.sssj.com.dogecoin;

public class News {
    private String Subject;
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
