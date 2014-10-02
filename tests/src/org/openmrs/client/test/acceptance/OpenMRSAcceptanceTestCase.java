package org.openmrs.client.test.acceptance;

import android.test.ActivityInstrumentationTestCase2;
import android.view.WindowManager;


import com.robotium.solo.Solo;

import org.openmrs.client.activities.DashboardActivity;
import org.openmrs.client.application.OpenMRS;

public class OpenMRSAcceptanceTestCase extends ActivityInstrumentationTestCase2<DashboardActivity> {
    private static final String PACKAGE_NAME = "org.openmrs.client";

    public OpenMRSAcceptanceTestCase() {
        super(PACKAGE_NAME, DashboardActivity.class);
    }

    protected Solo mSolo;

    @Override
    public void setUp() throws java.lang.Exception {
        super.setUp();

        mSolo = new Solo(getInstrumentation());
        getInstrumentation().waitForIdleSync();

        getActivity();
        unlockScreen();
    }

    @Override
    public void tearDown() throws java.lang.Exception {
        mSolo.finishOpenedActivities();
        OpenMRS.getInstance().clearUserPreferencesDataWhenLogout();
        super.tearDown();
    }

    public void unlockScreen() {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mSolo.getCurrentActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            }
        });
    }
}
