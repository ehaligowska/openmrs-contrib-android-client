package org.openmrs.client.test.acceptance.helpers;


import com.robotium.solo.Solo;

public class TestHelper {
    private Solo mSolo;

    public static final int TIMEOUT_ONE_SECOND = 1000;
    public static final int TIMEOUT_FIVE_SECOND = 5 * TIMEOUT_ONE_SECOND;
    public static final int TIMEOUT_TEN_SECOND = 10 * TIMEOUT_ONE_SECOND;

    public TestHelper(Solo solo) {
        this.mSolo = solo;
    }

    public Solo getSolo() {
        return mSolo;
    }
}
