package be.ap.edu.owa_app.Calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import be.ap.edu.owa_app.R;

/**
 * Created by isa_l on 12-01-18.
 */

public class Tab1Calendar extends Fragment {

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());

    private String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/calendarview";
    private String allEventsUrl = "https://graph.microsoft.com/v1.0/me/calendar/events";
    private CalendarActivity token;

    private ArrayList<Event> events;
    private ListView listView;
    private EventAdapter eventAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab1_calendar, container, false);


        /*final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);*/

        final TextView text = rootView.findViewById(R.id.section_label);
        text.setText(dateFormatMonth.format(new Date()));

        compactCalendar = (CompactCalendarView) rootView.findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);


        token = (CalendarActivity) getActivity();
        final String t = token.token;

        showEventsOnCalendar(t, allEventsUrl);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                events = new ArrayList<>();
                Log.d("DateClicked", dateClicked.toString());
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateClicked);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                Log.d("DateClicked", year + "-" + (month + 1) + "-" + day);
                String url = MSGRAPH_URL + "?startdatetime=" + year + "-" + (month + 1) + "-" + day + "T00:00:00.000&enddatetime=" + year + "-" + (month + 1) + "-" + day + "T23:59:59.000";
                Log.d("DateClicked", t);
                getAllEventsOfTheDay(t, url);
                Log.d("DateClicked", url);
                listView = rootView.findViewById(R.id.listview_events_calendarpage);
                eventAdapter = new EventAdapter(getContext(), R.layout.activity_listviewevents, events);
                listView.setAdapter(eventAdapter);
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                text.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }

        });


        return rootView;
    }


    private void getAllEventsOfTheDay(final String token, String url) {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
        } catch (Exception e) {
            Log.d("Parameters", "Failed to put parameters: " + e.toString());
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
            /* Successfully called graph, process data and send to UI */
                        Log.d("ListEvents", "Response: " + response.toString());

                        try {
                            JSONArray subject = response.getJSONArray("value");
                            for (int i = 0; i < subject.length(); i++) {
                                String id = (subject.getJSONObject(i).getString("id"));
                                String subj = (subject.getJSONObject(i).get("subject")).toString();
                                String body = (subject.getJSONObject(i).getJSONObject("body").getString("content"));
                                String startdate = (subject.getJSONObject(i).getJSONObject("start").getString("dateTime"));
                                String enddate = (subject.getJSONObject(i).getJSONObject("end").getString("dateTime"));
                                String location = (subject.getJSONObject(i).getJSONObject("location").getString("displayName"));
                                JSONArray x = (subject.getJSONObject(i).getJSONArray("attendees"));
                                StringBuilder aanwezigen = new StringBuilder();
                                for(int j = 0; j < x.length(); j++){
                                    aanwezigen.append(x.getJSONObject(j).getJSONObject("emailAddress").getString("name"));
                                }
                                events.add(new Event(id, subj, body, startdate, enddate, location, aanwezigen.toString()));
                            }
                            listView.requestLayout();
                            eventAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "Error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void showEventsOnCalendar(final String token, String url) {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
        } catch (Exception e) {
            Log.d("Parameters", "Failed to put parameters: " + e.toString());
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
            /* Successfully called graph, process data and send to UI */
                        Log.d("ListEvents", "Response: " + response.toString());

                        try {
                            JSONArray subject = response.getJSONArray("value");
                            for (int i = 0; i < subject.length(); i++) {
                                String subj = (subject.getJSONObject(i).get("subject")).toString();
                                String startdate = (subject.getJSONObject(i).getJSONObject("start").getString("dateTime"));
                                String enddate = (subject.getJSONObject(i).getJSONObject("end").getString("dateTime"));


                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                Date sd = null;//You will get date object relative to server/client timezone wherever it is parsed
                                sd = dateFormat.parse(startdate);
                                com.github.sundeepk.compactcalendarview.domain.Event event = new com.github.sundeepk.compactcalendarview.domain.Event(Color.BLACK, sd.getTime(), subj);
                                compactCalendar.addEvent(event);

                                Date ed = dateFormat.parse(enddate);
                                Calendar s = Calendar.getInstance();
                                s.setTime(sd);
                                s.add(Calendar.DATE, 1);
                                com.github.sundeepk.compactcalendarview.domain.Event evt = new com.github.sundeepk.compactcalendarview.domain.Event(Color.BLACK, ed.getTime(), subj);
                                compactCalendar.addEvent(evt);
                                Calendar e = Calendar.getInstance();
                                e.setTime(ed);

                                Long x = sd.getTime() + 1;
                                while(s.before(e)){
                                    com.github.sundeepk.compactcalendarview.domain.Event y = new com.github.sundeepk.compactcalendarview.domain.Event(Color.BLACK, s.getTimeInMillis(), subj);
                                    compactCalendar.addEvent(y);
                                    s.add(Calendar.DATE, 1);
                                    Log.d("dateClicked", s.toString());
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "Error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }



}
