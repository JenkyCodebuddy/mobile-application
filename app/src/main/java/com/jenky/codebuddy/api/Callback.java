package com.jenky.codebuddy.api;

import org.json.JSONException;
import org.json.JSONObject;

public interface Callback {

    void onSuccess(JSONObject result) throws JSONException;
    void onFailed(JSONObject result) throws JSONException;


}
