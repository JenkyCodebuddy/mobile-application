package com.jenky.codebuddy.api;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jenky.codebuddy.R;
import com.jenky.codebuddy.util.AppController;
import com.android.volley.Request.Method;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Request {
    public static final String API = "Https://Codebuddyjenky.herokuapp.com/";

    private Request() {
        //Prevent instantiation
    }

    /**
     * Adds a request to be executed to the Volley RequestQueue
     *
     * @param methodId     method the the request
     * @param url          URL which the request should be sent to
     * @param callback     The callback which contains how the data should be handled
     * @param extraHeaders Extra headers that should be sent with the request
     */


    public static void executeRequest(int methodId, String url, final Callback callback, final Map<String, String> extraHeaders, final String tag) {
        //Expect response in json format

        Log.i("Request", methodId + ": " + url);
        Log.i("Token:", AppController.getInstance().getPreferences().getSessionToken());
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(methodId, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("responseCode") == 200) {
                                Log.i("Response", tag +" successfully received");
                                callback.onSuccess(response);
                            } else {
                                callback.onFailed(response);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(AppController.getInstance(), R.string.default_error, Toast.LENGTH_LONG).show();
                            Log.e("JSONException", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("HttpError", "Error: " + error.getMessage());
                JSONObject errorMessage = new JSONObject();
                try {
                    errorMessage.put("responseMessage", AppController.getInstance().getResources().getString(R.string.default_error));
                    callback.onFailed(errorMessage);
                } catch (JSONException e) {
                    Log.e("JSONException", e.getMessage(), e);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (extraHeaders != null) {
                    headers.putAll(extraHeaders);
                }
                headers.put("token", AppController.getInstance().getPreferences().getSessionToken());
                return headers;
            }
        };
        jsonObjReq.setRetryPolicy(new
                DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );
        AppController.getInstance().cancelPendingRequests(tag);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag);
    }

    public static void getLogIn(Callback callback, String email, String password) {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);
        executeRequest(Method.POST,
                API + "login",
                callback,
                credentials,
                "log_in");
    }

    public static void getProfile(Callback callback) {
        executeRequest(Method.GET,
                API + "profile",
                callback,
                null,
                "get_profile"
        );
    }

    public static void getSignUp(Callback callback, String email) {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        executeRequest(Method.POST,
                API + "signup",
                callback,
                credentials,
                "get_sign_up"
        );
    }

    public static void setVerify(Callback callback, String code, String password) {
        Map<String, String> verification = new HashMap<>();
        verification.put("verificationcode", code);
        verification.put("password", password);
        executeRequest(Method.POST,
                API + "signup/verify",
                callback,
                verification,
                "set_verify"
        );
    }

    public static void getAchievements(Callback callback) {
        executeRequest(Method.GET,
                API + "achievements",
                callback,
                null,
                "get_achievements"
        );
    }

    public static void getProjects(Callback callback) {
        executeRequest(Method.GET,
                API + "projects",
                callback,
                null,
                "get_projects"
        );
    }


    public static void getTowers(Callback callback, int projectId) {
        executeRequest(Method.GET,
                API + "projects/" + projectId,
                callback,
                null,
                "get_towers"
        );
    }

    public static void getPurchase(Callback callback, int itemId) {
        executeRequest(Method.POST,
                API + "shop/buy/" + itemId,
                callback,
                null,
                "get_purchase"+itemId
        );
    }

    public static void getShop(Callback callback) {
        executeRequest(Method.GET,
                API + "shop",
                callback,
                null,
                "get_shop"
        );
    }

    public static void getEquipment(Callback callback) {
        executeRequest(Method.GET,
                API + "equipment",
                callback,
                null,
                "get_equipment"
        );
    }

    public static void setEquipment(Callback callback, String headId, String shirtId, String legsId, String blockId) {
        executeRequest(Method.POST,
                API + "equipment/equip?helmet=" + headId + "&" + "shirt=" + shirtId + "&" + "legs=" + legsId + "&" + "block=" + blockId,
                callback,
                null,
                "log_in"
        );
    }

    public static void setMessagingToken(Callback callback, String token) {
        Map<String, String> messagingToken = new HashMap<>();
        Log.e("Send:", "SendingToken");
        Log.e("MessageToken:", token);
        Log.e("AuthToken:", AppController.getInstance().getPreferences().getSessionToken());
        messagingToken.put("messagingtoken", token);
        executeRequest(Method.POST,
                API + "messaging/notifications",
                callback,
                messagingToken,
                "set_messaging_token"
        );

    }

    public static void removeMessagingToken(Callback callback) {
        Map<String, String> messagingToken = new HashMap<>();
        String token = AppController.getInstance().getPreferences().getSessionToken();
        Log.e("Remove:", "RemoveToken");
        Log.e("AuthToken:", AppController.getInstance().getPreferences().getSessionToken());
        executeRequest(Method.POST,
                API + "messaging/logout",
                callback,
                messagingToken,
                "set_messaging_token"
        );

    }
}
