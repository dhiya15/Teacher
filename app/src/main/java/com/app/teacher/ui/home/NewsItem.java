package com.app.teacher.ui.home;

public class NewsItem {

    public int image;
    public String title;
    public String subTitle;
    public String date;
    public String time;

    public NewsItem(int img, String title, String subTitle, String date, String time) {
        this.image = img;
        this.title = title;
        this.subTitle = subTitle;
        this.date = date;
        this.time = time;
    }

}
