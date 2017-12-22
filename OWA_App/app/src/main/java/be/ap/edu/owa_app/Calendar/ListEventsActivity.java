package be.ap.edu.owa_app.Calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import be.ap.edu.owa_app.R;

public class ListEventsActivity extends AppCompatActivity {

    private String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/calendar/events";
    private String token;
    private int year;
    private int month;
    private int dayOfMonth;


    private ArrayList<Event> events = new ArrayList<>();
    private ListView listView;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Eventlist");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        token = this.getIntent().getExtras().getString("token");
        Log.d("accesstoken", token);



        callGraphAPI(token, MSGRAPH_URL);


        //Log.d("eventslist", events.get(0).toString());

        listView = findViewById(R.id.event_list);
        eventAdapter = new EventAdapter(this, R.layout.activity_listviewevents, events);
        listView.setAdapter(eventAdapter);

        final Context context = this;
        if (listView != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Event e = events.get(position);

                    try {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        Date startdate = null;//You will get date object relative to server/client timezone wherever it is parsed
                        startdate = dateFormat.parse(e.getStartDate());
                        Date enddate = dateFormat.parse(e.getEndDate());
                        //DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm"); //If you need time just put specific format for time like 'HH:mm:ss'
                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm");
                        String dateStr = formatter.format(startdate);
                        String endD = formatter.format(enddate);
                        Log.d("startdate", dateStr);
                        Toast.makeText(getApplicationContext(), "Date: " + dateStr, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ListEventsActivity.this, DetailEventsView.class);
                        intent.putExtra("token", token);
                        intent.putExtra("eventid", e.getId());
                        intent.putExtra("subject", e.getSubject());
                        intent.putExtra("beschrijving", e.getBody());
                        intent.putExtra("startdate", dateStr);
                        intent.putExtra("enddate", endD);
                        intent.putExtra("location", e.getLocation());
                        intent.putExtra("aanwezigen", e.getAttendees());
                        startActivity(intent);

                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }

                }

            });
        }



    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_activities, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent intent = new Intent(ListEventsActivity.this, CalendarActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
                return true;

            case R.id.action_add_event:
                Intent intent2 = new Intent(ListEventsActivity.this, AddEventActivity.class);
                intent2.putExtra("token", token);
                startActivity(intent2);
                return true;




        }
        return super.onOptionsItemSelected(item);
    }

    private void callGraphAPI(final String token, String url) {
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
                Log.d("ListEvents", "Response: " + response.toString());

                try {
                    JSONArray subject = response.getJSONArray("value");
                    for (int i = 0; i < subject.length(); i++) {
                        String id = (subject.getJSONObject(i).getString("id"));
                        String subj = (subject.getJSONObject(i).get("subject")).toString();
                        String body = (subject.getJSONObject(i).getJSONObject("body").getString("content"));
                        String startdate = (subject.getJSONObject(i).getJSONObject("start").getString("dateTime"));
                        String enddate = (subject.getJSONObject(i).getJSONObject("end").getString("dateTime"));
                        String location = (subject.getJSONObject(i).getJSONObject("location").getString("displayName"));
                        JSONArray x = (subject.getJSONObject(i).getJSONArray("attendees"));
                        StringBuilder aanwezigen = new StringBuilder();
                        for(int j = 0; j < x.length(); j++){
                            aanwezigen.append((x.getJSONObject(j).getJSONObject("emailAddress").getString("name")) + ", ");
                        }
                        events.add(new Event(id, subj, body, startdate, enddate, location, aanwezigen.toString()));
                        Log.d("Aanwezigen", aanwezigen.toString());
                    }
                    listView.requestLayout();
                    eventAdapter.notifyDataSetChanged();
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
