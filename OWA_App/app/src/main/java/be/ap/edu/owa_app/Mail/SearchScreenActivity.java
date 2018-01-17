package be.ap.edu.owa_app.Mail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import be.ap.edu.owa_app.R;

public class SearchScreenActivity extends AppCompatActivity {

    String token;
    Button button;
    EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);

        getSupportActionBar().setTitle("Search Mail");

        token = this.getIntent().getExtras().getString("token");
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = findViewById(R.id.editText);
                Log.d("Button", text.getText().toString());
                Intent intent = new Intent(SearchScreenActivity.this, SearchResultActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("searchtext", text.getText().toString());
                startActivity(intent);
            }
        });
    }
}
