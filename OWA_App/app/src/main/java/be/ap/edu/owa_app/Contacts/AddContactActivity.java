package be.ap.edu.owa_app.Contacts;

import android.app.DatePickerDialog;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import be.ap.edu.owa_app.R;

public class AddContactActivity extends AppCompatActivity {

    private String token;
    private String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/contacts";
    private static final String TAG = AddContactActivity.class.getSimpleName();

    private EditText naam;
    private EditText voornaam;
    private EditText nummer;
    private EditText email;
    private EditText Straat;
    private EditText Provincie;
    private EditText Stad;
    private EditText Plaats;
    private EditText Status;
    private EditText Postcode;
    private EditText Land;
    private EditText Bedrijf;
    private EditText BedrijfsTitel;
    private EditText Opmerkingen;
    private TextView geboorteDatum;
    private DatePickerDialog.OnDateSetListener birthdayDatepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contacts);

        naam = findViewById(R.id.editname);
        voornaam = findViewById(R.id.voornaam);
        nummer = findViewById(R.id.editnumber);
        email = findViewById(R.id.editmail);
        Straat = findViewById(R.id.Straat);
        Stad = findViewById(R.id.Stad);
        Postcode = findViewById(R.id.Postcode);
        Provincie = findViewById(R.id.staat);
        Land = findViewById(R.id.Land);
        Bedrijf = findViewById(R.id.Bedrijf);
        BedrijfsTitel = findViewById(R.id.Titel);
        Opmerkingen = findViewById(R.id.Opmerkingen);
        geboorteDatum = findViewById(R.id.geboortedatum);

        getSupportActionBar().setTitle("Nieuw contact");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        token = this.getIntent().getExtras().getString("token");

        geboorteDatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AddContactActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, birthdayDatepicker, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        birthdayDatepicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyyy: " + day + "/" + month + "/" + year);
                TimeZone tz = TimeZone.getTimeZone("UTC");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                df.setTimeZone(tz);
                String date = year + "/" + month + "/" + day;


                try {
                    geboorteDatum.setText(toISO8601UTC(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    Log.d(TAG, "onDateSet: " + toISO8601UTC(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static String toISO8601UTC(String date) throws ParseException {
        String normalFormat = "yyyy/mm/dd";

        SimpleDateFormat formatter = new SimpleDateFormat(normalFormat);
        Date datum = formatter.parse(date);
        String to_format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat ISOformat = new SimpleDateFormat(to_format);

        return ISOformat.format(datum);
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
                    editContact(MSGRAPH_URL);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent2 = new Intent(AddContactActivity.this, ContactsActivity.class);
                intent2.putExtra("token", token);
                startActivity(intent2);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void editContact(String url) throws JSONException {
        Log.d("token", url);

    /* Make sure we have a token to send to graph */
        if (token == null) {
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        final JSONObject contactobject = new JSONObject(postContact());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,
                contactobject, new Response.Listener<JSONObject>() {
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

    private String postContact() {
        JsonObjectBuilder mail = Json.createObjectBuilder()
                .add("displayName", voornaam.getText().toString() + " " + naam.getText().toString())
                .add("givenName", voornaam.getText().toString())
                .add("surname", naam.getText().toString())
                .add("companyName", Bedrijf.getText().toString())
                .add("personalNotes", Opmerkingen.getText().toString())
                .add("jobTitle", BedrijfsTitel.getText().toString())
                .add("emailAddresses", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("address", email.getText().toString())
                        ))
                .add("mobilePhone", nummer.getText().toString())
                .add("homeAddress", Json.createObjectBuilder()
                        .add("street", Straat.getText().toString())
                        .add("city", Stad.getText().toString())
                        .add("state",Provincie.getText().toString() )
                        .add("countryOrRegion", Land.getText().toString())
                        .add("postalCode",Postcode.getText().toString())


                );
        return mail.build().toString();
    }
}
