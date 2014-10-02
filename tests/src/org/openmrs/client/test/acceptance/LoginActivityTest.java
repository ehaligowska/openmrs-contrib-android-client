package org.openmrs.client.test.acceptance;

import org.openmrs.client.test.acceptance.helpers.DashboardHelper;
import org.openmrs.client.test.acceptance.helpers.LoginHelper;
import org.openmrs.client.test.acceptance.helpers.SettingsHelper;

public class LoginActivityTest extends OpenMRSAcceptanceTestCase {
    public static final String USER_ADMIN = "admin";
    public static final String PASSWORD_ADMIN = "Admin123";
    public static final String DEMO_SERVER_URL = "http://demo.openmrs.org/openmrs";

    public void testLoginAndLogout() {
        DashboardHelper dashboardHelper = new LoginHelper(mSolo).
                loginAndMoveToDashboard(USER_ADMIN, PASSWORD_ADMIN, DEMO_SERVER_URL);
        SettingsHelper settingsHelper = dashboardHelper.navigateToSettings();
        settingsHelper.logoutAction();
    }

}
