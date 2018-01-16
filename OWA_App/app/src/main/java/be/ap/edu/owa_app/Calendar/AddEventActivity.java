package be.ap.edu.owa_app.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import be.ap.edu.owa_app.Contacts.Contacts;
import be.ap.edu.owa_app.Contacts.ContactsActivity;
import be.ap.edu.owa_app.R;

public class AddEventActivity extends AppCompatActivity {

    private String token;
    private String SEND_URL = "https://graph.microsoft.com/v1.0/me/events";

    EditText subject;
    EditText beschrijving;
    TextView startdate;
    TextView starttime;
    TextView enddate;
    TextView endtime;
    EditText location;
    EditText naam;
    EditText mail;
    TextView aanwezig;

    private static final String TAG = "DetailEvents";
    private DatePickerDialog.OnDateSetListener dateSetListenerStart;
    private DatePickerDialog.OnDateSetListener dateSetListenerEnd;

    private ArrayList<Contacts> contactlist = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Voeg Event Toe");
        token = this.getIntent().getExtras().getString("token");




        //Log.d("makeEvent", makeEvent());

        subject = findViewById(R.id.addevent_subject);
        beschrijving = findViewById(R.id.addevent_beschrijving);
        startdate = findViewById(R.id.addevent_startdate);
        starttime = findViewById(R.id.addevent_starttime);
        enddate = findViewById(R.id.addevent_enddate);
        endtime = findViewById(R.id.addevent_endtime);
        location = findViewById(R.id.addevent_locatie);
        aanwezig = findViewById(R.id.aanwezig);



        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AddEventActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListenerStart, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AddEventActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListenerEnd, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyyy: " + day + "/" + month + "/" + year);

                String date = year + "/" + month + "/" + day;
                startdate.setText(date);
            }
        };
        dateSetListenerEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyyy: " + day + "/" + month + "/" + year);

                String date = year + "/" + month + "/" + day;
                enddate.setText(date);
            }
        };



        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                new TimePickerDialog(AddEventActivity.this, st, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
            }
        });

        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                new TimePickerDialog(AddEventActivity.this, et, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
            }
        });

        aanwezig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddEventActivity.this, AttendeesActivity.class);
                intent.putExtra("token", token);
                startActivityForResult(intent, 1);
            }
        });

    }
    TimePickerDialog.OnTimeSetListener st = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            starttime.setText(String.format("%02d:%02d", hour, minute));
        }
    };
    TimePickerDialog.OnTimeSetListener et = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            endtime.setText(String.format("%02d:%02d", hour, minute));
        }
    };
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


        RequestQueue queue = Volley.newRequestQueue(this);

        Log.d("Contacts", makeEvent());

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

        JsonArrayBuilder jsonUserArray = Json.createArrayBuilder();

        for(Contacts x : contactlist){
            Log.d("Contacts" , x.getDisplayName());
            JsonObjectBuilder job = Json.createObjectBuilder().add("type", "required").add("emailAddress", Json.createObjectBuilder().add("name", x.getDisplayName()).add("address", x.getEmail()));
            jsonUserArray.add(job);
        }


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
                .add("attendees", jsonUserArray);

        return event.build().toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && data != null){
            if(resultCode == RESULT_OK) {
                contactlist = (ArrayList<Contacts>) data.getSerializableExtra("contact_items");
                Log.d("ArrayList", contactlist.get(0).getDisplayName());
                String attendees = "";
                for (Contacts contact : contactlist) {
                    attendees += contact.getDisplayName() + "; ";
                }
                aanwezig.setText(attendees);
            }
        }
    }
}
