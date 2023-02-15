package com.example.pixelperfect.Assets;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FontFileAsset {

    public static void setFontByName(Context context, TextView textView, String name) {
        AssetManager assetManager = context.getAssets();
        textView.setTypeface(Typeface.createFromAsset(assetManager, "fonts/" + name));
    }

    public static List<String> getListFonts() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("font.ttf");
        arrayList.add("0.ttf");
        arrayList.add("1.ttf");
        arrayList.add("2.otf");
        arrayList.add("3.ttf");
        arrayList.add("4.ttf");
        arrayList.add("5.ttf");
        arrayList.add("6.ttf");
        arrayList.add("7.ttf");
        arrayList.add("8.ttf");
        arrayList.add("9.ttf");
        arrayList.add("10.ttf");
        arrayList.add("11.ttf");
        arrayList.add("12.ttf");
        arrayList.add("13.ttf");
        arrayList.add("14.ttf");
        arrayList.add("15.ttf");
        arrayList.add("16.ttf");
        arrayList.add("17.ttf");
        arrayList.add("18.ttf");
        arrayList.add("19.ttf");
        arrayList.add("20.ttf");
        arrayList.add("21.ttf");
        arrayList.add("22.ttf");
        arrayList.add("23.ttf");
        arrayList.add("24.ttf");
        arrayList.add("25.ttf");
        arrayList.add("26.ttf");
        arrayList.add("27.ttf");
        arrayList.add("28.ttf");
        arrayList.add("29.ttf");
        arrayList.add("30.ttf");
        arrayList.add("31.ttf");
        arrayList.add("32.ttf");
        arrayList.add("33.ttf");
        arrayList.add("34.ttf");
        arrayList.add("35.ttf");
        arrayList.add("36.ttf");
        arrayList.add("37.ttf");
        arrayList.add("38.ttf");
        arrayList.add("39.otf");
        arrayList.add("40.ttf");
        return arrayList;
    }
}
