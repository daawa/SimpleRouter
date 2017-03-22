package simple.router;

import android.content.Context;
import android.content.Intent;

import java.util.List;
import java.util.Map;

import simple.router.pages.WakeAppActivity;

/**
 * Created by hzzhangzhenwei on 2017/3/16.
 */

public class WakeAppIntent extends TargetIntent {
    private static WakeAppIntent one;

    private WakeAppIntent(){}

    @Override
    public Intent intent(Context context, List<String> extractedPathParams, Map<String, String> extractedQueryParams) {
        return new Intent(context, WakeAppActivity.class);
    }


    public static WakeAppIntent getInstance(){
        if(one == null){
            one = new WakeAppIntent();
        }
        return one;
    }
}
