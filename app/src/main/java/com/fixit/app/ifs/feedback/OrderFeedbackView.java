package com.fixit.app.ifs.feedback;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Kostyantin on 8/17/2017.
 */

public interface OrderFeedbackView {
    Intent createIntent(Class<?> forClass);
    void startActivity(Intent intent);
    void transitionTo(Fragment fragment);
    String getString(int resId);
    String getString(int resId, Object... formatArgs);
    void closeApp();
    void finish();
}
