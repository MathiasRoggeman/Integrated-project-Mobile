package be.ap.edu.owa_app.Contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

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

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import be.ap.edu.owa_app.R;

public class EditContactActivity extends AppCompatActivity {

    private String token;
    private String contactid;
    private String displayName;
    private String name;
    private String surname;
    private String mail;
    private String mobilePhone;
    private static final String TAG = EditContactActivity.class.getSimpleName();
    private String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/contacts/";



    private EditText naam;
    private EditText voornaam;
    private EditText nummer;
    private EditText email;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        getSupportActionBar().setTitle("Edit contact");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        token = this.getIntent().getExtras().getString("token");
        contactid = this.getIntent().getExtras().getString("contactid");
        displayName = this.getIntent().getExtras().getString("naam");
        name = this.getIntent().getExtras().getString("name");
        surname = this.getIntent().getExtras().getString("surname");
        mail = this.getIntent().getExtras().getString("email");
        mobilePhone = this.getIntent().getExtras().getString("mobile");
        MSGRAPH_URL += contactid;

        naam = findViewById(R.id.editname);
        voornaam = findViewById(R.id.editfirstname);
        nummer = findViewById(R.id.editnumber);
        email = findViewById(R.id.editmail);

        Log.d("contactid", contactid);
        Log.d("displayname", displayName);
        //Log.d("name", name);
        //Log.d("surname", surname);
        Log.d("mail", mail);
        Log.d("mobilephone", mobilePhone);

        if(mobilePhone.equals("null"))
            mobilePhone = "";
        if(mail.equals("null"))
            mail = "";

        naam.setText(surname);
        voornaam.setText(name);
        nummer.setText(mobilePhone);
        email.setText(mail);



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
                    Intent intent = new Intent(EditContactActivity.this, ContactsActivity.class);
                    intent.putExtra("token", token);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
        final JSONObject contactobject = new JSONObject(patchContact());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, url,
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

    private String patchContact() {
        JsonObjectBuilder mail = Json.createObjectBuilder()
                .add("displayName", voornaam.getText().toString() + " " + naam.getText().toString())
                .add("givenName", voornaam.getText().toString())
                .add("surname", naam.getText().toString())
                .add("emailAddresses", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                        .add("address", email.getText().toString())
                        ))
                .add("mobilePhone", nummer.getText().toString());
        return mail.build().toString();
    }
}
