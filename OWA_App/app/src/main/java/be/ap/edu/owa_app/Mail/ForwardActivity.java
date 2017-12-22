package be.ap.edu.owa_app.Mail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import be.ap.edu.owa_app.R;

public class ForwardActivity extends AppCompatActivity {


    private String id;
    private String token;

    private String MSGRAPH_URL;
    private TextView name;
    private TextView mailadres;
    private TextView comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward);

        MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/mailfolders/inbox/messages/";
        id = this.getIntent().getExtras().getString("messageid");
        token = this.getIntent().getExtras().getString("token");
        Log.d("message", id);
        Button send = findViewById(R.id.send_forward);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendMail(token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Log.d("makeMail", makeMail());
            }
        });


    }

    private void sendMail(final String token) throws JSONException {

        name = (TextView)findViewById(R.id.name);
        mailadres = (TextView)findViewById(R.id.email);
        comment = (TextView)findViewById(R.id.comment);

        Log.d("message", name.getText().toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        System.out.println(makeMail());

        final JSONObject mailObject = new JSONObject(makeMail());

        System.out.println(mailObject.toString());

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, MSGRAPH_URL + id + "/forward", mailObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Verzonden!", Toast.LENGTH_SHORT).show();
                        Log.d("response", response.toString());
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
                headers.put("Content-type", "application/json; charset=utf-8");
                return headers;
            }

        };

        queue.add(objectRequest);
    }

    private String makeMail() {
        JsonObjectBuilder mail = Json.createObjectBuilder()
                .add("comment", comment.getText().toString())
                .add("toRecipients", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("emailAddress", Json.createObjectBuilder()
                                        .add("address", mailadres.getText().toString())
                                        .add("name", name.getText().toString()))));
        return mail.build().toString();
    }
}
