package be.ap.edu.owa_app.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import be.ap.edu.owa_app.MailList;
import be.ap.edu.owa_app.R;

/**
 * Created by isa_l on 16-12-17.
 */

public class EventAdapter extends ArrayAdapter<Event> {

    private ArrayList<Event> objects;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public EventAdapter(Context context, int textViewResourceId, ArrayList<Event> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent){

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.activity_listviewevents, null);


		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        Event i = objects.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView subject = (TextView) v.findViewById(R.id.subject_event);
            TextView date = (TextView) v.findViewById(R.id.date_event);


            // check to see if each individual textview is null.
            // if not, assign some text!
            if (subject != null){
                subject.setText(i.getSubject());
            }
            if (date != null){
                String datetime = i.getStartDate() + " - " + i.getEndDate();
                date.setText(datetime);
            }
        }

        // the view must be returned to our activity
        return v;

    }

    public Event getItem(int position){
        return objects.get(position);
    }
}
