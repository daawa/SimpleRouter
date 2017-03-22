package simple.router.pages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import znwey.github.com.library.R;

public class WakeAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router_library_dummy);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.activity_router_library_dummy).postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 600);

    }
}
