package znwey.github.com.pagerouter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.Map;

import simple.router.PageRouter;
import simple.router.TargetIntent;
import simple.router.UriMapInfo;

public class MainActivity extends AppCompatActivity {

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

        PageRouter.registerPageEntry(new UriMapInfo(), new TargetIntent() {
            @Override
            public Intent intent(Context context, List<String> extractedPathParams, Map<String, String> extractedQueryParams) {
                return null;
            }
        });
    }
}
