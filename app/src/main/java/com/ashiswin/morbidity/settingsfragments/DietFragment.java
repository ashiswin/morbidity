package com.ashiswin.morbidity.settingsfragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ashiswin.morbidity.GetStartedActivity;
import com.ashiswin.morbidity.R;
import com.ashiswin.morbidity.utils.Constants;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * A simple {@link Fragment} subclass.
 */
public class DietFragment extends Fragment implements SettingsFragmentInterface {
    TextView txtSubtitle;
    Spinner spnDiet;
    Button btnNext;

    public DietFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_diet, container, false);

        spnDiet = rootView.findViewById(R.id.spnDiet);
        btnNext = rootView.findViewById(R.id.btnNext);
        txtSubtitle = rootView.findViewById(R.id.txtSubtitle);

        ArrayAdapter dietAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, getResources().getStringArray(R.array.diets));
        dietAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spnDiet.setAdapter(dietAdapter);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetStartedActivity parent = (GetStartedActivity) getActivity();
                parent.nextFragment(3);
            }
        });

        return rootView;
    }
    public void setName(String name) {
        txtSubtitle.setText("What is your diet like " + name + "?");
    }

    public Pair<String, String> getSetting() {
        return new Pair<>(Constants.PREF_DIET, spnDiet.getSelectedItem().toString());
    }
}
