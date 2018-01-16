package be.ap.edu.owa_app.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;

import be.ap.edu.owa_app.Contacts.ContactsActivity;
import be.ap.edu.owa_app.Mail.MainActivity;
import be.ap.edu.owa_app.R;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        getSupportActionBar().setTitle("Kalender");




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

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_date_range_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_mode_edit_black_24dp);






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


        }
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Tab1Calendar tab1 = new Tab1Calendar();
                    return tab1;
                case 1:
                    Tab2Events tab2 = new Tab2Events();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Kalender";
                case 1:
                    return "Events";
                default:
                    return null;
            }
        }
    }

}
