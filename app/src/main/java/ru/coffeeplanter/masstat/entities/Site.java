package ru.coffeeplanter.masstat.entities;

import java.io.Serializable;

/**
 * Site table model.
 */

public class Site implements Serializable {

    private int mId;
    private String mName;
    private String mBaseUrl;
    private String mOpenTag;
    private String mCloseTag;

    public Site(String name, String url) {
        this.mName = name;
        this.mBaseUrl = url;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    public String getOpenTag() {
        return mOpenTag;
    }

    public void setOpenTag(String openTag) {
        this.mOpenTag = openTag;
    }

    public String getCloseTag() {
        return mCloseTag;
    }

    public void setCloseTag(String closeTag) {
        this.mCloseTag = closeTag;
    }

}
