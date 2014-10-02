package org.openmrs.client.test.acceptance.helpers;


import com.robotium.solo.Solo;

import org.openmrs.client.activities.LoginActivity;

import static junit.framework.Assert.assertTrue;

public final class LoginHelper extends TestHelper {

    public static final String LOGIN_BUTTON = "Login";
    public static final String DONE_BUTTON = "Done";
    public static final String FIND_PATIENTS = "Find Patients";
    public static final String ACTIVE_VISITS = "Active Visits";

    public LoginHelper(Solo solo) {
        super(solo);
        solo.waitForActivity(LoginActivity.class.getSimpleName(), TIMEOUT_TEN_SECOND);
        assertTrue(getSolo().getCurrentActivity() instanceof LoginActivity);
    }

    public DashboardHelper loginAndMoveToDashboard(String username, String password, String url) {
        enterLoginAndPassword(username, password, url);
        assertTrue(getSolo().waitForText(FIND_PATIENTS, 1, TIMEOUT_TEN_SECOND));
        assertTrue(getSolo().waitForText(ACTIVE_VISITS, 1, TIMEOUT_ONE_SECOND));
        return new DashboardHelper(getSolo());
    }

    private void enterLoginAndPassword(String login, String password, String url) {
        getSolo().waitForView(org.openmrs.client.R.id.loginPasswordField, 1, TIMEOUT_FIVE_SECOND, true);
        getSolo().clearEditText(0);
        getSolo().typeText(0, login);
        getSolo().clearEditText(1);
        getSolo().typeText(1, password);
        getSolo().clickOnButton(LOGIN_BUTTON);
        getSolo().waitForDialogToOpen(TIMEOUT_TEN_SECOND);
        getSolo().clearEditText(0);
        getSolo().typeText(0, url);
        getSolo().clickOnButton(DONE_BUTTON);
    
    }

}
