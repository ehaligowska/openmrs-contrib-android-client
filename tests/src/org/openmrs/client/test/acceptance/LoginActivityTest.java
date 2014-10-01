package org.openmrs.client.test.acceptance;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

import org.openmrs.client.activities.LoginActivity;
import org.openmrs.client.R;
import org.openmrs.client.test.acceptance.helpers.ButtonHelper;
import org.openmrs.client.test.acceptance.helpers.LoginHelper;
import org.openmrs.client.test.acceptance.helpers.WaitHelper;

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;
    private static final String WRONG_SERVER_URL = "http://openmrs-ac-ci.soldevelo.com:8080/openmrs-standalone";
    private static final String WRONG_PASSWORD = "Testuser";
    private static final String EMPTY_FIELD = "Login and password can not be empty.";

    public LoginActivityTest() {
        super(LoginActivity.class);
    }

    @Override
    public void setUp() throws java.lang.Exception {
        super.setUp();

        solo = new Solo(getInstrumentation());
        getActivity();
        getInstrumentation().waitForIdleSync();
    }

    public void testEmptyFields() throws Exception {
        EditText loginUsernameField = (EditText) solo.getView(R.id.loginUsernameField);
        EditText loginPasswordField = (EditText) solo.getView(R.id.loginPasswordField);

        //Empty both fields
        solo.clearEditText(loginUsernameField);
        solo.clearEditText(loginPasswordField);

        //Click on Login button
        ButtonHelper.click(solo, R.id.loginButton, LoginHelper.LOGIN_BUTTON);

        assertTrue(WaitHelper.waitForText(solo, EMPTY_FIELD));

        //Empty password field
        solo.clearEditText(loginUsernameField);
        solo.clearEditText(loginPasswordField);
        solo.enterText(loginUsernameField, LoginHelper.LOGIN);

        //Click on Login button
        ButtonHelper.click(solo, R.id.loginButton, LoginHelper.LOGIN_BUTTON);

        assertTrue(WaitHelper.waitForText(solo, EMPTY_FIELD));

        //Empty login field
        solo.clearEditText(loginUsernameField);
        solo.clearEditText(loginPasswordField);
        solo.enterText(loginPasswordField, LoginHelper.PASSWORD);

        //Click on Login button
        ButtonHelper.click(solo, R.id.loginButton, LoginHelper.LOGIN_BUTTON);

        assertTrue(WaitHelper.waitForText(solo, EMPTY_FIELD));
    }

    public void testLoginFailed() throws Exception {
        //Write login
        EditText loginUsernameField = (EditText) solo.getView(R.id.loginUsernameField);
        solo.clearEditText(loginUsernameField);
        solo.enterText(loginUsernameField, LoginHelper.LOGIN);

        //Write password
        EditText loginPasswordField = (EditText) solo.getView(R.id.loginPasswordField);
        solo.clearEditText(loginPasswordField);
        solo.enterText(loginPasswordField, WRONG_PASSWORD);

        //Click on Login button
        ButtonHelper.click(solo, R.id.loginButton, LoginHelper.LOGIN_BUTTON);

        //Write url
        EditText urlField = (EditText) solo.getView(R.id.openmrsEditText);
        solo.clearEditText(urlField);
        solo.enterText(urlField, LoginHelper.SERVER_URL);

        //Click on Done button
        ButtonHelper.click(solo, R.id.dialogFormButtonsSubmitButton, LoginHelper.DONE_BUTTON);

        assertTrue(WaitHelper.waitForText(solo, "Your user name or password may be incorrect. Please try again."));
    }

    public void testWrongUrl() throws Exception {
        //Write login
        EditText loginUsernameField = (EditText) solo.getView(R.id.loginUsernameField);
        solo.clearEditText(loginUsernameField);
        solo.enterText(loginUsernameField, LoginHelper.LOGIN);

        //Write password
        EditText loginPasswordField = (EditText) solo.getView(R.id.loginPasswordField);
        solo.clearEditText(loginPasswordField);
        solo.enterText(loginPasswordField, LoginHelper.PASSWORD);

        //Click on Login button
        ButtonHelper.click(solo, R.id.loginButton, LoginHelper.LOGIN_BUTTON);

        //Write wrong url
        EditText urlField = (EditText) solo.getView(R.id.openmrsEditText);
        solo.clearEditText(urlField);
        solo.enterText(urlField, WRONG_SERVER_URL);

        //Click on Done button
        ButtonHelper.click(solo, R.id.dialogFormButtonsSubmitButton, LoginHelper.DONE_BUTTON);

        assertTrue(WaitHelper.waitForText(solo, "Cancel"));
    }

    public void testLogin() throws Exception {
        assertTrue(LoginHelper.login(solo));
    }

    @Override
    public void tearDown() throws java.lang.Exception  {
        solo.finishOpenedActivities();
        super.tearDown();
    }
}
