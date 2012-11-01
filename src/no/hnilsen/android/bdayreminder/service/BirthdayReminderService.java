package no.hnilsen.android.bdayreminder.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * User: Haakon
 * Date: 01.11.12
 * Time: 16:59
 */
public class BirthdayReminderService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
