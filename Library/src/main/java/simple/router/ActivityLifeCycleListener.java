package simple.router;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by hzzhangzhenwei on 2017/3/16.
 */

class ActivityLifeCycleListener implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        PageChain.one().putNext(activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        PageChain.one().pop(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

}

