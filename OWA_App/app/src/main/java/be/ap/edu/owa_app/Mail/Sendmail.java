package be.ap.edu.owa_app.Mail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

import be.ap.edu.owa_app.R;


public class Sendmail extends AppCompatActivity {
    EditText mailContent;
    EditText mailSubject;
    EditText mailReciever;
    EditText mailCarbonCopy;
    Button sendMailButton;
    Button addAttachementButton;


    final static String SEND_URL = "https://graph.microsoft.com/v1.0/me/sendMail/";
    final static String DRAFT_URL = "https://graph.microsoft.com/v1.0/me/messages";
    Boolean attachementAdded = false;
    private AuthenticationResult authResult;
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmail);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sendmail, menu);

        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;

            case R.id.action_send_mail:
                final String token = this.getIntent().getExtras().getString("token");
                try {
                    sendMail(token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            //TODO change icon


        }
        return super.onOptionsItemSelected(item);
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

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, SEND_URL, mailObject,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(getApplicationContext(), "Verzonden!", Toast.LENGTH_SHORT).show();
                    System.out.println(response.toString());
                }

            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Toast.makeText(getApplicationContext(), "failed " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
       finish();
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

    private String makeDraftMail() {
      String mailbody =   mailContent.getText().toString();
        JsonObjectBuilder draftMail = Json.createObjectBuilder()
            .add("recievedDataTime", 0)
            .add("sentDateTime", 0)
            .add("hasAttachements", attachementAdded)
            .add("subject", mailSubject.getText().toString())
            .add("Body", Json.createObjectBuilder()
                .add("ContentType", "Text")
                .add("Content", mailContent.getText().toString()))
            .add("bodyPreview-value",mailbody.substring(0, Math.min(mailbody.length(), 255)));

        return draftMail.build().toString();
    }


}


