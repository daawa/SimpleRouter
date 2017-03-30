package simple.router.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import simple.router.PageRouter;
import simple.router.TargetIntent;
import simple.router.WakeAppIntent;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String KEY_URL = "urlKey";

    @Override
    public void onReceive(Context context, Intent intent) {

        String url = intent.getStringExtra(KEY_URL);

        TargetIntent ti = PageRouter.parseIntent(url);
        if (ti != null) {
            PageRouter.route(context, ti);

        } else {
            WakeAppIntent.getInstance().go(context);

//            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            activityManager.moveTaskToFront(context.getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
        }

    }

}
