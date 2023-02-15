package com.example.pixelperfect.Event;

import android.view.MotionEvent;

import com.example.pixelperfect.Sticker.StickerView;

public class EditTextIconEvent implements StickerIconEvent {
    public void onActionDown(StickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionMove(StickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionUp(StickerView paramStickerView, MotionEvent paramMotionEvent) {
        paramStickerView.editTextSticker();
    }
}
