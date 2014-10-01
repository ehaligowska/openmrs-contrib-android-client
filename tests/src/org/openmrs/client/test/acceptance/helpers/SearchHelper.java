package org.openmrs.client.test.acceptance.helpers;

import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

import org.openmrs.client.R;

public final class SearchHelper {

    private SearchHelper() {
    }

    public static void doSearch(Solo solo, String query, String searchHint) throws java.lang.Exception {
        solo.clickOnActionBarItem(R.id.action_search);
        solo.sleep(WaitHelper.TIMEOUT_ONE_SECOND);
        WaitHelper.waitForText(solo, searchHint);
        EditText search = (EditText) solo.getView(org.openmrs.client.R.id.search_src_text);
        solo.enterText(search, query);
        solo.sleep(WaitHelper.TIMEOUT_ONE_SECOND);
        solo.sendKey(Solo.ENTER);
    }
}
