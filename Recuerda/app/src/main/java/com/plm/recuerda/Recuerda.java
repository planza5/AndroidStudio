package com.plm.recuerda;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Recuerda extends BroadcastReceiver {
    public static final String ACTION_EXECUTE_TASK = "com.example.myapplication.ACTION_EXECUTE_TASK";
    public static final long INTERVAL_HOUR = 60 * 60 * 1000L; // 1 hora en milisegundos
    public static final long INTERVAL_MINUTE = 1 * 60 * 1000L; // 1 hora en milisegundos

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) || ACTION_EXECUTE_TASK.equals(intent.getAction())) {

            // Ejecuta la tarea aquí
            // ...
            Log.d("PABLO","HELLO!!");
            // Programa la próxima alarma en una hora
            scheduleNextAlarm(context);
        }
    }

    private void scheduleNextAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, Recuerda.class).setAction(ACTION_EXECUTE_TASK), 0);
        long nextTriggerTime = System.currentTimeMillis() + INTERVAL_HOUR;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTriggerTime, pendingIntent);
    }
}
