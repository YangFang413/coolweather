package Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import Service.AutoUpdateService;

/**
 * Created by Administrator on 2016/8/12.
 */
public class AutoUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoUpdateService.class);
        context.startService(i);
    }
}
