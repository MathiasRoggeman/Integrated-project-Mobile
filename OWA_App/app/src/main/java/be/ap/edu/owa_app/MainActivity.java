package be.ap.edu.owa_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.ap.edu.owa_app.Calendar.CalendarActivity;
import be.ap.edu.owa_app.Contacts.ContactsActivity;

public class MainActivity extends AppCompatActivity {

    final static String CLIENT_ID = "0f1fbbeb-1161-4034-9875-70c8099230d7";
    final static String SCOPES[] = {"https://graph.microsoft.com/Mail.Read",
            "https://graph.microsoft.com/Mail.ReadWrite",
            "https://graph.microsoft.com/Mail.Send",
            "https://graph.microsoft.com/Calendars.Read" };
    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/mailfolders/inbox/messages";

    private ArrayList<MailList> maillist = new ArrayList<>();
    private ListAdapter listadapter;

    /* UI & Debugging Variables */
    private static final String TAG = MainActivity.class.getSimpleName();


    ListView listView;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Postvak in");

        token = this.getIntent().getExtras().getString("token");
        Log.d("messagetoken", token);

        callGraphAPI(token);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        if (bottomNavigationView != null) {
            // Set action to perform when any menu-item is selected.
            bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectFragment(item);
                        return false;
                    }
                });
        }



        listView = findViewById(R.id.mobile_list);

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



    protected void selectFragment(MenuItem item) {

        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.action_home:
                // Action to perform when Home Menu item is selected.

                break;
            case R.id.action_calendar:
                // Action to perform when Bag Menu item is selected.
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                intent.putExtra("token",token);
                startActivity(intent);
                break;
            case R.id.action_contacts:
                // Action to perform when Bag Menu item is selected.
                Intent intent2 = new Intent(MainActivity.this, ContactsActivity.class);
                intent2.putExtra("token",token);
                startActivity(intent2);
                break;
        }
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maillist, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_create_mail:
                Intent intent = new Intent(MainActivity.this, Sendmail.class);
                intent.putExtra("token", token);
                startActivity(intent);
                return true;


            case R.id.action_logout:
                onSignOutClicked();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }
    private void callGraphAPI(final String token) {
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
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MSGRAPH_URL,
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

    private void onSignOutClicked() {
        PublicClientApplication sampleApp;
        sampleApp = null;
        if (sampleApp == null) {
            sampleApp = new PublicClientApplication(
                    this.getApplicationContext(),
                    CLIENT_ID);
        }

    /* Attempt to get a user and remove their cookies from cache*/
        List<User> users = null;

        try {
            users = sampleApp.getUsers();

            if (users == null) {
            /* We have no users*/

            } else if (users.size() == 1) {
            /* We have 1 user */
            /* Remove from token cache*/
                sampleApp.remove(users.get(0));

            }
            else {
            /* We have multiple users*/
                for (int i = 0; i < users.size(); i++) {
                    sampleApp.remove(users.get(i));
                }
            }

            Toast.makeText(getBaseContext(), "Signed Out!", Toast.LENGTH_SHORT)
                    .show();

        } catch (MsalClientException e) {
            Log.d(TAG, "MSAL Exception Generated while getting users: " + e.toString());

        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "User at this position does not exist: " + e.toString());
        }


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
