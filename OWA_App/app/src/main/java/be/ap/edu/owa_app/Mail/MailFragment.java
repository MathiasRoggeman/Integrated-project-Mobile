package be.ap.edu.owa_app.Mail;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.ap.edu.owa_app.R;

/**
 * Created by Mathias on 15-12-2017.
 */

public class MailFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mail_fragment, container, false);
    }

}
