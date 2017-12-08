package be.ap.edu.owa_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;

public class OpenMailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_mail);

        String sender = this.getIntent().getExtras().getString("sender");
        String onderwerp = this.getIntent().getExtras().getString("onderwerp");
        String date = this.getIntent().getExtras().getString("date");
        String message = this.getIntent().getExtras().getString("message");

        TextView zender = (TextView)findViewById(R.id.sender_open);
        TextView subject = (TextView)findViewById(R.id.onderwerp_open);
        TextView datum = (TextView)findViewById(R.id.date_open);
        EditText bericht = (EditText) findViewById(R.id.message_open);

        String textFromHtml = Jsoup.parse(message).text();

        zender.setText(sender);
        subject.setText(onderwerp);
        datum.setText(date);
        bericht.setText(textFromHtml);

        Button button = findViewById(R.id.back_open);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OpenMailActivity.this, MainActivity.class));
            }
        });
    }
}
