package com.ashiswin.morbidity.settingsfragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ashiswin.morbidity.HomeActivity;
import com.ashiswin.morbidity.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutFragment extends Fragment {
    TextView txtSubtitle;
    Button btnDone;

    public WorkoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_workout, container, false);

        Spinner spnWorkouts = rootView.findViewById(R.id.spnWorkouts);
        txtSubtitle = rootView.findViewById(R.id.txtSubtitle);
        btnDone = rootView.findViewById(R.id.btnDone);

        ArrayAdapter workoutsAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, getResources().getStringArray(R.array.workouts));
        workoutsAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spnWorkouts.setAdapter(workoutsAdapter);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });

        return rootView;
    }
    public void setName(String name) {
        txtSubtitle.setText("What are your workouts like " + name + "?");
    }
}
