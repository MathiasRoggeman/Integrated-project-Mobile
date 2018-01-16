package be.ap.edu.owa_app.Contacts;

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

import be.ap.edu.owa_app.Calendar.CalendarActivity;
import be.ap.edu.owa_app.Mail.MainActivity;
import be.ap.edu.owa_app.R;

public class ContactsActivity extends AppCompatActivity {
    private String token;
    private String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/contacts";

    private ArrayList<Contacts> contacts = new ArrayList<>();
    private ArrayList<Address> adressen = new ArrayList<>();
    private ListView listView;
    private ContactsAdapter contactsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        getSupportActionBar().setTitle("Contacts");
        token = this.getIntent().getExtras().getString("token");

        getContacts(token, MSGRAPH_URL);

        Collections.sort(contacts, new Comparator<Contacts>(){
            public int compare(Contacts p1, Contacts p2) {
                return p1.getDisplayName().compareTo(p2.getDisplayName());
            }
        });

        listView = findViewById(R.id.contacts_list);
        contactsAdapter = new ContactsAdapter(this, R.layout.activity_listviewcontacts, contacts);
        listView.setAdapter(contactsAdapter);

        if (listView != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Contacts e = contacts.get(position);


                    Intent intent = new Intent(ContactsActivity.this, ContactsDetailActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("contactid", e.getId());
                    intent.putExtra("name", e.getDisplayName());
                    intent.putExtra("mail", e.getEmail());
                    intent.putExtra("mobile", e.getMobile());
                    intent.putExtra("givenname", e.getName());
                    intent.putExtra("surname", e.getSurname());
                    intent.putExtra("companyName",e.getBedrijf());
                    intent.putExtra("personalNotes", e.getOpmerkingen());
                    intent.putExtra("birthday",e.getBirthday());
                    intent.putExtra("jobTitle", e.getBedrijfsTitel());
           /*
                    intent.putExtra("adress_Straat", e.getAddress().getStraat());
                    intent.putExtra("adress_Postbus", e.getAddress().getPostbus());
                    intent.putExtra("adress_Omgeving", e.getAddress().getOmgeving());
                    intent.putExtra("adress_Plaats", e.getAddress().getPlaats());
                    intent.putExtra("adress_Postcode", e.getAddress().getPostcode());
                    intent.putExtra("adress_Land", e.getAddress().getLand());*/
                    startActivity(intent);


                }

            });
        }





        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_contacts);
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
    }

    protected void selectFragment(MenuItem item) {

        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.action_home:
                // Action to perform when Home Menu item is selected.
                Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
                intent.putExtra("token",token);
                startActivity(intent);
                break;
            case R.id.action_calendar:
                // Action to perform when Bag Menu item is selected.
                Intent intent2 = new Intent(ContactsActivity.this, CalendarActivity.class);
                intent2.putExtra("token",token);
                startActivity(intent2);
                break;
        }

    }




    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contacts, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_make_contact:
                // Action to perform when Bag Menu item is selected.
                Intent intent = new Intent(ContactsActivity.this, AddContactActivity.class);
                intent.putExtra("token",token);
                startActivity(intent);
                break;


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
                                String bedrijf  = (subject.getJSONObject(i).get("companyName")).toString();
                                String opmerkingen = (subject.getJSONObject(i).get("personalNotes")).toString();
                                String birthday = (subject.getJSONObject(i).get("birthday")).toString();
                                String bedrijfstitel = (subject.getJSONObject(i).get("jobTitle")).toString();
                                JSONArray mailArray = (subject.getJSONObject(i).getJSONArray("emailAddresses"));
                                String mail;
                                if(mailArray != null && mailArray.length() > 0) {
                                    mail = mailArray.getJSONObject(0).getString("address");
                                }else{
                                    mail = "null";
                                }
                                contacts.add(new Contacts(id, name, givenname, surname, mail, mobilePhone, bedrijf, opmerkingen, birthday, bedrijfstitel));
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
