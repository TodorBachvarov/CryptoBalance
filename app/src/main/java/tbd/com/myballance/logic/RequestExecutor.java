package tbd.com.myballance.logic;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tbd.com.myballance.AppController;
import tbd.com.myballance.SettingsManager;

/**
 * Created by todorbachvarov on 17.12.17.
 */

public class RequestExecutor {
    Context mContext;
    static RequestExecutor mInstance;

    public static void initInstance(Context context) {
        mInstance = new RequestExecutor(context);
    }

    public static RequestExecutor getInstance() {
        return mInstance;
    }

    RequestExecutor(Context context) {
        mContext = context;
    }

    public void loadCurrency(String currency, final RequestListener listener) {
        String url = "https://api.coinmarketcap.com/v1/ticker/"+ SettingsManager.getInstance().getCryptoIdByName(currency);

        JsonArrayRequest JsonRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("Request", response.toString());
                listener.onSuccess(response);
                try {
                    JSONObject test = new JSONObject("{}");
//                    JSONObject result = response != null ? response : null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Request", "Error: " + error.getMessage());
                Toast.makeText(mContext,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(JsonRequest);

    }

    public void loadMarket(int limit,final RequestListener listener){
        String url = "https://api.coinmarketcap.com/v1/ticker/?limit="+limit;
        JsonArrayRequest JsonRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("Request", response.toString());
                listener.onSuccess(response);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Request", "Error: " + error.getMessage());
                Toast.makeText(mContext,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(JsonRequest);
    }

    public abstract static class RequestListener{
        public abstract void onSuccess(Object result);
    }

}
