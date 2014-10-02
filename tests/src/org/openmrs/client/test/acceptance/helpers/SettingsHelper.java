package org.openmrs.client.test.acceptance.helpers;


import com.robotium.solo.Solo;

import org.openmrs.client.activities.LoginActivity;
import org.openmrs.client.activities.SettingsActivity;

import static junit.framework.Assert.assertTrue;

public class SettingsHelper extends TestHelper {

    public static final String LOGOUT_TEXT = "Logout";

    public SettingsHelper(Solo solo) {
        super(solo);
        solo.waitForActivity(SettingsActivity.class.getSimpleName(), TIMEOUT_TEN_SECOND);
        assertTrue(getSolo().getCurrentActivity() instanceof SettingsActivity);
    }

    public LoginHelper logoutAction() {
        getSolo().waitForText(LOGOUT_TEXT, 1, TIMEOUT_FIVE_SECOND, true);
        getSolo().clickOnText(LOGOUT_TEXT, 0, true);
        getSolo().waitForDialogToOpen(TIMEOUT_TEN_SECOND);
        getSolo().clickOnButton(LOGOUT_TEXT);
        getSolo().waitForActivity(LoginActivity.class.getSimpleName(), TIMEOUT_TEN_SECOND);
        return new LoginHelper(getSolo());
    }
}
