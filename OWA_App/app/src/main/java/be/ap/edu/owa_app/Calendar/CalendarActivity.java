package be.ap.edu.owa_app.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;

import be.ap.edu.owa_app.Contacts.ContactsActivity;
import be.ap.edu.owa_app.Mail.MainActivity;
import be.ap.edu.owa_app.R;

public class CalendarActivity extends AppCompatActivity {

    CalendarView calendarView;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);




        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_calendar);
        if (bottomNavigationView != null) {
            // Set action to perform when any menu-item is selected.
            bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectFragment(item);
                        return false;
                    }
                });
        }

        token = this.getIntent().getExtras().getString("token");

        calendarView = findViewById(R.id.calendarView3);
        calendarView.setShowWeekNumber(true);

        calendarView.setDate(Calendar.getInstance().getTimeInMillis(),false,true);


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
    protected void selectFragment(MenuItem item) {

        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.action_home:
                // Action to perform when Home Menu item is selected.
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                intent.putExtra("token",token);
                startActivity(intent);
                break;
            case R.id.action_contacts:
                // Action to perform when Bag Menu item is selected.
                Intent intent2 = new Intent(CalendarActivity.this, ContactsActivity.class);
                intent2.putExtra("token",token);
                startActivity(intent2);
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
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
