package simple.router;

import android.app.Activity;
import android.util.Log;

import java.lang.ref.WeakReference;

import znwey.github.com.library.BuildConfig;

/**
 * Created by hzzhangzhenwei on 2016/12/30.
 */

public class PageChain {
    private static final String TAG = PageChain.class.getSimpleName();
    private boolean debug = false;

    private static PageChain one;
    private WeakActivity root;
    private WeakActivity head;

    public static PageChain one() {
        if (one == null)
            one = new PageChain();
        return one;
    }

    private PageChain() {
    }

    public void setDebug(boolean debug){
        this.debug = debug;
    }

    public void putNext(Activity activity) {
        log(TAG, "putNext:" + activity.getClass().getSimpleName());
        if (root == null) {
            root = weakActivity(activity);
            head = root;
        } else {
            WeakActivity cur = weakActivity(activity);
            if (head == null) {
                head = cur;
                head.last = root;
            } else {
                cur.last = head;
                head = cur;
            }
        }

        dump();
    }

    public Activity getTop() {
        dump();
        log(TAG, "getTop ===:");
        if (head == null) {
            log(TAG, "TOP: head is null");
            return null;
        }
        Activity top = head.wActivity.get();
        while (top == null || top.isFinishing()) {
            head = head.last;
            if (head == null) {
                log(TAG, "TOP: head is null");
                return null;
            }
            top = head.wActivity.get();
        }

        log(TAG, "TOP:" + (top != null ? top.getClass().getSimpleName() : " null"));

        return top;
    }

    public void pop(Activity activity) {
        log(TAG, "pop:" + activity.getClass().getSimpleName());
        if (head == null) return;
        do {
            Activity top = head.wActivity.get();
            if (top == activity || top == null) {
                head = head.last;
            } else {
                return;
            }
        } while (head != null);

        dump();
    }

    private void dump() {
        if (!BuildConfig.DEBUG) return;

        log(TAG, "dumping start..");
        WeakActivity tmp = head == null ? root : head;
        int i = 0;
        while (tmp != null) {
            log(TAG, "" + (i++) + " " + tmp.wActivity.get() + " ,name=" + tmp.name);
            tmp = tmp.last;
        }
        log(TAG, "dumping end..");
    }

    private void log(String tag, String msg) {
        //if (BuildConfig.DEBUG)
        if(debug)
            Log.v(tag, msg);
    }

    private WeakActivity weakActivity(Activity activity) {
        WeakActivity weakActivity = new WeakActivity(new WeakReference<Activity>(activity), null);
        return weakActivity;
    }

    private static class WeakActivity {
        WeakActivity(WeakReference<Activity> cur, WeakActivity last) {
            wActivity = cur;
            Object o = cur.get();
            name = o != null ? o.getClass().getName() : null;

            this.last = last;
        }

        public WeakReference<Activity> wActivity;
        public String name;
        WeakActivity last;
    }
}
