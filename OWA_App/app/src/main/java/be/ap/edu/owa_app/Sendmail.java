package be.ap.edu.owa_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class Sendmail extends AppCompatActivity {
    EditText mailContent;
    EditText mailSubject;
    EditText mailReciever;
    EditText mailCarbonCopy;
    Button sendMailButton;
    Button addAttachementButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmail);
        SharedPreferences sharedPref = getSharedPreferences("SessionInfo" ,Context.MODE_PRIVATE);
        String token = sharedPref.getString("Saved_Token","");

        mailContent = findViewById(R.id.txtMailContent);
        mailSubject = findViewById(R.id.txtSubject);
        mailReciever = findViewById(R.id.txtTo);
        mailCarbonCopy = findViewById(R.id.txtCc);
        sendMailButton = findViewById(R.id.btnSendMail);
        addAttachementButton = findViewById(R.id.btnAttachement);



        mailContent.setText("Hier token " + token.toString());



    }

}
