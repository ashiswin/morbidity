package com.ashiswin.morbidity.settingsfragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.ashiswin.morbidity.GetStartedActivity;
import com.ashiswin.morbidity.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NameFragment extends Fragment {
    public String name;

    public NameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_name, container, false);

        EditText edtName = rootView.findViewById(R.id.edtName);
        final Button btnNext = rootView.findViewById(R.id.btnNext);

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    btnNext.setEnabled(true);
                    name = s.toString();
                }
                else {
                    btnNext.setEnabled(false);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Context context = rootView.getContext();

            // Hide keyboard
            InputMethodManager imm = (InputMethodManager)
                    context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getRootView().getWindowToken(), 0);

            // Change fragment
            GetStartedActivity parent = (GetStartedActivity) getActivity();
            parent.nextFragment(0);
            }
        });
        return rootView;
    }

}
