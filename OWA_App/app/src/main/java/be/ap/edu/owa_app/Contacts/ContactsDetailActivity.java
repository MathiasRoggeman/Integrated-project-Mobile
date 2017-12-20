package be.ap.edu.owa_app.Contacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class ContactsDetailActivity extends AppCompatActivity {

    private String token;
    private String contactid;
    private String displayName;
    private String name;
    private String surname;
    private String mail;
    private String mobile;
    private String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/contacts/";
    private static final String TAG = ContactsDetailActivity.class.getSimpleName();


    TextView naam;
    TextView email;
    TextView mobilePhone;
    ImageView icon;
    ImageView mailIcon;
    Button delete;

    private Button bewerken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detail);

        token = this.getIntent().getExtras().getString("token");
        contactid = this.getIntent().getExtras().getString("contactid");
        displayName = this.getIntent().getExtras().getString("name");
        name = this.getIntent().getExtras().getString("givenname");
        surname = this.getIntent().getExtras().getString("surname");
        mail = this.getIntent().getExtras().getString("mail");
        mobile = this.getIntent().getExtras().getString("mobile");


        naam = findViewById(R.id.contactnaam);
        email = findViewById(R.id.contactsemail);
        mobilePhone = findViewById(R.id.contactmobile);
        icon = findViewById(R.id.phone);
        mailIcon = findViewById(R.id.imageView);

        if(mail.equals("null")){
            email.setVisibility(View.INVISIBLE);
            mailIcon.setVisibility(View.INVISIBLE);
        }
        if(mobile.equals("null")){
            mobilePhone.setVisibility(View.INVISIBLE);
            icon.setVisibility(View.INVISIBLE);
        }

        naam.setText(displayName);
        email.setText(mail);
        mobilePhone.setText(mobile);

        bewerken = findViewById(R.id.contactbewerken);
        bewerken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsDetailActivity.this, EditContactActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("contactid", contactid);
                intent.putExtra("naam", displayName);
                intent.putExtra("name", name);
                intent.putExtra("surname", surname);
                intent.putExtra("email", mail);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });

        delete = findViewById(R.id.contactdelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteContact(contactid);
                Intent intent = new Intent(ContactsDetailActivity.this, ContactsActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

    }

    private void deleteContact(String id) {

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
        } catch (Exception e) {
            Log.d(TAG, "Failed to put parameters: " + e.toString());
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, MSGRAPH_URL + id,
                null, new Response.Listener<JSONObject>() {
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
}
