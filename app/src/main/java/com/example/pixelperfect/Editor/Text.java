package com.example.pixelperfect.Editor;

import android.graphics.Color;
import android.graphics.Shader;

import java.util.ArrayList;
import java.util.List;

public class Text {
    private int backgroundAlpha;
    private int backgroundBorder;
    private int backgroundColor;
    private int backgroundColorIndex;
    private int fontIndex;
    private String fontName;
    private boolean isShowBackground;
    private int paddingHeight;
    private int paddingWidth;
    private String text;
    private int textAlign;
    private int textAlpha;
    private int textColor;
    private int textColorIndex;
    private int textHeight;
    private Shader textShader;
    private int textShaderIndex;
    private TextShadow textShadow;
    private int textShadowIndex;
    private int textSize;
    private int textWidth;

    public static class TextShadow {
        private int colorShadow;
        private int x;
        private int y;
        private int radius;

        TextShadow(int i, int i2, int i3, int i4) {
            this.radius = i;
            this.x = i2;
            this.y = i3;
            this.colorShadow = i4;
        }

        public int getRadius() {
            return this.radius;
        }

        public void setRadius(int i) {
            this.radius = i;
        }

        public int getDx() {
            return this.x;
        }

        public int getDy() {
            return this.y;
        }

        public int getColorShadow() {
            return this.colorShadow;
        }
    }

    public static List<TextShadow> getLstTextShadow() {
        ArrayList<TextShadow> arrayList = new ArrayList<>();
        arrayList.add(new TextShadow(0, 0, 0, -16711681));
        arrayList.add(new TextShadow(8, 4, 4, Color.parseColor("#FFFFFF")));
        arrayList.add(new TextShadow(8, 4, 4, Color.parseColor("#000000")));
        arrayList.add(new TextShadow(8, 4, 4, Color.parseColor("#FF0000")));
        arrayList.add(new TextShadow(8, 4, 4, Color.parseColor("#FF3C00")));
        arrayList.add(new TextShadow(8, 4, 4, Color.parseColor("#FFAA00")));
        arrayList.add(new TextShadow(8, 4, 4, Color.parseColor("#D9FF00")));
        arrayList.add(new TextShadow(8, 4, 4, Color.parseColor("#08FF00")));
        arrayList.add(new TextShadow(8, 4, 4, Color.parseColor("#00FFA6")));
        arrayList.add(new TextShadow(8, 4, 4, Color.parseColor("#0099FF")));
        arrayList.add(new TextShadow(8, 4, 4, Color.parseColor("#0022FF")));
        arrayList.add(new TextShadow(8, 4, 4, Color.parseColor("#7700FF")));
        arrayList.add(new TextShadow(8, 4, 4, Color.parseColor("#D900FF")));
        arrayList.add(new TextShadow(8, 4, 4, Color.parseColor("#FF0099")));
        arrayList.add(new TextShadow(8, 4, 4, Color.parseColor("#FF0048")));
        arrayList.add(new TextShadow(8, -4, -4, Color.parseColor("#FFFFFF")));
        arrayList.add(new TextShadow(8, -4, -4, Color.parseColor("#000000")));
        arrayList.add(new TextShadow(8, -4, -4, Color.parseColor("#FF0000")));
        arrayList.add(new TextShadow(8, -4, -4, Color.parseColor("#FF3C00")));
        arrayList.add(new TextShadow(8, -4, -4, Color.parseColor("#FFAA00")));
        arrayList.add(new TextShadow(8, -4, -4, Color.parseColor("#D9FF00")));
        arrayList.add(new TextShadow(8, -4, -4, Color.parseColor("#08FF00")));
        arrayList.add(new TextShadow(8, -4, -4, Color.parseColor("#00FFA6")));
        arrayList.add(new TextShadow(8, -4, -4, Color.parseColor("#0099FF")));
        arrayList.add(new TextShadow(8, -4, -4, Color.parseColor("#0022FF")));
        arrayList.add(new TextShadow(8, -4, -4, Color.parseColor("#7700FF")));
        arrayList.add(new TextShadow(8, -4, -4, Color.parseColor("#D900FF")));
        arrayList.add(new TextShadow(8, -4, -4, Color.parseColor("#FF0099")));
        arrayList.add(new TextShadow(8, -4, -4, Color.parseColor("#FF0048")));

        arrayList.add(new TextShadow(8, -4, 4, Color.parseColor("#FFFFFF")));
        arrayList.add(new TextShadow(8, -4, 4, Color.parseColor("#000000")));
        arrayList.add(new TextShadow(8, -4, 4, Color.parseColor("#FF0000")));
        arrayList.add(new TextShadow(8, -4, 4, Color.parseColor("#FF3C00")));
        arrayList.add(new TextShadow(8, -4, 4, Color.parseColor("#FFAA00")));
        arrayList.add(new TextShadow(8, -4, 4, Color.parseColor("#D9FF00")));
        arrayList.add(new TextShadow(8, -4, 4, Color.parseColor("#08FF00")));
        arrayList.add(new TextShadow(8, -4, 4, Color.parseColor("#00FFA6")));
        arrayList.add(new TextShadow(8, -4, 4, Color.parseColor("#0099FF")));
        arrayList.add(new TextShadow(8, -4, 4, Color.parseColor("#0022FF")));
        arrayList.add(new TextShadow(8, -4, 4, Color.parseColor("#7700FF")));
        arrayList.add(new TextShadow(8, -4, 4, Color.parseColor("#D900FF")));
        arrayList.add(new TextShadow(8, -4, 4, Color.parseColor("#FF0099")));
        arrayList.add(new TextShadow(8, -4, 4, Color.parseColor("#FF0048")));

        arrayList.add(new TextShadow(8, 4, -4, Color.parseColor("#FFFFFF")));
        arrayList.add(new TextShadow(8, 4, -4, Color.parseColor("#000000")));
        arrayList.add(new TextShadow(8, 4, -4, Color.parseColor("#FF0000")));
        arrayList.add(new TextShadow(8, 4, -4, Color.parseColor("#FF3C00")));
        arrayList.add(new TextShadow(8, 4, -4, Color.parseColor("#FFAA00")));
        arrayList.add(new TextShadow(8, 4, -4, Color.parseColor("#D9FF00")));
        arrayList.add(new TextShadow(8, 4, -4, Color.parseColor("#08FF00")));
        arrayList.add(new TextShadow(8, 4, -4, Color.parseColor("#00FFA6")));
        arrayList.add(new TextShadow(8, 4, -4, Color.parseColor("#0099FF")));
        arrayList.add(new TextShadow(8, 4, -4, Color.parseColor("#0022FF")));
        arrayList.add(new TextShadow(8, 4, -4, Color.parseColor("#7700FF")));
        arrayList.add(new TextShadow(8, 4, -4, Color.parseColor("#D900FF")));
        arrayList.add(new TextShadow(8, 4, -4, Color.parseColor("#FF0099")));
        arrayList.add(new TextShadow(8, 4, -4, Color.parseColor("#FF0048")));
        return arrayList;
    }

