package de.ur.mi.android.demos.healthbestie.drawer_menu_fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import de.ur.mi.android.demos.healthbestie.R;


public class SettingsFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_settings);
        nightModeSwitch();
        biometricAuthSwitch();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            view.setBackgroundColor(getResources().getColor(R.color.color_dark_bg));
        }
        return view;
    }



    private void nightModeSwitch() {
        SwitchPreference nightMode = (SwitchPreference) SettingsFragment.this.findPreference("NIGHT");
        SharedPreferences preferences = getActivity().getSharedPreferences("nightModeState", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        nightMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((boolean) newValue) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("nightModeState", true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("nightModeState", false);
                }
                editor.apply();

                return true;
            }
        });
    }

    private void biometricAuthSwitch() {
        SwitchPreference biometricAuthMode = (SwitchPreference) SettingsFragment.this.findPreference("BIOMETRIC");
        SharedPreferences preferences = getActivity().getSharedPreferences("biometricAuthState", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        biometricAuthMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((boolean) newValue) {
                    editor.putBoolean("biometricAuthState", true);
                } else {
                    editor.putBoolean("biometricAuthState", false);
                }
                editor.apply();

                return true;
            }
        });
    }



}