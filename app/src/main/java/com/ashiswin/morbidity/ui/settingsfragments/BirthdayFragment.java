package com.ashiswin.morbidity.ui.settingsfragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ashiswin.morbidity.ui.GetStartedActivity;
import com.ashiswin.morbidity.R;
import com.ashiswin.morbidity.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class BirthdayFragment extends Fragment implements SettingsFragmentInterface {
    TextView txtSubtitle;
    Button btnBirthday;

    Calendar myCalendar = Calendar.getInstance();

    public BirthdayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_birthday, container, false);

        final Button btnNext = rootView.findViewById(R.id.btnNext);
        txtSubtitle = rootView.findViewById(R.id.txtSubtitle);
        btnBirthday = rootView.findViewById(R.id.btnBirthday);

        // Store date in calendar object and set date on btnBirthday
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        // Set calendar dialog to pop up
        btnBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(rootView.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Set next button
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

    @Override
    public Pair<String, String> getSetting() {
        String date = Constants.BIRTHDAY_FORMAT.format(myCalendar.getTime());
        return new Pair<>(Constants.PREF_BIRTHDAY, date);
    }

    private void updateLabel() {
        String myFormat = "dd MMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        btnBirthday.setText(sdf.format(myCalendar.getTime()));
    }
}
