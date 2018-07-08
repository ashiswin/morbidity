package com.ashiswin.morbidity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ashis on 7/8/2018.
 */

public class DatePreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat implements DialogPreference.TargetFragment, DatePicker.OnDateChangedListener {

    DatePicker datePicker = null;
    private String changedValueCanBeNull;
    private Calendar maxCalendar;
    private int maxYear;
    private int maxMonth;
    private int maxDay;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected View onCreateDialogView(Context context) {
        datePicker = new DatePicker(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
        } else {
            maxCalendar = Calendar.getInstance();
            maxYear = maxCalendar.get(Calendar.YEAR);
            maxMonth = maxCalendar.get(Calendar.MONTH);
            maxDay = maxCalendar.get(Calendar.DAY_OF_MONTH);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.datePicker.setCalendarViewShown(false);
        }

        DatePreference pref = (DatePreference) getPreference();
        Calendar calendar = pref.getDate();
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);
        return datePicker;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        DatePreference pref = (DatePreference) getPreference();
        String value = DatePreference.dateToString(pref.getDate());
        if (pref.callChangeListener(value)) pref.persistStringValue(value);
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult && changedValueCanBeNull != null) {
            DatePreference pref = (DatePreference) getPreference();
            pref.setTheDate(changedValueCanBeNull);
            changedValueCanBeNull = null;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);
        datePicker.clearFocus();
        onDateChanged(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        onDialogClosed(which == DialogInterface.BUTTON_POSITIVE); // OK?
    }

    @Override
    public Preference findPreference(CharSequence charSequence) {
        return getPreference();
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar selected = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB && selected.after(maxCalendar)) {
            selected = new GregorianCalendar(maxYear, maxMonth, maxDay);
        }
        changedValueCanBeNull = DatePreference.formatter().format(selected.getTime());
    }
}
