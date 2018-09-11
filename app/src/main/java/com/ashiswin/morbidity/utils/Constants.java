package com.ashiswin.morbidity.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {
    public static final SimpleDateFormat BIRTHDAY_FORMAT = new SimpleDateFormat("yyyy.MM.dd", Locale.US);

    public static final String PREF_NAME = "name";
    public static final String PREF_BIRTHDAY = "birthday";
    public static final String PREF_SEX = "sex";
    public static final String PREF_COUNTRY = "country";
    public static final String PREF_DIET = "diet";
    public static final String PREF_WORKOUT = "workout";

    public static final int[] LIFE_EXPECTANCY = new int[]{86, 94};
}
