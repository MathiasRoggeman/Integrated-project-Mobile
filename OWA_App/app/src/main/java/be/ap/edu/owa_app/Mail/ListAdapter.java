package be.ap.edu.owa_app.Mail;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import be.ap.edu.owa_app.R;

/**
 * Created by isa_l on 06-12-17.
 */

public class ListAdapter extends ArrayAdapter<MailList> {


    // declaring our ArrayList of items
    private ArrayList<MailList> objects;
    private ImageView person_circle;
    private TextView person_circle_text;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public ListAdapter(Context context, int textViewResourceId, ArrayList<MailList> objects) {
        super(context, textViewResourceId, objects);

        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {


        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.activity_listviewmails, null);


		/*
         * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        MailList i = objects.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.
            person_circle = (ImageView) v.findViewById(R.id.person_circle);
            person_circle_text = v.findViewById(R.id.person_circle_text);
           // RandomColor randomColor = new RandomColor();

          //  int color = randomColor.randomColor(1000, RandomColor.SaturationType.RANDOM, RandomColor.Luminosity.LIGHT);
           // person_circle.setColorFilter(color);
            TextView tt = (TextView) v.findViewById(R.id.subject);
            TextView ttd = (TextView) v.findViewById(R.id.sender);
            TextView bp = (TextView) v.findViewById(R.id.body);
            // CheckBox cb = v.findViewById(R.id.delete);

            if(!(i.getSender() == null)) {
                String firstLetter = i.getSender().substring(0, 1);
                person_circle_text.append(firstLetter.toUpperCase());

            }

            // check to see if each individual textview is null.
            // if not, assign some text!
            if (tt != null) {
                tt.setText(i.getSubject());
            }
            if (ttd != null) {
                ttd.setText(i.getSender());
            }
            if (bp != null) {
                bp.setText(i.getBodyPreview());
            }
            if (!(i.isRead())) {
                tt.setTextColor(Color.RED);
                bp.setTextColor(Color.RED);
                ttd.setTextColor(Color.RED);
            }
        }

        // the view must be returned to our activity
        return v;

    }

    public MailList getItem(int position) {
        return objects.get(position);
    }
}
