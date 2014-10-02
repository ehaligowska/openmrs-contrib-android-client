package org.openmrs.client.test.acceptance.helpers;


import com.robotium.solo.Solo;

import org.openmrs.client.activities.DashboardActivity;
import org.openmrs.client.activities.SettingsActivity;

import static junit.framework.Assert.assertTrue;

public class DashboardHelper extends TestHelper {

    public DashboardHelper(Solo solo) {
        super(solo);
        solo.waitForActivity(DashboardActivity.class.getSimpleName(), TIMEOUT_TEN_SECOND);
        assertTrue(getSolo().getCurrentActivity() instanceof DashboardActivity);
    }

    public SettingsHelper navigateToSettings() {
        getSolo().clickOnActionBarItem(org.openmrs.client.R.id.action_settings);
        getSolo().waitForText("Settings", 1, TIMEOUT_FIVE_SECOND, true);
        getSolo().clickOnText("Settings", 1, true);
        getSolo().waitForActivity(SettingsActivity.class.getSimpleName(), TIMEOUT_FIVE_SECOND);
        return new SettingsHelper(getSolo());
    }

}
