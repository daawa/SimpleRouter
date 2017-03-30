package simple.router.sample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

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

        PageRouter.registerPageEntry(UriMapInfo.one().scheme("myapp").host("testhost").pathParamPattern("/(\\w+)/(\\w+)").addQueryMap("url-key", "ARG-KEY"),
                new TargetIntent() {
                    @Override
                    public Intent intent(Context context, List<String> extractedPathParams, Map<String, String> extractedQueryParams) {
                        Intent intent = new Intent(getApplicationContext(),TestActivity.class);
                        intent.putExtra(TestActivity.PATH_KEY1, extractedPathParams.get(0));
                        intent.putExtra(TestActivity.PATH_KEY2, extractedPathParams.get(1));
                        intent.putExtra(TestActivity.QUERY_KEY, extractedQueryParams.get(TestActivity.QUERY_KEY));
                        return intent;
                    }
                });
    }

    public static void sendNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        String title = "Test Message";
        builder.setTicker(title);
        builder.setContentTitle(title);
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
        notificationManager.notify(notification_id++, notification);
    }
}
