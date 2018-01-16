package be.ap.edu.owa_app.Mail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import be.ap.edu.owa_app.R;

public class ReplyAllActivity extends AppCompatActivity {

    TextView comment;
    String id;
    String token;
    String MSGRAPH_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        getSupportActionBar().setTitle("Reply All");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reply, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;


            case R.id.action_reply_mail:
                id = this.getIntent().getExtras().getString("messageid");
                token = this.getIntent().getExtras().getString("token");

                MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/mailfolders/inbox/messages/";

                try {
                    sendMail(id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }



    private void sendMail(String id) throws JSONException {

        comment = (TextView)findViewById(R.id.comment);

        Log.d("message", comment.getText().toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        System.out.println(makeMail());

        final JSONObject mailObject = new JSONObject(makeMail());

        System.out.println(mailObject.toString());

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, MSGRAPH_URL + id + "/replyAll", mailObject,
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
                .add("comment", comment.getText().toString());
        return mail.build().toString();
    }
}