    public static Text getDefaultProperties() {
        Text polishText = new Text();
        polishText.setTextSize(30);
        polishText.setTextAlign(4);
        polishText.setFontName("36.ttf");
        polishText.setTextColor(-1);
        polishText.setTextAlpha(255);
        polishText.setBackgroundAlpha(255);
        polishText.setPaddingWidth(12);
        polishText.setTextShaderIndex(7);
        polishText.setBackgroundColorIndex(21);
        polishText.setTextColorIndex(16);
        polishText.setFontIndex(0);
        polishText.setShowBackground(false);
        polishText.setBackgroundBorder(8);
        polishText.setTextAlign(4);
        return polishText;
    }

    public int getTextColorIndex() {
        return this.textColorIndex;
    }

    public void setTextColorIndex(int i) {
        this.textColorIndex = i;
    }

    public int getTextShaderIndex() {
        return this.textShaderIndex;
    }

    public void setTextShaderIndex(int i) {
        this.textShaderIndex = i;
    }

    public int getBackgroundColorIndex() {
        return this.backgroundColorIndex;
    }

    public void setBackgroundColorIndex(int i) {
        this.backgroundColorIndex = i;
    }

    public int getFontIndex() {
        return this.fontIndex;
    }

    public void setFontIndex(int i) {
        this.fontIndex = i;
    }

    public int getTextShadowIndex() {
        return this.textShadowIndex;
    }

    public void setTextShadowIndex(int i) {
        this.textShadowIndex = i;
    }

    public TextShadow getTextShadow() {
        return this.textShadow;
    }

    public void setTextShadow(TextShadow textShadow2) {
        this.textShadow = textShadow2;
    }

    public int getBackgroundBorder() {
        return this.backgroundBorder;
    }

    public void setBackgroundBorder(int i) {
        this.backgroundBorder = i;
    }

    public int getTextHeight() {
        return this.textHeight;
    }

    public void setTextHeight(int i) {
        this.textHeight = i;
    }

    public int getTextWidth() {
        return this.textWidth;
    }

    public void setTextWidth(int i) {
        this.textWidth = i;
    }

    public int getPaddingWidth() {
        return this.paddingWidth;
    }

    public void setPaddingWidth(int i) {
        this.paddingWidth = i;
    }

    public int getPaddingHeight() {
        return this.paddingHeight;
    }

    public void setPaddingHeight(int i) {
        this.paddingHeight = i;
    }

    public int getTextSize() {
        return this.textSize;
    }

    public void setTextSize(int i) {
        this.textSize = i;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int i) {
        this.textColor = i;
    }

    public int getTextAlpha() {
        return this.textAlpha;
    }

    public void setTextAlpha(int i) {
        this.textAlpha = i;
    }

    public Shader getTextShader() {
        return this.textShader;
    }

    public void setTextShader(Shader shader) {
        this.textShader = shader;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public int getTextAlign() {
        return this.textAlign;
    }

    public void setTextAlign(int i) {
        this.textAlign = i;
    }

    public String getFontName() {
        return this.fontName;
    }

    public void setFontName(String str) {
        this.fontName = str;
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(int i) {
        this.backgroundColor = i;
    }

    public boolean isShowBackground() {
        return this.isShowBackground;
    }

    public void setShowBackground(boolean z) {
        this.isShowBackground = z;
    }

    public int getBackgroundAlpha() {
        return this.backgroundAlpha;
    }

    public void setBackgroundAlpha(int i) {
        this.backgroundAlpha = i;
    }
}
