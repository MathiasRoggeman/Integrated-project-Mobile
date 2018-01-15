package be.ap.edu.owa_app.Calendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import be.ap.edu.owa_app.Contacts.Contacts;
import be.ap.edu.owa_app.Contacts.ContactsActivity;
import be.ap.edu.owa_app.Contacts.ContactsAdapter;
import be.ap.edu.owa_app.R;

public class AttendeesActivity extends AppCompatActivity {
    private String url = "https://graph.microsoft.com/v1.0/me/contacts";

    private ArrayList<Contacts> contacts = new ArrayList<>();
    private ListView listView;
    private ContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendees);

        String token = this.getIntent().getExtras().getString("token");
        getContacts(token, url);

        Collections.sort(contacts, new Comparator<Contacts>(){
            public int compare(Contacts p1, Contacts p2) {
                return p1.getDisplayName().compareTo(p2.getDisplayName());
            }
        });

        listView = findViewById(R.id.listAanwezigen);
        contactsAdapter = new ContactsAdapter(this, R.layout.activity_listviewcontacts, contacts);
        listView.setAdapter(contactsAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater menuInflater = actionMode.getMenuInflater();
                menuInflater.inflate(R.menu.menu_attendees, menu);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_attendees, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            case R.id.action_add_attendees:
                finish();
                return true;






        }
        return super.onOptionsItemSelected(item);
    }

    private void getContacts(final String token, String url) {
        Log.d("Start", "Start callGraphAPI");

        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d("QUEUE", "After queue");
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
            Log.d("ParametersO", parameters.toString());
        } catch (Exception e) {
            Log.d("ParametersE", "Failed to put parameters: " + e.toString());
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
            /* Successfully called graph, process data and send to UI */
                        Log.d("Contacts", "Response: " + response.toString());

                        try {
                            JSONArray subject = response.getJSONArray("value");
                            for (int i = 0; i < subject.length(); i++) {
                                String id = (subject.getJSONObject(i).getString("id"));
                                String name = (subject.getJSONObject(i).get("displayName")).toString();
                                String givenname = (subject.getJSONObject(i).get("givenName")).toString();
                                Log.d("givenname", givenname);
                                String surname = (subject.getJSONObject(i).get("surname")).toString();
                                String mobilePhone = (subject.getJSONObject(i).getString("mobilePhone"));
                                JSONArray mailArray = (subject.getJSONObject(i).getJSONArray("emailAddresses"));
                                String mail;
                                if(mailArray != null && mailArray.length() > 0) {
                                    mail = mailArray.getJSONObject(0).getString("address");
                                }else{
                                    mail = "null";
                                }
                                contacts.add(new Contacts(id, name, givenname, surname, mail, mobilePhone));
                            }
                            listView.requestLayout();
                            contactsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "Error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("header", "add headers");
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                Log.d("header", token);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Log.d("request", "add request");
        queue.add(request);
    }
}
