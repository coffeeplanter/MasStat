package ru.coffeeplanter.masstat.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plumillonforge.android.chipview.Chip;

/**
 * Keyword model providing usage in ChipView.
 */

public class Keyword implements Chip {

    @Expose
    @SerializedName("id")
    private int mId;
    @Expose
    @SerializedName("name")
    private String mName;
    @Expose
    @SerializedName("person_id")
    private int mPersonId;

    public Keyword(String name) {
        mName = name;
    }

    public Keyword(int id, String name, int personId) {
        mId = id;
        mName = name;
        mPersonId = personId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getPersonId() {
        return mPersonId;
    }

    public void setPersonId(int personId) {
        mPersonId = personId;
    }

    // Method for ChipView functionality.
    @Override
    public String getText() {
        return mName;
    }

}
