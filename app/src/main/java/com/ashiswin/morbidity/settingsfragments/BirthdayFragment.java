package com.ashiswin.morbidity.settingsfragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ashiswin.morbidity.GetStartedActivity;
import com.ashiswin.morbidity.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BirthdayFragment extends Fragment {
    TextView txtSubtitle;

    public BirthdayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_birthday, container, false);

        final Button btnNext = rootView.findViewById(R.id.btnNext);
        txtSubtitle = rootView.findViewById(R.id.txtSubtitle);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetStartedActivity parent = (GetStartedActivity) getActivity();
                parent.nextFragment(1);
            }
        });

        return rootView;
    }
    public void setName(String name) {
        txtSubtitle.setText("When were you born " + name + "?");
    }
}
