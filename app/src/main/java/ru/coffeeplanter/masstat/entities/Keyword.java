package ru.coffeeplanter.masstat.entities;

import com.plumillonforge.android.chipview.Chip;

/**
 * Keyword model providing usage in ChipView.
 */

public class Keyword implements Chip {

    private int mId;
    private String mName;
    private int mPersonId;
    private int mType = 0;

    public Keyword(String name, int type) {
        this(name);
        mType = type;
    }

    public Keyword(String name) {
        mName = name;
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

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    // Method for ChipView functionality.
    @Override
    public String getText() {
        return mName;
    }

}
