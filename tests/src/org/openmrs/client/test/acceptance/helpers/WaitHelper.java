package org.openmrs.client.test.acceptance.helpers;

import android.util.Log;

import com.jayway.android.robotium.solo.Solo;

public final class WaitHelper {
    public static final int TIMEOUT_ONE_SECOND = 1000;
    public static final int TIMEOUT_THIRTY_SECOND = 30 * TIMEOUT_ONE_SECOND;
    public static final long TIMEOUT_TWO_MINUTES = 120 * TIMEOUT_ONE_SECOND;

    public static final String TAG = "OpenMRSTest";
    private static final String TIME = "Time: ";
    private static final String RESULT = ", Result: ";

    private WaitHelper() {
    }

    public static boolean waitForText(Solo solo, String txt) throws java.lang.Exception {
        return waitForText(solo, txt, TIMEOUT_TWO_MINUTES);
    }

    public static boolean waitForText(Solo solo, String txt, long timeout) throws java.lang.Exception {
        boolean result = false;
        int sec = 0;
        while (!result && sec * TIMEOUT_ONE_SECOND < timeout) {
            result = solo.waitForText(txt, 1, TIMEOUT_THIRTY_SECOND);
            sec = sec + TIMEOUT_THIRTY_SECOND / TIMEOUT_ONE_SECOND;
            Log.d(TAG, TIME + sec + "s., WaitForText: " + txt + RESULT + result);
        }
        return result;
    }

    public static boolean waitForActivity(Solo solo, java.lang.Class<? extends android.app.Activity> activityClass) throws java.lang.Exception {
        return waitForActivity(solo, activityClass, TIMEOUT_TWO_MINUTES);
    }

    public static boolean waitForActivity(Solo solo, java.lang.Class<? extends android.app.Activity> activityClass, long timeout) throws java.lang.Exception {
        boolean result = false;
        int sec = 0;
        while (!result && sec * TIMEOUT_ONE_SECOND < timeout) {
            result = solo.waitForActivity(activityClass, TIMEOUT_THIRTY_SECOND);
            sec = sec + TIMEOUT_THIRTY_SECOND / TIMEOUT_ONE_SECOND;
            Log.d(TAG, TIME + sec + "s., WaitForActivity: " + activityClass + RESULT + result);
        }
        return result;
    }

}
