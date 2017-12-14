package be.ap.edu.owa_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
import com.microsoft.identity.client.AuthenticationResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObjectBuilder;


public class Sendmail extends AppCompatActivity {
    EditText mailContent;
    EditText mailSubject;
    EditText mailReciever;
    EditText mailCarbonCopy;
    Button sendMailButton;
    Button addAttachementButton;

    final static String CLIENT_ID = "0f1fbbeb-1161-4034-9875-70c8099230d7";
    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/sendMail/";
    private AuthenticationResult authResult;
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmail);


        final String token = this.getIntent().getExtras().getString("accesstoken");


        sendMailButton = findViewById(R.id.btnSendMail);
        addAttachementButton = findViewById(R.id.btnAttachement);
        sendMailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    sendMail(token);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        });


        //  mailContent.setText("Hier token " + token.toString());


    }

    private void sendMail(final String token) throws JSONException {

        mailContent = findViewById(R.id.txtMailContent);
        mailSubject = findViewById(R.id.txtSubject);
        mailReciever = findViewById(R.id.txtTo);
        mailCarbonCopy = findViewById(R.id.txtCc);
        System.out.println(mailContent.getText());


        RequestQueue queue = Volley.newRequestQueue(this);

        final JSONObject mailObject = new JSONObject(makeMail());

        System.out.println(mailObject.toString());

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, MSGRAPH_URL, mailObject,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(getApplicationContext(), "Verzonden!", Toast.LENGTH_SHORT).show();
                    System.out.println(response.toString());
                }

            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "failed " + error.getMessage() , Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(Sendmail.this, MainActivity.class);
        startActivity(intent);
    }

    private String makeMail() {
        JsonObjectBuilder mail = Json.createObjectBuilder()
            .add("Message", Json.createObjectBuilder()
                .add("Subject", mailSubject.getText().toString())
                .add("Body", Json.createObjectBuilder()
                    .add("ContentType", "Text")
                    .add("Content", mailContent.getText().toString()))
                .add("ToRecipients", Json.createArrayBuilder()
                    .add(Json.createObjectBuilder()
                        .add("EmailAddress", Json.createObjectBuilder()
                            .add("Address", mailReciever.getText().toString()))))
            );
        return mail.build().toString();
    }

}


