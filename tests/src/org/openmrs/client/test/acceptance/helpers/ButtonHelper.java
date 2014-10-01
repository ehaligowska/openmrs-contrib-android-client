package org.openmrs.client.test.acceptance.helpers;

import android.util.Log;
import android.view.View;

import com.jayway.android.robotium.solo.Solo;

public final class ButtonHelper {

    public static final String TAG = "OpenMRSTest";

    private ButtonHelper() {
    }

    public static void click(Solo solo, int id, String buttonTxt) throws java.lang.Exception {
        if (WaitHelper.waitForText(solo, buttonTxt)) {
            solo.clickOnText(buttonTxt);
            Log.d(TAG, "ClickOnText:" + buttonTxt);
        } else {
            View button = solo.getView(id);
            solo.clickOnView(button);
            Log.d(TAG, "ClickOnView:" + buttonTxt);
        }
    }
}
