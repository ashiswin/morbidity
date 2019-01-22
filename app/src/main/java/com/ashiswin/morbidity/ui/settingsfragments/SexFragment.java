package com.ashiswin.morbidity.ui.settingsfragments;


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

import com.ashiswin.morbidity.ui.GetStartedActivity;
import com.ashiswin.morbidity.R;
import com.ashiswin.morbidity.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class SexFragment extends Fragment implements SettingsFragmentInterface {
    TextView txtSubtitle;
    Spinner spnSex;

    public SexFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sex, container, false);

        spnSex = rootView.findViewById(R.id.spnSex);
        Button btnNext = rootView.findViewById(R.id.btnNext);
        txtSubtitle = rootView.findViewById(R.id.txtSubtitle);

        ArrayAdapter sexAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, getResources().getStringArray(R.array.sexes));
        sexAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spnSex.setAdapter(sexAdapter);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetStartedActivity parent = (GetStartedActivity) getActivity();
                parent.nextFragment(2);
            }
        });

        return rootView;
    }

    public void setName(String name) {
        txtSubtitle.setText("What is your sex " + name + "?");
    }

    @Override
    public Pair<String, String> getSetting() {
        return new Pair<>(Constants.PREF_SEX, spnSex.getSelectedItem().toString());
    }
}
