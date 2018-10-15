package com.example.android.thenews3;

/**
 * Created by yahir on 10/15/2018.
 */

/**
 * @link News} represents a article title, category, author, and date for news articles
 */
public class News {

    /**
     * News Title
     */
    private String mTitle;

    /**
     * News Category
     */
    private String mCategoryName;

    /**
     * News Author
     */
    private String mAuthor;

    /**
     * News report date
     */
    private String mDate;

    /**
     * News URL
     */
    private String mUrl;

    /**
     * Constructor for creating News object
     *
     * @param title is the string resource for the article title
     * @param categoryName is the string resource for the category name
     * @param author is the string resource for the author name
     * @param date is the string resource for the date of the article
     * @param url is the string resource for the article url at the guardian
     */
    public News(String title, String categoryName, String author, String date, String url) {
        mTitle = title;
        mCategoryName = categoryName;
        mAuthor = author;
        mDate = date;
        mUrl = url;
    }

    /**
     * Getter method for article title
     * @return article title
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Getter method for category name
     * @return article category name
     */
    public String getCategoryName() {
        return mCategoryName;
    }

    /**
     * Getter method for article author
     * @return author of the article
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Getter method for the article date
     * @return date of the article
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Getter method for the article url at the guardian
     * @return guardian url
     */
    public String getUrl() {
        return mUrl;
    }
}
