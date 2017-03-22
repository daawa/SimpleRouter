package simple.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hzzhangzhenwei on 2017/3/2.
 */

public abstract class TargetIntent {
    private String sourceUri;
    private String component;
    private List<String> pathParams;

    private Map<String, String> queryParams;

    public int type;

    private Map<String, String> extraParams = new HashMap<>(3);

    public TargetIntent(String name) {
        this.component = name;
    }

    public TargetIntent() {

    }

    public abstract Intent intent(Context context, List<String> extractedPathParams, Map<String, String> extractedQueryParams);

    public TargetIntent component(String compo){
        this.component = compo;
        return this;
    }

    public TargetIntent type(int type){
        this.type = type;
        return this;
    }

    public TargetIntent sourceUri(String link){
        this.sourceUri = link;
        return this;
    }

    protected TargetIntent pathParams(List<String> pathParams) {
        this.pathParams = pathParams;
        return this;
    }

    protected TargetIntent queryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
        return this;
    }

    public void extraParam(String key, String value) {
        extraParams.put(key, value);
    }

    private Intent appendExtra(Intent intent) {
        if (intent == null) return null;
        for (Map.Entry<String, String> entry : extraParams.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        extraParams.clear();
        return intent;
    }

    public void go(Context context) {

        if (!(context instanceof Activity)) {
            Activity cur = PageChain.one().getTop();
            if (cur != null) {
                context = cur;
            }
        }

        Intent intent = appendExtra(intent(context, pathParams, queryParams));
        if (intent != null) {
            if (context instanceof Activity) {
                context.startActivity(intent);
            } else {
                context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                Intent mainIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Intent[] intents = {mainIntent, intent};
                context.startActivities(intents);
            }
        }
    }

    public String getSourceUri() {
        return sourceUri;
    }

    public String getComponent() {
        return component;
    }

    public List<String> getPathParams() {
        return pathParams;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public int getType() {
        return type;
    }

    public Map<String, String> getExtraParams() {
        return extraParams;
    }
}