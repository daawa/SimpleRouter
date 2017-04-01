package simple.router.sample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import simple.router.PageRouter;
import simple.router.TargetIntent;
import simple.router.UriMapInfo;
import znwey.github.com.pagerouter.R;

public class MainActivity extends AppCompatActivity {

    private static final String BROADCAST = "meixue.163.notification";
    static int notification_id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        PageRouter.init(getApplication(), new RouteMap() {
//            @Override
//            public void registerRoutingMap() {
//
//            }
//        });

        PageRouter.registerPageEntry(UriMapInfo.one().scheme("myapp").host("testhost").pathParamPattern("/(\\w+)/(\\w+)").addQueryMap("url-key", TestActivity.QUERY_KEY),
                new TargetIntent() {
                    @Override
                    public Intent intent(Context context, List<String> extractedPathParams, Map<String, String> extractedQueryParams) {
                        Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                        intent.putExtra(TestActivity.PATH_KEY1, extractedPathParams.get(0));
                        intent.putExtra(TestActivity.PATH_KEY2, extractedPathParams.get(1));
                        intent.putExtra(TestActivity.QUERY_KEY, extractedQueryParams.get(TestActivity.QUERY_KEY));
                        return intent;
                    }
                });

        findViewById(R.id.send_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification(getApplicationContext());
            }
        });

        findViewById(R.id.check_enabled).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean enabled = isNotificationEnabled();

                //if disabled , toast can't show out either on MIUI
                Toast.makeText(getApplicationContext(), enabled? " enabled:" : "disabled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        String title = "Test Message";
        builder.setTicker(title);
        builder.setContentTitle(title);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentText("TEST");
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)).setColor(Color.parseColor("#EAA935"));

        Intent broadcastIntent = new Intent(BROADCAST);
        String url = "myapp://testhost/arg1/arg2?url-key=url-value";
        broadcastIntent.putExtra(NotificationReceiver.KEY_URL, url);

        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(context, notification_id++, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        notificationManager.notify("myApp", notification_id++, notification);

    }

    private boolean isNotificationEnabled() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return notificationManager.areNotificationsEnabled();
        } else {
            return NotificationManagerCompat.from(this).areNotificationsEnabled();
        }

    }
}
