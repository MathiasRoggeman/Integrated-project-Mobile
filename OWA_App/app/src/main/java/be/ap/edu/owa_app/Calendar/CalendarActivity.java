package be.ap.edu.owa_app.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;

import be.ap.edu.owa_app.R;

public class CalendarActivity extends AppCompatActivity {

    CalendarView calendarView;
    Button today;
    Button add;
    Button listEvents;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(null);

        token = this.getIntent().getExtras().getString("token");
        today = findViewById(R.id.today);
        add = findViewById(R.id.addEvent);
        listEvents = findViewById(R.id.listEvents);
        calendarView = findViewById(R.id.calendarView3);
        calendarView.setShowWeekNumber(true);

        calendarView.setDate(Calendar.getInstance().getTimeInMillis(),false,true);

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendarView.setDate(Calendar.getInstance().getTimeInMillis(),false,true);
            }
        });
        listEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, ListEventsActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, AddEventActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date by using a toast
                Toast.makeText(getApplicationContext(), dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CalendarActivity.this, ListEventDateActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("year", year);
                intent.putExtra("month", (month+1));
                intent.putExtra("dayOfMonth", dayOfMonth);
                startActivity(intent);
            }
        });






    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_add_event:
                Intent intent = new Intent(CalendarActivity.this, AddEventActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
                return true;


            case R.id.action_list_events:
                Intent intent2 = new Intent(CalendarActivity.this,ListEventsActivity.class);
                intent2.putExtra("token", token);
                startActivity(intent2);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

}
