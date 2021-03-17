package com.example.administrator.getuitest.demo.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import com.example.administrator.getuitest.demo.DemoApplication;
import com.example.administrator.getuitest.demo.ui.presenter.AuthInteractor;

import java.util.Calendar;

import static com.example.administrator.getuitest.demo.DemoApplication.isSignError;
import static com.example.administrator.getuitest.demo.config.Config.AUTH_ACTION;

/**
 * Time：2020-08-31 on 17:16.
 * Decription:.
 * Author:jimlee.
 */
public class AlarmUtils {

    public static final String IS_TRY_ONCEN = "is_try_once";

    public static void startAuthAlram(AlarmManager manager,boolean isTryOnce,int amount) {
        if (isSignError && !AuthInteractor.isTimeCorrect) {
            return;
        }
        Intent intent = new Intent(AUTH_ACTION);
        intent.putExtra(IS_TRY_ONCEN, isTryOnce);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, amount);
        PendingIntent pendingintent =
                PendingIntent.getBroadcast(DemoApplication.appContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingintent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingintent);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingintent);
        }
    }
}
