package com.ashiswin.morbidity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.ashiswin.morbidity.utils.DatePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final SimpleDateFormat birthdayFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.US);

    private Button btnBirthday, btnDone;
    private Spinner spnSex, spnCountry;
    private Date birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBirthday = findViewById(R.id.btnBirthday);
        spnSex = findViewById(R.id.spnSex);
        spnCountry = findViewById(R.id.spnCountry);
        btnDone = findViewById(R.id.btnDone);

        String[] countries = getResources().getStringArray(R.array.countries);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, countries);
        spnCountry.setAdapter(countryAdapter);

        String[] sexes = getResources().getStringArray(R.array.sexes);
        ArrayAdapter<String> sexAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, sexes);
        spnSex.setAdapter(sexAdapter);

        btnBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String[] months = getResources().getStringArray(R.array.months);
                        try {
                            birthday = birthdayFormat.parse(year + "-" + (month + 1) + "-" + dayOfMonth);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        btnBirthday.setText(getString(R.string.strBirthday, dayOfMonth, months[month], year));
                    }
                });
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("birthday", birthdayFormat.format(birthday));
                editor.putString("country", (String) spnCountry.getSelectedItem());
                editor.putString("sex", (String) spnSex.getSelectedItem());
            }
        });
    }

}