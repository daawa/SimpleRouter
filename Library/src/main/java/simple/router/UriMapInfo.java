package simple.router;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hzzhangzhenwei on 2017/3/2.
 */

public class UriMapInfo {
    private static String defaultScheme = "MY-APP";

    String componentName;
    String scheme;//? using pattern
    String host; //? using pattern

    String path;//? using pattern
    String pathParamPattern;

    /**
     * tag?id=123
     * id -> TagActivity.TAG_ARG_ID
     */

    Map<String, String> queryParamMap = new HashMap<>(2);

    public static UriMapInfo one() {
        return new UriMapInfo();
    }

    public static void setDefaultScheme(String scheme){
        defaultScheme = scheme;
    }

    public UriMapInfo() {
        scheme = defaultScheme;
    }

    public UriMapInfo scheme(String arg) {
        this.scheme = arg;
        return this;
    }

    public UriMapInfo host(String arg) {
        this.host = arg;
        return this;
    }

    public UriMapInfo path(String arg) {
        this.path = arg;
        return this;
    }

    public UriMapInfo pathParamPattern(String arg) {
        this.pathParamPattern = arg;
        return this;
    }

    public UriMapInfo componentName(String name) {
        componentName = name;
        return this;
    }

    public UriMapInfo addQueryMap(String fromUrl, String toNative) {
        this.queryParamMap.put(fromUrl, toNative);
        return this;
    }
}
