package org.openmrs.client.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.openmrs.client.R;
import org.openmrs.client.activities.fragments.CustomFragmentDialog;
import org.openmrs.client.adapters.LocationArrayAdapter;
import org.openmrs.client.application.OpenMRS;
import org.openmrs.client.bundle.CustomDialogBundle;
import org.openmrs.client.dao.LocationDAO;
import org.openmrs.client.models.Location;
import org.openmrs.client.net.LocationManager;
import org.openmrs.client.utilities.ApplicationConstants;
import org.openmrs.client.utilities.FontsUtil;
import org.openmrs.client.utilities.ImageUtils;
import org.openmrs.client.utilities.ToastUtil;
import org.openmrs.client.utilities.URLValidator;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends ACBaseActivity {

    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginButton;
    private ProgressBar mSpinner;
    private Spinner mDropdownLocation;
    private LinearLayout mLoginFormView;
    private SparseArray<Bitmap> mBitmapCache;
    private static boolean mErrorOccurred;
    private static String mLastURL = "";
    private static List<Location> mLocationsList;
    private TextView urlTextView;
    private ImageView urlEdit;
    private LinearLayout mUrlField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view_layout);
      //  mScrollView = (ScrollView) findViewById(R.id.scroll);
        mUrlField = (LinearLayout) findViewById(R.id.urlField);
        mUsername = (EditText) findViewById(R.id.loginUsernameField);
        mUsername.setText(OpenMRS.getInstance().getUsername());
        mPassword = (EditText) findViewById(R.id.loginPasswordField);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLoginFields()) {
                    login();
                } else {
                    ToastUtil.showShortToast(getApplicationContext(),
                            ToastUtil.ToastType.ERROR,
                            R.string.login_dialog_login_or_password_empty);
                }
            }
        });
        mSpinner = (ProgressBar) findViewById(R.id.loginLoading);
        mLoginFormView = (LinearLayout) findViewById(R.id.loginFormView);
        mDropdownLocation = (Spinner) findViewById(R.id.locationSpinner);
        urlTextView = (TextView) findViewById(R.id.urlText);
        urlEdit = (ImageView) findViewById(R.id.urlEdit);
        if (mErrorOccurred || OpenMRS.getInstance().getServerUrl().equals(ApplicationConstants.EMPTY_STRING)) {
            showURLDialog();
        } else {
            urlTextView.setText(OpenMRS.getInstance().getServerUrl());
            urlEdit.setVisibility(View.VISIBLE);
            hideURLDialog();
        }
        FontsUtil.setFont((ViewGroup) findViewById(android.R.id.content));
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindDrawableResources();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindDrawableResources();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private boolean validateLoginFields() {
        return !(ApplicationConstants.EMPTY_STRING.equals(mUsername.getText().toString())
                || ApplicationConstants.EMPTY_STRING.equals(mPassword.getText().toString()));
    }

    public void onEditUrlCallback(View v) {
        showURLDialog();
    }

    public void showURLDialog() {
        mUrlField.setVisibility(View.INVISIBLE);
        CustomDialogBundle bundle = new CustomDialogBundle();
        bundle.setTitleViewMessage(getString(R.string.login_dialog_title));
        if (mLastURL.equals(ApplicationConstants.EMPTY_STRING)) {
            bundle.setEditTextViewMessage(OpenMRS.getInstance().getServerUrl());
        } else {
            bundle.setEditTextViewMessage(mLastURL);
        }
        bundle.setRightButtonText(getString(R.string.dialog_button_done));
        bundle.setRightButtonAction(CustomFragmentDialog.OnClickAction.SET_URL);
        if (!OpenMRS.getInstance().getServerUrl().equals(ApplicationConstants.EMPTY_STRING)) {

            bundle.setLeftButtonText(getString(R.string.dialog_button_cancel));
            bundle.setLeftButtonAction(CustomFragmentDialog.OnClickAction.DISMISS_URL_DIALOG);
        }
        createAndShowDialog(bundle, ApplicationConstants.DialogTAG.URL_DIALOG_TAG);
    }

    private void showInvalidURLDialog() {
        mErrorOccurred = true;
        Intent i = new Intent(this, DialogActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setAction(ApplicationConstants.DialogTAG.INVALID_URL_DIALOG_TAG);
        startActivity(i);
    }

    private void login() {
        mLoginFormView.setVisibility(View.GONE);
        mSpinner.setVisibility(View.VISIBLE);
        mAuthorizationManager.login(mUsername.getText().toString(), mPassword.getText().toString());
    }

    private void bindDrawableResources() {
        mBitmapCache = new SparseArray<Bitmap>();
        ImageView openMrsLogoImage = (ImageView) findViewById(R.id.openmrsLogo);
        createImageBitmap(R.drawable.openmrs_logo, openMrsLogoImage.getLayoutParams());
        createImageBitmap(R.drawable.ico_edit, urlEdit.getLayoutParams());
        openMrsLogoImage.setImageBitmap(mBitmapCache.get(R.drawable.openmrs_logo));
        urlEdit.setImageBitmap(mBitmapCache.get(R.drawable.ico_edit));
    }

    private void createImageBitmap(Integer key, ViewGroup.LayoutParams layoutParams) {
        if (mBitmapCache.get(key) == null) {
            mBitmapCache.put(key, ImageUtils.decodeBitmapFromResource(getResources(), key,
                    layoutParams.width, layoutParams.height));
        }
    }

    private void unbindDrawableResources() {
        if (null != mBitmapCache) {
            for (int i = 0; i < mBitmapCache.size(); i++) {
                Bitmap bitmap = mBitmapCache.valueAt(i);
                bitmap.recycle();
            }
        }
    }

    public void initLoginForm(List<Location> locationsList, String serverURL) {
        mErrorOccurred = false;
        mLastURL = ApplicationConstants.EMPTY_STRING;
        OpenMRS.getInstance().setServerUrl(serverURL);
        urlTextView.setText(OpenMRS.getInstance().getServerUrl());
        mUrlField.setVisibility(View.VISIBLE);
        urlEdit.setVisibility(View.VISIBLE);
        mLocationsList = locationsList;
        List<String> items = getLocationStringList(locationsList);
        final LocationArrayAdapter adapter = new LocationArrayAdapter(this, items);
        mDropdownLocation.setAdapter(adapter);

        mDropdownLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean mInitialized;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!mInitialized && position >= 0 && id >= 1) {
                    mInitialized = true;
                    adapter.notifyDataSetChanged();
                    mLoginButton.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mLoginButton.setEnabled(false);
        mSpinner.setVisibility(View.GONE);
        mLoginFormView.setVisibility(View.VISIBLE);
    }

    private List<String> getLocationStringList(List<Location> locationList) {
        List<String> list = new ArrayList<String>();
        list.add(getString(R.string.login_location_select));
        for (int i = 0; i < locationList.size(); i++) {
            list.add(locationList.get(i).getDisplay());
        }
        return list;
    }

    public void saveLocationsToDatabase() {
        OpenMRS.getInstance().setLocation(mDropdownLocation.getSelectedItem().toString());
        new LocationDAO().deleteAllLocations();
        for (int i = 0; i < mLocationsList.size(); i++) {
            new LocationDAO().saveLocation(mLocationsList.get(i));
        }
    }

    public void setUrl(String url) {
        mLastURL = url;
        URLValidator.ValidationResult result = URLValidator.validate(url);
        if (result.isURLValid()) {
            mSpinner.setVisibility(View.VISIBLE);
            mLoginFormView.setVisibility(View.GONE);
            LocationManager lm = new LocationManager(this);
            lm.getAvailableLocation(url);
        } else {
            showInvalidURLDialog();
        }
    }

    public void setErrorOccurred(boolean errorOccurred) {
        this.mErrorOccurred = errorOccurred;
    }

    public void hideURLDialog() {
        if (mLocationsList == null) {
            LocationManager lm = new LocationManager(this);
            lm.getAvailableLocation(OpenMRS.getInstance().getServerUrl());
        } else {
            initLoginForm(mLocationsList, OpenMRS.getInstance().getServerUrl());
        }
    }
}
