package com.ashiswin.morbidity.ui.settingsfragments;

import android.util.Pair;

public interface SettingsFragmentInterface {

    /**
     * All fragments in Get Started implement getter for data
     * @return Pair where the key is from Constants
     */
    public Pair<String, String> getSetting();

    public void setName(String name);
}
