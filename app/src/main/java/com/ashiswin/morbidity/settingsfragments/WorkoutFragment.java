package com.ashiswin.morbidity.settingsfragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ashiswin.morbidity.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutFragment extends Fragment {
    TextView txtSubtitle;

    public WorkoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout, container, false);

        Spinner spnWorkouts = rootView.findViewById(R.id.spnWorkouts);
        txtSubtitle = rootView.findViewById(R.id.txtSubtitle);

        ArrayAdapter workoutsAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, getResources().getStringArray(R.array.workouts));
        workoutsAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spnWorkouts.setAdapter(workoutsAdapter);

        return rootView;
    }
    public void setName(String name) {
        txtSubtitle.setText("What are your workouts like " + name + "?");
    }
}
