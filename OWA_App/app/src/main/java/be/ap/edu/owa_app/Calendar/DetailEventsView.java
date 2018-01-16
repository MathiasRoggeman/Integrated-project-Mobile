package be.ap.edu.owa_app.Calendar;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import be.ap.edu.owa_app.R;

public class DetailEventsView extends AppCompatActivity {

    private static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/events/";
    String token;
    String eventid;
    String subject;
    String beschrijving;
    String startdate;
    String enddate;
    String location;
    String aanwezigen;

    TextView onderwerp;
    TextView description;
    TextView datetime;
    TextView locatie;
    TextView attendees;

    Button back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_events_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        token = this.getIntent().getExtras().getString("token");
        eventid = this.getIntent().getExtras().getString("eventid");
        subject = this.getIntent().getExtras().getString("subject");
        beschrijving = this.getIntent().getExtras().getString("beschrijving");
        startdate = this.getIntent().getExtras().getString("startdate");
        enddate = this.getIntent().getExtras().getString("enddate");
        location = this.getIntent().getExtras().getString("location");
        aanwezigen = this.getIntent().getExtras().getString("aanwezigen");

        onderwerp = findViewById(R.id.subject_eventdetail);
        description = findViewById(R.id.beschrijving_eventdetail);
        datetime = findViewById(R.id.datetime_eventdetail);
        locatie = findViewById(R.id.locatie_eventdetail);
        attendees = findViewById(R.id.aanwezigen_eventdetail);
        getSupportActionBar().setTitle(subject.toString());
        onderwerp.setText(subject);
        //description.setText(beschrijving);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            description.setText(Html.fromHtml(beschrijving,Html.FROM_HTML_MODE_LEGACY));
        } else {
            description.setText(Html.fromHtml(beschrijving));
        }
        datetime.setText(startdate + " - " + enddate);
        locatie.setText("Locatie: " + location);
        attendees.setText(aanwezigen);



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);

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
                MSGRAPH_URL += eventid;
                deleteEvent(MSGRAPH_URL);
                Intent intent2 = new Intent(DetailEventsView.this, CalendarActivity.class);
                intent2.putExtra("token", token);
                startActivity(intent2);
                return true;






        }
        return super.onOptionsItemSelected(item);
    }
    private void deleteEvent(String url) {

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            /* Successfully called graph, process data and send to UI */
                Log.d("Deleted", "Response: " + response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DeleteError", "Error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        Log.d("Deleting", "Deleting to Queue, Request: " + request.toString());

        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
