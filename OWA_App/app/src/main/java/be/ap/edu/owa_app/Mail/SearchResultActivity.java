package be.ap.edu.owa_app.Mail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.ap.edu.owa_app.R;

public class SearchResultActivity extends AppCompatActivity {

    String token;
    String searchtext;

    String url = "https://graph.microsoft.com/v1.0/me/messages?$search=\"";

    private ArrayList<MailList> maillist = new ArrayList<>();
    private ListAdapter listadapter;

    private ListView listView;
    /* UI & Debugging Variables */
    private static final String TAG = SearchResultActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        token = this.getIntent().getExtras().getString("token");
        searchtext = this.getIntent().getExtras().getString("searchtext");

        Log.d("texttt", searchtext);

        url += searchtext + "\"";

        TextView textView = findViewById(R.id.search_result);
        textView.setText(searchtext);

        callGraphAPI(url);

        listView = findViewById(R.id.listview_search);

        listadapter = new ListAdapter(this, R.layout.activity_listviewmails, maillist);
        listView.setAdapter(listadapter);

        final Context context = this;
        if (listView != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // 1
                    MailList mail = maillist.get(position);

                    // 2
                    Intent intent = new Intent(context, OpenMailActivity.class);

                    // 3
                    intent.putExtra("id", mail.getId());
                    intent.putExtra("sender", mail.getSender());
                    intent.putExtra("onderwerp", mail.getSubject());
                    intent.putExtra("date", mail.getDate());
                    intent.putExtra("message", mail.getMessage());
                    intent.putExtra("token", token);
                    intent.putExtra("isRead", mail.isRead());

                    // 4
                    startActivity(intent);
                }

            });
        }




    }

    private void callGraphAPI(String url) {
        Log.d(TAG, "Starting volley request to graph");

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
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: " + response.toString());

                updateGraphUI(response);
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

    /* Sets the Graph response */
    private void updateGraphUI(JSONObject graphResponse) {
        try {
            JSONArray subject = graphResponse.getJSONArray("value");
            for (int i = 0; i < subject.length(); i++) {
                String id = (subject.getJSONObject(i).getString("id"));
                String subj = (subject.getJSONObject(i).get("subject")).toString();
                String sender = (subject.getJSONObject(i).getJSONObject("sender").getJSONObject("emailAddress").get("name")).toString();
                String bodypr = (subject.getJSONObject(i).getString("bodyPreview"));
                Boolean read = (subject.getJSONObject(i).getBoolean("isRead"));
                String message = (subject.getJSONObject(i).getJSONObject("body").getString("content"));
                String date = (subject.getJSONObject(i).getString("receivedDateTime"));
                maillist.add(new MailList(id, subj, sender, bodypr, read, message, date));
            }
            listView.requestLayout();
            listadapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
