package com.example.android.newsapp;


import android.graphics.Bitmap;

public class News {

    /**
     * Title of the news
     */
    private String webTitle;

    /**
     * Name of the section
     */
    private String sectionName;

    /**
     * Web publication date of the news
     */
    private String publicationDate;

    /**
     * Web URL of the news
     */
    private String webUrl;




    /**
     *  @param webTitle          is the title of the news
     * @param sectionName       is the name of the section (Politics, Science etc)
     * @param publicationDate   is the publication date of the news
     * @param webUrl            is the website URL to find more details about the news
     */
    public News(String webTitle, String sectionName, String publicationDate, String webUrl) {
        this.webTitle = webTitle;
        this.sectionName = sectionName;
        this.publicationDate = publicationDate;
        this.webUrl = webUrl;

    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getWebUrl() {
        return webUrl;
    }


}
