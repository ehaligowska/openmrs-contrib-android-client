package org.openmrs.client.test.acceptance;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.CheckBox;

import com.jayway.android.robotium.solo.Solo;

import org.openmrs.client.activities.FindPatientsActivity;
import org.openmrs.client.R;
import org.openmrs.client.activities.FindPatientsSearchActivity;
import org.openmrs.client.activities.LoginActivity;
import org.openmrs.client.databases.OpenMRSSQLiteOpenHelper;
import org.openmrs.client.test.acceptance.helpers.LoginHelper;
import org.openmrs.client.test.acceptance.helpers.SearchHelper;
import org.openmrs.client.test.acceptance.helpers.WaitHelper;

public class FindPatientsDetailsTest extends ActivityInstrumentationTestCase2<FindPatientsActivity> {

    private Solo solo;
    private static final String PATIENT_EXIST = "Armstrong";
    private static final String PATIENT_NO_EXIST = "Enrique";
    private static boolean isAuthenticated;

    public FindPatientsDetailsTest() {
        super(FindPatientsActivity.class);
    }

    @Override
    public void setUp() throws java.lang.Exception {
        super.setUp();
        if (!isAuthenticated) {
            getInstrumentation().getTargetContext().deleteDatabase(OpenMRSSQLiteOpenHelper.DATABASE_NAME);
        }
        solo = new Solo(getInstrumentation());
        getActivity();
        getInstrumentation().waitForIdleSync();

        if (!isAuthenticated) {
            if (!solo.waitForActivity(LoginActivity.class, WaitHelper.TIMEOUT_THIRTY_SECOND)) {
                ((FindPatientsActivity) solo.getCurrentActivity()).moveUnauthorizedUserToLoginScreen();
            }
            assertTrue(solo.waitForActivity(LoginActivity.class, WaitHelper.TIMEOUT_THIRTY_SECOND));

            LoginHelper.login(solo);
            isAuthenticated = true;
            assertTrue(solo.waitForActivity(FindPatientsActivity.class, WaitHelper.TIMEOUT_THIRTY_SECOND));
        }
    }

    public void testPatientNotExist() throws Exception {
        SearchHelper.doSearch(solo, PATIENT_NO_EXIST, "Patient name");
        assertTrue(solo.waitForActivity(FindPatientsSearchActivity.class, WaitHelper.TIMEOUT_THIRTY_SECOND));
        assertTrue(WaitHelper.waitForText(solo, "No results found for query \"" +  PATIENT_NO_EXIST + "\""));

        solo.goBackToActivity("FindPatientsActivity");
        assertTrue(solo.waitForActivity(FindPatientsActivity.class, WaitHelper.TIMEOUT_THIRTY_SECOND));
    }

    public void testSearchPatient() throws Exception {
        SearchHelper.doSearch(solo, PATIENT_EXIST, "Patient name");

        assertTrue(solo.waitForActivity(FindPatientsSearchActivity.class, WaitHelper.TIMEOUT_THIRTY_SECOND));
        assertTrue(WaitHelper.waitForText(solo, PATIENT_EXIST));

        solo.goBackToActivity("FindPatientsActivity");
        assertTrue(solo.waitForActivity(FindPatientsActivity.class, WaitHelper.TIMEOUT_THIRTY_SECOND));

        //wait for finish requests send to server.
        solo.sleep(WaitHelper.TIMEOUT_THIRTY_SECOND);
    }

    public void testSearchPatientAndSave() throws Exception {
        SearchHelper.doSearch(solo, PATIENT_EXIST, "Patient name");

        assertTrue(solo.waitForActivity(FindPatientsSearchActivity.class, WaitHelper.TIMEOUT_THIRTY_SECOND));
        assertTrue(WaitHelper.waitForText(solo, PATIENT_EXIST));

        CheckBox isPatientSave = (CheckBox) solo.getView(R.id.offlineCheckbox);
        assertTrue(WaitHelper.waitForText(solo, "Download"));
        assertFalse(isPatientSave.isChecked());

        solo.clickOnCheckBox(0);

        assertTrue(WaitHelper.waitForText(solo, "Available offline"));

        solo.goBackToActivity("FindPatientsActivity");
        assertTrue(solo.waitForActivity(FindPatientsActivity.class, WaitHelper.TIMEOUT_THIRTY_SECOND));

        assertTrue(WaitHelper.waitForText(solo, PATIENT_EXIST));

        //wait for finish all requests sent to server.
        solo.sleep(WaitHelper.TIMEOUT_THIRTY_SECOND);
    }

    @Override
    public void tearDown() throws java.lang.Exception  {
        solo.finishOpenedActivities();
        super.tearDown();
    }

}
