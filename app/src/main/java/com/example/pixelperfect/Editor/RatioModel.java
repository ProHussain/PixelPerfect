package com.example.pixelperfect.Editor;

import com.steelkiwi.cropiwa.AspectRatio;

public class RatioModel extends AspectRatio {
    private int selectedIem;
    private String name;

    public RatioModel(int i, int i2, int i4, String name) {
        super(i, i2);
        this.selectedIem = i4;
        this.name = name;
    }

    public int getSelectedIem() {
        return this.selectedIem;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
