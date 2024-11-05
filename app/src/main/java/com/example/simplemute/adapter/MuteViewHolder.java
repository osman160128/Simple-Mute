package com.example.simplemute.adapter;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplemute.Mute;
import com.example.simplemute.R;
import com.example.simplemute.databinding.ItemMuteBinding;
import com.example.simplemute.util.DayUtil;
import com.example.simplemute.util.OnToggleMuteListener;

public class MuteViewHolder extends RecyclerView.ViewHolder {
    TextView muteFrom,muteTo,muteRecurringDays,muteTitle,amPmFromTxt,amPmTxtTo;
    Switch muteStarted;
    ImageView deleteMute,muteRecuring;
    View itemView;
    TextView muteDay;
    public MuteViewHolder(@NonNull ItemMuteBinding itemView) {
        super(itemView.getRoot());

        muteFrom = itemView.itemMuteTimeFrom;
        muteRecurringDays=itemView.itemMuteRecurringDays;
        muteTitle = itemView.itemMuteTitle;
        muteStarted = itemView.itemMuteStarted;
        deleteMute = itemView.itemMuteRecurringDelete;
        muteRecuring = itemView.itemMuteRecurring;
        muteTo = itemView.itemMuteTimeTo;
        amPmFromTxt = itemView.ampmFromTxt;
        amPmTxtTo = itemView.amPmToTxt;
        muteDay = itemView.itemMuteDay;
        this.itemView = itemView.getRoot();
    }

    public void bind(Mute mute, OnToggleMuteListener listener) {
        int hourFrom =  mute.getHourFrom();
        int  hourFromAMPM = hourFrom % 12; // Convert 24-hour to 12-hour
        if (hourFromAMPM== 0) hourFromAMPM = 12;
        int hourTo =  mute.getHourTo();
        int  hourToAMPM = hourTo % 12; // Convert 24-hour to 12-hour
        if (hourToAMPM== 0) hourToAMPM = 12;
        String muteTextFrom=String.format("%02d:%02d ",hourFromAMPM,mute.getMinuteFrom());
        String muteTextTo=String.format("%02d:%02d ",hourToAMPM,mute.getMinuteTo());
        muteFrom.setText(muteTextFrom);
        muteTo.setText(muteTextTo);
        amPmFromTxt.setText(mute.getPmAmFrom());
        amPmTxtTo.setText(mute.getPmAmTo());
        muteStarted.setChecked(mute.isStarted());
        muteTitle.setText(mute.getTitle());

        if (mute.isRecurring()) {
            muteRecuring.setImageResource(R.drawable.ic_repeat_black_24dp);
            muteRecurringDays.setText(mute.getRecurringDaysText());
        } else {
            if(mute.isStarted()){
                muteRecuring.setImageResource(R.drawable.ic_repeat_black_24dp);
                muteRecurringDays.setText("Everyday");
            }else{
                muteRecuring.setImageResource(R.drawable.ic_looks_one_black_24dp);
                muteRecurringDays.setText("Once off");
            }

        }
//
        if(!mute.isStarted()){
            muteStarted.setVisibility(View.GONE);
        }
//        if(mute.isRecurring()){
//            muteDay.setText(mute.getRecurringDaysText());
//        }
//        else {
//            if(!mute.isStarted()){
//                muteDay.setText(DayUtil.getDay(mute.getHourFrom(),mute.getMinuteFrom()));
//            }else {
//                muteDay.setText("every day");
//            }
//
//        }
        muteStarted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isShown() || buttonView.isPressed()){
                    listener.onToggle(mute);
                }
            }
        });

        deleteMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(mute);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("mute is startred","IN view model mute "+mute.isStarted()+" requirment "+mute.isRecurring());
               listener.onItemClick(mute,view);
            }
        });

    }
}
