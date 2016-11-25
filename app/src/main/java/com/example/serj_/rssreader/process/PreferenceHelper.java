package com.example.serj_.rssreader.process;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.example.serj_.rssreader.R;
import lombok.NonNull;


public final class PreferenceHelper{
    public static boolean openinBrowser(@NonNull Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(context.getString(R.string.browser_check_key),false);
    }
    public static long returnPeriod (@NonNull Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String selectedPeriod = sharedPref.getString(context.getString(R.string.update_periods_key)," ");
        if(selectedPeriod.equals(context.getString(R.string.period_value_2))){
            return AlarmManager.INTERVAL_HOUR;

        }else
            if(selectedPeriod.equals(context.getString(R.string.period_value_3))){
                return AlarmManager.INTERVAL_HOUR*2;
            }else
                if(selectedPeriod.equals(context.getString(R.string.period_value_4))){
                    return AlarmManager.INTERVAL_HOUR*4;
                }else {
                    return AlarmManager.INTERVAL_HALF_HOUR;
                }

    }
}
