package com.ashiswin.morbidity.utils;

/**
 * Created by ashis on 7/8/2018.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.Preference;
import android.util.AttributeSet;

public class DatePreference extends DialogPreference {

    private final String TAG = getClass().getSimpleName();

    public String dateString;

    public DatePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static String dateToString(Calendar calendar) {
        return summaryFormatter().format(calendar.getTime());
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            if (defaultValue == null) dateString = getPersistedString(defaultValue());
            else dateString = getPersistedString(defaultValue.toString());
            setTheDate(dateString);
        } else {
            final boolean wasNull = dateString == null;
            setDate((String) defaultValue);
            if (!wasNull) {
                persistStringValue(dateString);
            }
        }
    }

    private String defaultValue() {
        if (this.dateString == null) {
            setDate(defaultCalendarString());
        }
        return this.dateString;
    }

    public static String defaultCalendarString() {
        return formatter().format(defaultCalendar().getTime());
    }

    public static SimpleDateFormat formatter() {
        return new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
    }

    public static SimpleDateFormat summaryFormatter() {
        return new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
    }

    public static Calendar defaultCalendar() {
        return new GregorianCalendar();
    }

    public void setTheDate(String s) {
        setDate(s);
        persistStringValue(s);
    }

    public void persistStringValue(String value) {
        persistString(value);
        setSummary(summaryFormatter().format(getDate().getTime()));
    }

    public void setDate(String dateString) {
        this.dateString = dateString;
    }

    public Calendar getDate() {
        try {
            final SimpleDateFormat sfd = formatter();
            final Date date = sfd.parse(defaultValue());
            final Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch (ParseException e) {
            return defaultCalendar();
        }
    }
}

