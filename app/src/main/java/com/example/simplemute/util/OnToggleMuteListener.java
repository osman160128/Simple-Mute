package com.example.simplemute.util;

import android.view.View;

import com.example.simplemute.Mute;

public interface OnToggleMuteListener {
    void onToggle(Mute mute);
    void onDelete(Mute mute);
    void onItemClick(Mute mute, View view);
}
