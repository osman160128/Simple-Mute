package com.example.simplemute;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.simplemute.broadcastreciver.MuteBroadcastReciver;
import com.example.simplemute.broadcastreciver.database.UnMuteBroadCastReciver;
import com.example.simplemute.util.DayUtil;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "mute_table")
public class Mute implements Serializable {

    @PrimaryKey
    @NonNull
    private int muteId;
    String title;
    private int hourFrom,minuteFrom,hourTo,minuteTo;
    private String pmAmFrom,pmAmTo;
    private boolean started,recurring;
    int year, month,day;
    private boolean monday,tuseday,wednesday,thursday,friday,saturday,sunday;

    public Mute(int muteId, String title, int hourFrom, int minuteFrom, int hourTo, int minuteTo,String pmAmFrom,String pmAmTo,int year,int month,int day,
                boolean started, boolean recurring, boolean monday, boolean tuseday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday) {
        this.muteId = muteId;
        this.title = title;
        this.hourFrom = hourFrom;
        this.minuteFrom = minuteFrom;
        this.hourTo = hourTo;
        this.minuteTo = minuteTo;
        this.pmAmFrom = pmAmFrom;
        this.pmAmTo = pmAmTo;
        this.year  = year;
        this.month = month;
        this.day = day;
        this.started = started;
        this.recurring = recurring;
        this.monday = monday;
        this.tuseday = tuseday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMinuteFrom() {
        return minuteFrom;
    }

    public void setMinuteFrom(int minuteFrom) {
        this.minuteFrom = minuteFrom;
    }

    public int getHourTo() {
        return hourTo;
    }

    public void setHourTo(int hourTo) {
        this.hourTo = hourTo;
    }

    public int getHourFrom() {
        return hourFrom;
    }

    public void setHourFrom(int hourFrom) {
        this.hourFrom = hourFrom;
    }

    public int getMinuteTo() {
        return minuteTo;
    }

    public void setMinuteTo(int minuteTo) {
        this.minuteTo = minuteTo;
    }
    public String getPmAmFrom() {
        return pmAmFrom;
    }

    public void setPmAmFrom(String pmAmFrom) {
        this.pmAmFrom = pmAmFrom;
    }

    public String getPmAmTo() {
        return pmAmTo;
    }

    public void setPmAmTo(String pmAmTo) {
        this.pmAmTo = pmAmTo;
    }

    public int getMuteId() {
        return muteId;
    }

    public void setMuteId(int alarmId) {
        this.muteId = alarmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }


    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean isTuseday() {
        return tuseday;
    }

    public void setTuseday(boolean tuseday) {
        this.tuseday = tuseday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }

    public void shedule(Context context) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        AlarmManager alarmManager2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.setTimeInMillis(System.currentTimeMillis());
        calendarFrom.set(Calendar.HOUR_OF_DAY, hourFrom);
        calendarFrom.set(Calendar.MINUTE, minuteFrom);
        calendarFrom.set(Calendar.SECOND, 0);
        calendarFrom.set(Calendar.MILLISECOND, 0);

        Intent intentFrom = new Intent(context, MuteBroadcastReciver.class);
        Bundle bundleFrom=new Bundle();
        bundleFrom.putSerializable(context.getString(R.string.arg_alarm_obj),this);
        intentFrom.putExtra(context.getString(R.string.bundle_alarm_obj),bundleFrom);
        PendingIntent PendingIntentFrom = PendingIntent.getBroadcast(context, muteId, intentFrom,PendingIntent.FLAG_ONE_SHOT| PendingIntent.FLAG_MUTABLE);

        Calendar calendarTo = Calendar.getInstance();
        calendarTo.setTimeInMillis(System.currentTimeMillis());
        calendarTo.set(Calendar.HOUR_OF_DAY, hourTo);
        calendarTo.set(Calendar.MINUTE, minuteTo);
        calendarTo.set(Calendar.SECOND, 0);
        calendarTo.set(Calendar.MILLISECOND, 0);

        if(year>0){
            calendarFrom.set(Calendar.DAY_OF_MONTH,day);
            calendarFrom.set(Calendar.MONTH,month);
            calendarFrom.set(Calendar.YEAR,year);

            calendarTo.set(Calendar.DAY_OF_MONTH,day);
            calendarTo.set(Calendar.MONTH,month);
            calendarTo.set(Calendar.YEAR,year);

        }

        Intent intentTo = new Intent(context, UnMuteBroadCastReciver.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable(context.getString(R.string.arg_alarm_obj),this);
        intentTo.putExtra(context.getString(R.string.bundle_alarm_obj),bundle);
        PendingIntent PendingIntenTo = PendingIntent.getBroadcast(context, muteId*10000, intentTo, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        // if alarm time has already passed, increment day by 1
        if (calendarFrom.getTimeInMillis() <= System.currentTimeMillis()) {
            calendarFrom.set(Calendar.DAY_OF_MONTH, calendarFrom.get(Calendar.DAY_OF_MONTH) + 1);
            calendarTo.set(Calendar.DAY_OF_MONTH, calendarTo.get(Calendar.DAY_OF_MONTH) + 1);

        }
        if(calendarFrom.getTimeInMillis()>= calendarTo.getTimeInMillis()){
            calendarTo.set(Calendar.DAY_OF_MONTH, calendarTo.get(Calendar.DAY_OF_MONTH) + 1);
        }

        if (!recurring) {

            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarFrom.getTimeInMillis(),
                    PendingIntentFrom
            );
            alarmManager2.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarTo.getTimeInMillis(),
                    PendingIntenTo
            );


        } else {

            final long RUN_DAILY = 24 * 60 * 60 * 1000;
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendarFrom.getTimeInMillis(),
                    RUN_DAILY,
                    PendingIntentFrom
            );
            alarmManager2.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendarTo.getTimeInMillis(),
                    RUN_DAILY,
                    PendingIntenTo
            );

        }

        this.started = true;

    }


    public String getRecurringDaysText() {
        if (!recurring) {
            return null;
        }

        String days = "";
        if (monday) {
            days += "Mon ";
        }
        if (tuseday) {
            days += "Tue ";
        }
        if (wednesday) {
            days += "Wed ";
        }
        if (thursday) {
            days += "Thu ";
        }
        if (friday) {
            days += "Fri ";
        }
        if (saturday) {
            days += "Sat ";
        }
        if (sunday) {
            days += "Sun ";
        }

        return days;
    }
    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MuteBroadcastReciver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, muteId, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);
        alarmManager.cancel(alarmPendingIntent);
        AlarmManager alarmManager2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intentTo = new Intent(context, UnMuteBroadCastReciver.class);
        PendingIntent alarmPendingIntentTo = PendingIntent.getBroadcast(context,muteId, intentTo, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);
        alarmManager2.cancel(alarmPendingIntentTo);
        this.started = false;


    }


}
