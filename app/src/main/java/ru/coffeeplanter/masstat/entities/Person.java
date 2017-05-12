package ru.coffeeplanter.masstat.entities;

import com.plumillonforge.android.chipview.Chip;

import java.io.Serializable;
import java.util.List;

/**
 * Person table model. Maybe change to just a Map.
 */

public class Person implements Serializable {

    private int mId;
    private String mName;
    private List<Chip> mKeywords;

    public Person(String name) {
        this.mName = name;
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

    public List<Chip> getKeywords() {
        return mKeywords;
    }

    public void setKeywords(List<Chip> keywords) {
        mKeywords = keywords;
    }
}
