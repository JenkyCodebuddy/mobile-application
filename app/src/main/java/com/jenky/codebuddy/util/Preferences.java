package com.jenky.codebuddy.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.jenky.codebuddy.api.Callback;
import com.jenky.codebuddy.api.Request;
import com.jenky.codebuddy.services.InstanceIDService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Preferences {
    public static final String SESSION_TOKEN = "session_token";
    public static final String USER_NAME = "user_name";

    private SharedPreferences sharedPreferences;

    public static final Callback messagingCallback = new Callback() {
        @Override
        public void onSuccess(JSONObject result) throws JSONException {
            Toast.makeText(AppController.getInstance(), result.getString("responseMessage"), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed(JSONObject result) throws JSONException {
        }
    };
    public static void logOut(final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        Request.removeMessagingToken(new Callback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                Toast.makeText(AppController.getInstance(), result.getString("responseMessage"), Toast.LENGTH_SHORT).show();
                AppController.getInstance().getPreferences().reset();
                Log.e("logOut:", "now");
                Log.e("AuthToken logOut:", AppController.getInstance().getPreferences().getSessionToken());
                Intent intent = IntentFactory.getLogInIntent(AppController.getInstance());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                AppController.getInstance().startActivity(intent);
            }

            @Override
            public void onFailed(JSONObject result) throws JSONException {
                Toast.makeText(AppController.getInstance(), "Failed to Log out", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public Preferences(Context appContext) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }


    public void setSessionToken(String token) {
        sharedPreferences.edit().putString(SESSION_TOKEN, token).apply();
    }

    public String getSessionToken() {
        return sharedPreferences.getString(SESSION_TOKEN, "");
    }


    public void setUserName(String mUserName) {
        sharedPreferences.edit().putString(USER_NAME, mUserName).apply();
    }


    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "");
    }


    public void reset() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public Boolean hasCredentials() {
        return AppController.getInstance().getPreferences().getSessionToken() == null || AppController.getInstance().getPreferences().getSessionToken().equals("");
    }
}


