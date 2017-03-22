package simple.router.pages;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import simple.router.PageRouter;
import znwey.github.com.library.R;

/**
 * Created by hzzhangzhenwei on 2017/2/17.
 */

public class RouterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router_library_router);

        Uri uri = getIntent().getData();
        if(uri == null || TextUtils.isEmpty(uri.toString())){
            return;
        }

        finish();
        PageRouter.route(getApplication(), uri.toString());

    }
}
