package ru.coffeeplanter.masstat.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.plumillonforge.android.chipview.Chip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.coffeeplanter.masstat.dao.ChipListSerializer;

/**
 * Person table model.
 */

public class Person implements Serializable {

    @Expose
    @SerializedName("id")
    private int mId;
    @Expose
    @SerializedName("name")
    private String mName;
    @Expose
    @SerializedName("keywords")
    @JsonAdapter(ChipListSerializer.class)
    private List<Chip> mKeywords;
    private List<Chip> mAdditionalKeywords; // Needs for passing additional keyword list

    public Person() {
        mKeywords = new ArrayList<>();
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
        Collections.sort(mKeywords, new Comparator<Chip>() {
            @Override
            public int compare(Chip o1, Chip o2) {
                return o1.getText().compareTo(o2.getText());
            }
        });
        return mKeywords;
    }

    public void setKeywords(List<Chip> keywords) {
        mKeywords = keywords;
    }

    public void addKeyword(Chip keyword) {
        mKeywords.add(keyword);
    }

    public List<Chip> getAdditionalKeywords() {
        Collections.sort(mAdditionalKeywords, new Comparator<Chip>() {
            @Override
            public int compare(Chip o1, Chip o2) {
                return o1.getText().compareTo(o2.getText());
            }
        });
        return mAdditionalKeywords;
    }

    public void setAdditionalKeywords(List<Chip> additionalKeywords) {
        mAdditionalKeywords = additionalKeywords;
    }

}
