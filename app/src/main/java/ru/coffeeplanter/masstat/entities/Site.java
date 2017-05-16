package ru.coffeeplanter.masstat.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Site table model.
 */

public class Site implements Serializable {

    @Expose
    @SerializedName("id")
    private int mId;
    @Expose
    @SerializedName("name")
    private String mName;
    @Expose
    @SerializedName("base_url")
    private String mBaseUrl;
    @Expose
    @SerializedName("open_tag")
    private String mOpenTag;
    @Expose
    @SerializedName("close_tag")
    private String mCloseTag;

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
