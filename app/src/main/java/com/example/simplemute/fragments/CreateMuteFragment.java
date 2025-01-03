package com.example.simplemute.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplemute.Mute;
import com.example.simplemute.R;
import com.example.simplemute.broadcastreciver.MuteService;
import com.example.simplemute.databinding.FragmentCreateMuteBinding;
import com.example.simplemute.viewmodel.CreateAlarmViewmodel;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;
import java.util.Random;


public class CreateMuteFragment extends Fragment {

    FragmentCreateMuteBinding fragmentCreateMuteBinding;

    Mute mute;

    CreateAlarmViewmodel createAlarmViewmodel;
    int hourFrom=0,minuteFrom=0,hourTo=0,minuteTo=0;
    String amPmFrom,amPmTo;
    boolean isStared=false;
    boolean isRecuring;
    int dateMonth =-1 ;
    int dateYear =-1;
    int dateDay =-1 ;
    // SharedPreferences key for alarmId
// SharedPreferences key for alarmId
    private static final String PREFS_NAME = "MuteAppPrefs";
    private static final String ALARM_ID_KEY = "current_alarm_id";

    // SharedPreferences object
    private SharedPreferences sharedPreferences;

    // Static variable to track the current alarmId
    private int currentAlarmId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mute = (Mute) getArguments().getSerializable(getString(R.string.arg_alarm_obj));
        }
        createAlarmViewmodel = new  ViewModelProvider(this).get(CreateAlarmViewmodel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentCreateMuteBinding = FragmentCreateMuteBinding.inflate(inflater, container, false);
        View view = fragmentCreateMuteBinding.getRoot();

        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        currentAlarmId = getCurrentAlarmIdFromPrefs();
        fragmentCreateMuteBinding.startAtBtn.setOnClickListener(View->startTimeMethod());
        fragmentCreateMuteBinding.endAtBtn.setOnClickListener(View->endTimeMethod());

        if(mute!=null){
            updateMuteInfo(mute);
        }


        fragmentCreateMuteBinding.fragmentCreatemuteOnce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStared = false;
                isRecuring = false;

                //set background for button
                fragmentCreateMuteBinding.fragmentCreatemuteOnce.setBackgroundResource(R.color.colorAccent);
                fragmentCreateMuteBinding.fragmentCreatemuteEveryDay.setBackgroundResource(R.color.bgcolor);
                fragmentCreateMuteBinding.fragmentCreatemuteRecurring.setBackgroundResource(R.color.bgcolor);

                //set tex color for background
                fragmentCreateMuteBinding.fragmentCreatemuteOnce.setTextColor(Color.parseColor("#FFFFFF"));
                fragmentCreateMuteBinding.fragmentCreatemuteEveryDay.setTextColor(Color.parseColor("#000000"));
                fragmentCreateMuteBinding.fragmentCreatemuteRecurring.setTextColor(Color.parseColor("#000000"));
                //visibilty for days
                fragmentCreateMuteBinding.fragmentCreatealarmRecurringOptions.setVisibility(View.GONE);

            }
        });
        fragmentCreateMuteBinding.fragmentCreatemuteEveryDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStared = true ;
                isRecuring = false;
                //set background for button
                fragmentCreateMuteBinding.fragmentCreatemuteEveryDay.setBackgroundResource(R.color.colorAccent);
                fragmentCreateMuteBinding.fragmentCreatemuteOnce.setBackgroundResource(R.color.bgcolor);
                fragmentCreateMuteBinding.fragmentCreatemuteRecurring.setBackgroundResource(R.color.bgcolor);

                //set tex color for background
                fragmentCreateMuteBinding.fragmentCreatemuteOnce.setTextColor(Color.parseColor("#000000"));
                fragmentCreateMuteBinding.fragmentCreatemuteEveryDay.setTextColor(Color.parseColor("#FFFFFF"));
                fragmentCreateMuteBinding.fragmentCreatemuteRecurring.setTextColor(Color.parseColor("#000000"));

                fragmentCreateMuteBinding.fragmentCreatealarmRecurringOptions.setVisibility(View.GONE);
            }
        });
        fragmentCreateMuteBinding.fragmentCreatemuteRecurring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStared = true;
                isRecuring = true;
                fragmentCreateMuteBinding.fragmentCreatemuteRecurring.setBackgroundResource(R.color.colorAccent);
                fragmentCreateMuteBinding.fragmentCreatemuteEveryDay.setBackgroundResource(R.color.bgcolor);
                fragmentCreateMuteBinding.fragmentCreatemuteOnce.setBackgroundResource(R.color.bgcolor);


                //set tex color for background
                fragmentCreateMuteBinding.fragmentCreatemuteOnce.setTextColor(Color.parseColor("#000000"));
                fragmentCreateMuteBinding.fragmentCreatemuteEveryDay.setTextColor(Color.parseColor("#000000"));
                fragmentCreateMuteBinding.fragmentCreatemuteRecurring.setTextColor(Color.parseColor("#FFFFFF"));
                fragmentCreateMuteBinding.fragmentCreatealarmRecurringOptions.setVisibility(View.VISIBLE);
            }
        });

        //save  Button
        fragmentCreateMuteBinding.saveMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecuring && fragmentCreateMuteBinding.fragmentCreatealarmCheckFri.isChecked()==false && fragmentCreateMuteBinding.fragmentCreatealarmCheckSat.isChecked()==false &&fragmentCreateMuteBinding.fragmentCreatealarmCheckSun.isChecked()==false &&fragmentCreateMuteBinding.fragmentCreatealarmCheckMon.isChecked()==false &&
                        fragmentCreateMuteBinding.fragmentCreatealarmCheckTue.isChecked()==false &&fragmentCreateMuteBinding.fragmentCreatealarmCheckWed.isChecked()==false &&fragmentCreateMuteBinding.fragmentCreatealarmCheckThu.isChecked()==false ){
                    Toast.makeText(getContext(), "Please select repearting day", Toast.LENGTH_SHORT).show();
                }else {
                    if (mute != null) {
                        updateScheduleFrom();
                        Navigation.findNavController(view).navigate(R.id.action_createMuteFragment_to_muteListFragment);
                    } else {
                        createscheduleFrom();
                        Navigation.findNavController(view).navigate(R.id.action_createMuteFragment_to_muteListFragment);
                    }
                }
            }
        });
        fragmentCreateMuteBinding.createMuteFragmentCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_createMuteFragment_to_muteListFragment);
            }
        });

        //select date to mute
        fragmentCreateMuteBinding.selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectdate();
            }
        });

        return view;

    }


    // Method to get the saved alarmId from SharedPreferences
    private int getCurrentAlarmIdFromPrefs() {
        return sharedPreferences.getInt(ALARM_ID_KEY, 0); // Default value is 0 if not set
    }

    // Method to save the current alarmId to SharedPreferences
    private void saveCurrentAlarmIdToPrefs(int alarmId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ALARM_ID_KEY, alarmId);
        editor.apply(); // Save the value asynchronously
    }

    //selete date method
    private void selectdate() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Handle the date picked
                    dateMonth = monthOfYear;
                    dateYear = year;
                    dateDay = dayOfMonth;
                    fragmentCreateMuteBinding.showdate.setText(dateYear+"/"+dateMonth+"/"+dateDay);
                    fragmentCreateMuteBinding.fragmentCreatemuteRecurring.setVisibility(View.GONE);
                    fragmentCreateMuteBinding.fragmentCreatemuteEveryDay.setVisibility(View.GONE);
                    }
                },
                year, month, dayOfMonth);
        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void endTimeMethod() {
        // Get the current time if hourTo and minuteTo are not set
        if (hourTo == 0 && minuteTo == 0) {
            Calendar calendar = Calendar.getInstance();
            hourTo = calendar.get(Calendar.HOUR_OF_DAY); // Get current hour (24-hour format)
            minuteTo = calendar.get(Calendar.MINUTE); // Get current minute
        }

        // Create a MaterialTimePicker with 12-hour clock and AM/PM input
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTitleText("Select end time")
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(hourTo) // Set the hour (from stored or current time)
                .setMinute(minuteTo) // Set the minute (from stored or current time)
                .build();

        // Listener to get the selected time
        picker.addOnPositiveButtonClickListener(dialog -> {
            hourTo = picker.getHour();
            minuteTo = picker.getMinute();
            amPmTo = (hourTo >= 12) ? "PM" : "AM"; // Determine AM/PM
            int hourToAMPM = hourTo % 12; // Convert 24-hour to 12-hour
            if (hourToAMPM == 0) hourToAMPM = 12; // Handle midnight and noon case
            // Update TextView with selected time
            String time = String.format("%02d:%02d %s", hourToAMPM, minuteTo, amPmTo);
            fragmentCreateMuteBinding.endAtTime.setText(time);
        });

        // Show the Time Picker
        picker.show(getParentFragmentManager(), "TIME_PICKER");
    }

    private void startTimeMethod() {
        // Get the current time if hourFrom and minuteFrom are not set
        if (hourFrom == 0 && minuteFrom == 0) {
            Calendar calendar = Calendar.getInstance();
            hourFrom = calendar.get(Calendar.HOUR_OF_DAY); // Get current hour (24-hour format)
            minuteFrom = calendar.get(Calendar.MINUTE); // Get current minute
        }

        // Create a MaterialTimePicker with 12-hour clock and AM/PM input
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTitleText("Select start time")
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(hourFrom) // Set the hour (from stored or current time)
                .setMinute(minuteFrom) // Set the minute (from stored or current time)
                .build();

        // Listener to get the selected time
        picker.addOnPositiveButtonClickListener(dialog -> {
            hourFrom = picker.getHour();
            minuteFrom = picker.getMinute();
            amPmFrom = (hourFrom >= 12) ? "PM" : "AM"; // Determine AM/PM
            int hourFromAMPM = hourFrom % 12; // Convert 24-hour to 12-hour
            if (hourFromAMPM == 0) hourFromAMPM = 12; // Handle midnight and noon case
            // Update TextView with selected time
            String time = String.format("%02d:%02d %s", hourFromAMPM, minuteFrom, amPmFrom);
            fragmentCreateMuteBinding.startAtTime.setText(time);
        });

        // Show the Time Picker
        picker.show(getParentFragmentManager(), "TIME_PICKER");
    }


    private void updateMuteInfo(Mute mute) {
        fragmentCreateMuteBinding.muteTitle.setText(mute.getTitle());

        hourFrom =  mute.getHourFrom();
        minuteFrom = mute.getMinuteFrom();
        amPmFrom = mute.getPmAmFrom();
        hourTo = mute.getHourTo();
        minuteTo = mute.getMinuteTo();
        amPmTo = mute.getPmAmTo();
        int  hourFromAMPM = hourFrom % 12; // Convert 24-hour to 12-hour
        if (hourFromAMPM== 0) hourFromAMPM = 12;
        int hourTo =  mute.getHourTo();
        int  hourToAMPM = hourTo % 12; // Convert 24-hour to 12-hour
        if (hourToAMPM== 0) hourToAMPM = 12;
        String muteTextFrom=String.format("%02d:%02d %s",hourFromAMPM,minuteFrom,amPmFrom);
        String muteTextTo=String.format("%02d:%02d %s",hourToAMPM,minuteTo,amPmTo);
        if(mute.getYear()<1){
            dateYear = mute.getYear();
            dateMonth = mute.getMonth();
            dateDay = mute.getDay();
            fragmentCreateMuteBinding.showdate.setText(dateYear+"/"+dateMonth+"/"+dateDay);

        }

       // Log.d("mute is startred","mute "+mute.isStarted()+" requirment "+mute.isRecurring());
        fragmentCreateMuteBinding.startAtTime.setText( muteTextFrom);
        fragmentCreateMuteBinding.endAtTime.setText( muteTextTo);
        if(!mute.isStarted()){
            fragmentCreateMuteBinding.fragmentCreatemuteOnce.setBackgroundResource(R.color.colorAccent);
            fragmentCreateMuteBinding.fragmentCreatemuteOnce.setTextColor(Color.parseColor("#FFFFFF"));
            isStared = false;
            isRecuring = false;
        }
        else if (mute.isStarted()){
            if(!mute.isRecurring()){

                fragmentCreateMuteBinding.fragmentCreatemuteEveryDay.setBackgroundResource(R.color.colorAccent);
                fragmentCreateMuteBinding.fragmentCreatemuteEveryDay.setTextColor(Color.parseColor("#FFFFFF"));
                isStared = true;
                isRecuring = false;
            }
            else if(mute.isRecurring()){

                    fragmentCreateMuteBinding.fragmentCreatemuteRecurring.setBackgroundResource(R.color.colorAccent);
                    fragmentCreateMuteBinding.fragmentCreatemuteRecurring.setTextColor(Color.parseColor("#FFFFFF"));
                    isStared = true;
                    isRecuring = true;
                    fragmentCreateMuteBinding.fragmentCreatealarmRecurringOptions.setVisibility(View.VISIBLE);
                    if(mute.isMonday())
                        fragmentCreateMuteBinding.fragmentCreatealarmCheckMon.setChecked(true);
                    if(mute.isTuseday())
                        fragmentCreateMuteBinding.fragmentCreatealarmCheckTue.setChecked(true);
                    if(mute.isWednesday())
                        fragmentCreateMuteBinding.fragmentCreatealarmCheckWed.setChecked(true);
                    if(mute.isThursday())
                        fragmentCreateMuteBinding.fragmentCreatealarmCheckThu.setChecked(true);
                    if(mute.isFriday())
                        fragmentCreateMuteBinding.fragmentCreatealarmCheckFri.setChecked(true);
                    if(mute.isSaturday())
                        fragmentCreateMuteBinding.fragmentCreatealarmCheckSat.setChecked(true);
                    if(mute.isSunday())
                        fragmentCreateMuteBinding.fragmentCreatealarmCheckSun.setChecked(true);
            }

        }

    }
    public void updateScheduleFrom() {
        mute.cancelAlarm(getContext());
        String title = fragmentCreateMuteBinding.muteTitle.getText().toString();
        if(title.isEmpty()){
            title = getString(R.string.mute_title);
        }

        Mute  updateMute = new Mute(
                mute.getMuteId(),
                title,
                hourFrom,
                minuteFrom,
                hourTo,
                minuteTo,
                amPmFrom,
                amPmTo,
                dateYear,
                dateMonth,
                dateDay,
                isStared,
                isRecuring,
                fragmentCreateMuteBinding.fragmentCreatealarmCheckMon.isChecked(),
                fragmentCreateMuteBinding.fragmentCreatealarmCheckTue.isChecked(),
                fragmentCreateMuteBinding.fragmentCreatealarmCheckWed.isChecked(),
                fragmentCreateMuteBinding.fragmentCreatealarmCheckThu.isChecked(),
                fragmentCreateMuteBinding.fragmentCreatealarmCheckFri.isChecked(),
                fragmentCreateMuteBinding.fragmentCreatealarmCheckSat.isChecked(),
                fragmentCreateMuteBinding.fragmentCreatealarmCheckSun.isChecked()
        );

        createAlarmViewmodel.update(updateMute);
        updateMute.shedule(getContext());
    }

    public void createscheduleFrom() {
        String title = fragmentCreateMuteBinding.muteTitle.getText().toString();
        if(title.isEmpty()){
            title = getString(R.string.mute_title);
        }

        // Use the current alarmId and increment it
        int alarmId = currentAlarmId++;

        // Save the updated alarmId back to SharedPreferences
        saveCurrentAlarmIdToPrefs(currentAlarmId);;

            Mute mute = new Mute(
                    alarmId,
                    title,
                    hourFrom,
                    minuteFrom,
                    hourTo,
                    minuteTo,
                    amPmFrom,
                    amPmTo,
                    dateYear,
                    dateMonth,
                    dateDay,
                    isStared,
                    isRecuring,
                    fragmentCreateMuteBinding.fragmentCreatealarmCheckMon.isChecked(),
                    fragmentCreateMuteBinding.fragmentCreatealarmCheckTue.isChecked(),
                    fragmentCreateMuteBinding.fragmentCreatealarmCheckWed.isChecked(),
                    fragmentCreateMuteBinding.fragmentCreatealarmCheckThu.isChecked(),
                    fragmentCreateMuteBinding.fragmentCreatealarmCheckFri.isChecked(),
                    fragmentCreateMuteBinding.fragmentCreatealarmCheckSat.isChecked(),
                    fragmentCreateMuteBinding.fragmentCreatealarmCheckSun.isChecked()
            );
            createAlarmViewmodel.insert(mute);
            mute.shedule(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentCreateMuteBinding = null;
    }

}