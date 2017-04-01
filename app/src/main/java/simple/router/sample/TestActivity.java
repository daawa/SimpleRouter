package simple.router.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import znwey.github.com.pagerouter.R;

public class TestActivity extends AppCompatActivity {

    public static String PATH_KEY1= "key1";
    public static String PATH_KEY2 = "key2";
    public static String QUERY_KEY = "test-key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();
        String text = PATH_KEY1 + " : " + intent.getStringExtra(TestActivity.PATH_KEY1)
                + "\n" + PATH_KEY2 + " : " + intent.getStringExtra(TestActivity.PATH_KEY2)
                + "\n" + QUERY_KEY + " : " + intent.getStringExtra(TestActivity.QUERY_KEY);

        ((TextView)findViewById(R.id.text)).setText(text);
    }
}
