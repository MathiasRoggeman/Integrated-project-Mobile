package be.ap.edu.owa_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Map;

public class OpenMailActivity extends AppCompatActivity {

    private String id;
    private static final String TAG = OpenMailActivity.class.getSimpleName();
    private String MSGRAPH_URL;
    SharedPreferences sharedPref;
    String token;

    String onderwerp;
    String sender;
    String date;
    String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_mail);

        id = this.getIntent().getExtras().getString("id");
        MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/mailfolders/inbox/messages/" + id;
        sharedPref = getSharedPreferences("SessionInfo" , Context.MODE_PRIVATE);
        token = sharedPref.getString("Saved_Token","");

        callGraphAPI();

        /*String sender = this.getIntent().getExtras().getString("sender");
        String onderwerp = this.getIntent().getExtras().getString("onderwerp");
        String date = this.getIntent().getExtras().getString("date");
        String message = this.getIntent().getExtras().getString("message");*/

        TextView zender = (TextView)findViewById(R.id.sender_open);
        TextView subject = (TextView)findViewById(R.id.onderwerp_open);
        TextView datum = (TextView)findViewById(R.id.date_open);
        EditText bericht = (EditText) findViewById(R.id.message_open);

        //String textFromHtml = Jsoup.parse(message).text();

        /*zender.setText(sender);
        subject.setText(onderwerp);
        datum.setText(date);
        bericht.setText(textFromHtml);*/

        Button button = findViewById(R.id.back_open);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OpenMailActivity.this, MainActivity.class));
            }
        });
    }

    private void callGraphAPI() {
        Log.d("token", MSGRAPH_URL);

    /* Make sure we have a token to send to graph */
        if (token == null) {
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
        } catch (Exception e) {
            Log.d(TAG, "Failed to put parameters: " + e.toString());
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MSGRAPH_URL,
                parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: " + response.toString());

                /*JSON Object creation*/
                try {
                    onderwerp = response.getString("subject");
                    Log.d("onderwerp", onderwerp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        Log.d(TAG, "Adding HTTP GET to Queue, Request: " + request.toString());

        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
