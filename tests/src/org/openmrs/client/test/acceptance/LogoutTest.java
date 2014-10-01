package org.openmrs.client.test.acceptance;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

import org.openmrs.client.activities.DashboardActivity;
import org.openmrs.client.activities.LoginActivity;
import org.openmrs.client.activities.SettingsActivity;
import org.openmrs.client.R;
import org.openmrs.client.test.acceptance.helpers.ButtonHelper;
import org.openmrs.client.test.acceptance.helpers.LoginHelper;
import org.openmrs.client.test.acceptance.helpers.WaitHelper;

public class LogoutTest extends ActivityInstrumentationTestCase2<DashboardActivity> {

    private Solo solo;

    public LogoutTest() {
        super(DashboardActivity.class);
    }

    @Override
    public void setUp() throws java.lang.Exception {
        super.setUp();

        solo = new Solo(getInstrumentation());
        getActivity();
        getInstrumentation().waitForIdleSync();
        if (WaitHelper.waitForActivity(solo, LoginActivity.class, WaitHelper.TIMEOUT_THIRTY_SECOND)) {
            LoginHelper.login(solo);
        }
    }

    public void testLogout() throws Exception {
        //open menu
        solo.clickOnActionBarItem(org.openmrs.client.R.id.action_settings);

        assertTrue(WaitHelper.waitForActivity(solo, SettingsActivity.class));

        solo.clickInList(3);

        //wait for Logout dialog
        assertTrue(WaitHelper.waitForText(solo, "Logging out"));

        //Click on Cancel button
        ButtonHelper.click(solo, R.id.dialogFormButtonsCancelButton, "Cancel");

        solo.assertCurrentActivity("Wrong activity. SettingsActivity expected", SettingsActivity.class);

        solo.clickInList(3);

        //wait for Logout dialog
        assertTrue(WaitHelper.waitForText(solo, "Logging out"));

        //Click on Logout button
        ButtonHelper.click(solo, R.id.dialogFormButtonsSubmitButton, "Logout");

        assertTrue(solo.waitForActivity(LoginActivity.class, WaitHelper.TIMEOUT_THIRTY_SECOND));
    }

    @Override
    public void tearDown() throws java.lang.Exception  {
        solo.finishOpenedActivities();
        super.tearDown();
    }
}
