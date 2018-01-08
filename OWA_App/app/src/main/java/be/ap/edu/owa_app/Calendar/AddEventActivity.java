package be.ap.edu.owa_app.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import be.ap.edu.owa_app.R;

public class AddEventActivity extends AppCompatActivity {

    private String token;
    private String SEND_URL = "https://graph.microsoft.com/v1.0/me/events";

    EditText subject;
    EditText beschrijving;
    EditText startdate;
    EditText starttime;
    EditText enddate;
    EditText endtime;
    EditText location;
    EditText naam;
    EditText mail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        token = this.getIntent().getExtras().getString("token");




        //Log.d("makeEvent", makeEvent());





    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            case R.id.action_save:

                try {
                    createEvent(token);
                    Intent intent2 = new Intent(AddEventActivity.this, CalendarActivity.class);
                    intent2.putExtra("token", token);
                    startActivity(intent2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;






        }
        return super.onOptionsItemSelected(item);
    }


    private void createEvent(final String token) throws JSONException {

        /*mailContent = findViewById(R.id.txtMailContent);
        mailSubject = findViewById(R.id.txtSubject);
        mailReciever = findViewById(R.id.txtTo);
        mailCarbonCopy = findViewById(R.id.txtCc);
        System.out.println(mailContent.getText());*/

        subject = findViewById(R.id.addevent_subject);
        beschrijving = findViewById(R.id.addevent_beschrijving);
        startdate = findViewById(R.id.addevent_startdate);
        starttime = findViewById(R.id.addevent_starttime);
        enddate = findViewById(R.id.addevent_enddate);
        endtime = findViewById(R.id.addevent_endtime);
        location = findViewById(R.id.addevent_locatie);
        naam = findViewById(R.id.addevent_naam);
        mail = findViewById(R.id.addevent_mail);


        RequestQueue queue = Volley.newRequestQueue(this);

        final JSONObject eventObject = new JSONObject(makeEvent());

        System.out.println(eventObject.toString());

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, SEND_URL, eventObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Verzonden!", Toast.LENGTH_SHORT).show();
                        System.out.println(response.toString());
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "failed " + error.getMessage(), Toast.LENGTH_SHORT).show();
                VolleyLog.e("Error: ", error.getMessage());
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);

                return headers;
            }

        };

        queue.add(objectRequest);
    }


    private String makeEvent() {
        JsonObjectBuilder event = Json.createObjectBuilder()
                .add("subject", subject.getText().toString())
                .add("body", Json.createObjectBuilder()
                        .add("contentType", "HTML")
                        .add("content", beschrijving.getText().toString()))
                .add("start", Json.createObjectBuilder()
                        .add("dateTime", (startdate.getText().toString() + 'T' + starttime.getText().toString()))
                        .add("timeZone", "UTC"))
                .add("end", Json.createObjectBuilder()
                        .add("dateTime", (enddate.getText().toString() + 'T' + endtime.getText().toString()))
                        .add("timeZone", "UTC"))
                .add("location", Json.createObjectBuilder()
                        .add("displayName", location.getText().toString()))
                .add("attendees", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("emailAddress", Json.createObjectBuilder()
                                        .add("address", mail.getText().toString())
                                        .add("name", naam.getText().toString()))
                                .add("type", "required")));
        return event.build().toString();
    }
}
