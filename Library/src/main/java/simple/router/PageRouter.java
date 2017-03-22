package simple.router;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by hzzhangzhenwei on 2016/11/21.
 */

public class PageRouter {
    private static List<UriMapInfo> sUriMap = new ArrayList<>(50);
    private static Map<String, TargetIntent> pageMap = new HashMap<>(50);

    private PageRouter() {
    }


    public static void init(Application app, RouteMap rm) {
        app.registerActivityLifecycleCallbacks(new ActivityLifeCycleListener());
        rm.registerRoutingMap();

    }

    public static void registerPageEntry(UriMapInfo uriInfo, TargetIntent intent){
        sUriMap.add(uriInfo);
        if(TextUtils.isEmpty(intent.getComponent())){
            intent.component(uriInfo.componentName);
        }
        pageMap.put(uriInfo.componentName, intent);
    }

    public static void route(Context context, String link) {
        if (TextUtils.isEmpty(link))
            return;

        TargetIntent targetIntent = parseIntent(link);
        route(context, targetIntent);

    }

    public static void route(Context context, TargetIntent targetIntent) {
        if (targetIntent != null) {
            targetIntent.go(context);
        } else {
            WakeAppIntent.getInstance().go(context);
        }
    }

    public static TargetIntent parseIntent(String sUri) {
        if (TextUtils.isEmpty(sUri)) {
            return null;
        }
        try {
            sUri = URLDecoder.decode(sUri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }


        Uri uri = Uri.parse(sUri);
        for (UriMapInfo info : sUriMap) {
            List<String> pathParams = null;
            Map<String, String> queryParams = null;
            if (match(info.scheme, uri.getScheme()) && match(info.host, uri.getHost())) {
                if (!TextUtils.isEmpty(info.pathParamPattern)) {
                    Matcher m = matchReg(uri.getPath(), info.pathParamPattern);
                    if (m == null || !m.find()) {
                        continue;
                    }
                    int count = m.groupCount();
                    pathParams = new ArrayList<>(count);
                    for (int i = 0; i < count; i++) {
                        try {
                            pathParams.add(m.group(i + 1));
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                } else if (!match(info.path, uri.getPath())) {
                    continue;
                }

                if(uri.getQueryParameterNames() != null){
                    for(String key : uri.getQueryParameterNames()){
                        String value = uri.getQueryParameter(key);
                        if (!info.queryParamMap.isEmpty()) {
                            key = info.queryParamMap.get(key);
                        }
                        queryParams.put(key,value);
                    }
                }

                TargetIntent targetIntent = pageMap.get(info.componentName);
                if (targetIntent != null) {
                    targetIntent.sourceUri(sUri);
                    targetIntent.pathParams(pathParams);
                    targetIntent.queryParams(queryParams);
                    return targetIntent;
                }

            }

        }

        return null;

    }


    private static boolean match(String temp, String arg2) {
        if (TextUtils.equals(temp, arg2))
            return true;
        if (TextUtils.isEmpty(temp))
            return true;

        return false;
    }

    private static Matcher matchReg(String arg1, String reg) {
        if (TextUtils.isEmpty(arg1))
            return null;
        Pattern p = Pattern.compile(reg);
        return p.matcher(arg1);
    }

    public static String appendParamToUri(String uri, String key, String value) {
        if (TextUtils.isEmpty(uri)) {
            return "";
        }
        try {
            URI oldUri = new URI(uri);
            String newQuery = oldUri.getQuery();
            StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            if (sb.length() > 0) {
                if (newQuery == null) {
                    newQuery = sb.toString();
                } else {
                    newQuery += "&" + sb.toString();
                }
            } else {
                return uri;
            }
            String newUrl = new URI(oldUri.getScheme(), oldUri.getAuthority(),
                    oldUri.getPath(), newQuery, oldUri.getFragment()).toString();
            return newUrl;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }

    public static boolean isAppForeground(Context context) {
        boolean result = false;
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    result = true;
                } else {
                    result = false;
                }
            }
        }
        return result;
    }


}
