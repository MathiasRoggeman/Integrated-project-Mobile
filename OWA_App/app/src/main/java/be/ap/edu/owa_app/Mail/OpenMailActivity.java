package be.ap.edu.owa_app.Mail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import be.ap.edu.owa_app.R;

public class OpenMailActivity extends AppCompatActivity {

    private String id;
    private Boolean isRead;
    private Boolean hasAttachments;
    private static final String TAG = OpenMailActivity.class.getSimpleName();
    private String MSGRAPH_URL;
    String token;
    final Context context = this;

    ImageView attachm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_mail);


        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        id = this.getIntent().getExtras().getString("id");
        MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/mailfolders/inbox/messages/";
        token = this.getIntent().getExtras().getString("token");
        isRead = this.getIntent().getExtras().getBoolean("isRead");
        hasAttachments = this.getIntent().getExtras().getBoolean("hasAttachments");

        String sender = this.getIntent().getExtras().getString("sender");
        String onderwerp = this.getIntent().getExtras().getString("onderwerp");
        String date = this.getIntent().getExtras().getString("date");
        String message = this.getIntent().getExtras().getString("message");

        final TextView zender = (TextView)findViewById(R.id.sender_open);
        final TextView subject = (TextView)findViewById(R.id.onderwerp_open);
        final TextView datum = (TextView)findViewById(R.id.date_open);
        final EditText bericht = (EditText) findViewById(R.id.message_open);
        final String textFromHtml = Jsoup.parse(message).text();

        try {
            patchIsRead(MSGRAPH_URL, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        while(message.contains("<!--")){
            String first = "<!--";
            String second = "-->";
            message = message.replace(message.substring(message.indexOf(first), message.indexOf(second) + 3), "");
           // message = message.replace("-->", "");

        }

        attachm = findViewById(R.id.attachment);
        attachm.setVisibility(View.INVISIBLE);

        /*if(message.contains("<style>")) {
            String str = message.replace(message.substring(message.lastIndexOf("<style>"), message.lastIndexOf("</style>")), "");
            Log.d("Substring", str);
        }*/

        zender.setText(sender);
        subject.setText(onderwerp);
        datum.setText(date);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            bericht.setText(Html.fromHtml(message,Html.FROM_HTML_MODE_LEGACY));
        } else {
            bericht.setText(Html.fromHtml(message));
        }

        bericht.setMovementMethod(LinkMovementMethod.getInstance());

        Log.d("Attachments", hasAttachments.toString());

        if(hasAttachments){

            attachm.setVisibility(View.VISIBLE);
        }


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_openmail, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            case R.id.action_delete:
                deleteMail(id);
                Intent intent2 = new Intent(OpenMailActivity.this, MainActivity.class);
                intent2.putExtra("token", token);
                startActivity(intent2);
                return true;
            //TODO change icon

            case R.id.action_forward:
                String message = this.getIntent().getExtras().getString("message");
                final String textFromHtml = Jsoup.parse(message).text();
                Intent intent3 = new Intent(context, ForwardActivity.class);

                intent3.putExtra("messageid", id);
                intent3.putExtra("body", textFromHtml);
                intent3.putExtra("token", token);
                startActivity(intent3);
                Log.d("messageid", id);
                return true;

            case R.id.action_reply:
                Intent in = new Intent(context, ReplyActivity.class);
                in.putExtra("messageid", id);
                in.putExtra("token", token);
                startActivity(in);
                return true;

            case R.id.action_reply_all:
                Intent i = new Intent(context, ReplyAllActivity.class);
                i.putExtra("messageid", id);
                i.putExtra("token", token);
                startActivity(i);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }


    private void patchIsRead(String url, String id) throws JSONException {
        Log.d("token", MSGRAPH_URL);

    /* Make sure we have a token to send to graph */
        if (token == null) {
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        final JSONObject mailObject = new JSONObject(makeMail());
        Log.d("isRead", mailObject.toString());

        try {

        } catch (Exception e) {
            Log.d(TAG, "Failed to put parameters: " + e.toString());
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, MSGRAPH_URL + id,
                mailObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: " + response.toString());

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
                headers.put("Content-Type", "application/json; charset=utf-8");
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

    private String makeMail() {
        JsonObjectBuilder mail = Json.createObjectBuilder().add("isRead", true);
        return mail.build().toString();
    }

    private void deleteMail(String id) {

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
        } catch (Exception e) {
            Log.d(TAG, "Failed to put parameters: " + e.toString());
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, MSGRAPH_URL + id,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: " + response.toString());

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
