package com.jenky.codebuddy.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jenky.codebuddy.R;
import com.jenky.codebuddy.api.Callback;
import com.jenky.codebuddy.api.Request;
import com.jenky.codebuddy.util.AppController;
import com.jenky.codebuddy.util.IntentFactory;
import com.jenky.codebuddy.util.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogIn;
    private Button buttonSignUp;
    private Toolbar toolbar;
    private ProgressBar progressBar;


    private Callback logInCallback = new Callback() {
        @Override
        public void onSuccess(JSONObject result) throws JSONException {
            AppController.getInstance().getPreferences().setSessionToken(result.getString("responseMessage"));
            AppController.getInstance().getPreferences().setUserName(editTextEmail.getText().toString());
            logIn();
        }

        @Override
        public void onFailed(JSONObject result) throws JSONException {
            Toast.makeText(AppController.getInstance(), result.getString("responseMessage"), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppController.getInstance().getPreferences().hasCredentials()) {
            setContentView(R.layout.activity_log_in);
            setViews();
            setSupportActionBar(toolbar);
            setTitle(R.string.app_name);
        } else {
            logIn();
        }
    }

    private void setViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        buttonLogIn = (Button) findViewById(R.id.log_in);
        buttonSignUp = (Button) findViewById(R.id.sign_up);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        buttonSignUp.setOnClickListener(this);
        buttonLogIn.setOnClickListener(this);
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing to do
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nothing to do
            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    editTextEmail.setText(result);
                    editTextEmail.setSelection(result.length());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.log_in:
                progressBar.setVisibility(View.VISIBLE);
                Request.getLogIn(logInCallback, editTextEmail.getText().toString(), editTextPassword.getText().toString());
                break;
            case R.id.sign_up:
                goToSignUp();
                break;
            default:
                Log.e("onClick", getString(R.string.unknown_id));
                break;
        }
    }

    private void logIn() {
        Intent intent = IntentFactory.getMainIntent(AppController.getInstance());
        startActivity(intent);
        finish();
        Request.setMessagingToken(Preferences.messagingCallback, FirebaseInstanceId.getInstance().getToken());
    }

    private void goToSignUp() {
        Intent intent = IntentFactory.getSignUpIntent(this);
        startActivity(intent);
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 9000)
                        .show();
            } else {
                Log.i("LoginActivity", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
