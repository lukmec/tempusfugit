package de.lumdev.tempusfugit.settings;

import android.content.Context;
import android.util.AttributeSet;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import de.lumdev.tempusfugit.R;


//---------------------not needed anymore -------------------------------------------------------------
public class MultipleValuePreference extends Preference {

    private String stringValue;
    private int intValue;

    public MultipleValuePreference(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public String getStringValue() {
        return stringValue;
    }
    public void setStringValue(String stringValue, String valueIdInSharedPreferences) {
        this.stringValue = stringValue;
        //save value to sharedPreferences
//        activity.getPreferences(Context.MODE_PRIVATE).edit()
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putString(valueIdInSharedPreferences, stringValue)
                .apply();

    }
    public int getIntValue() {
        return intValue;
    }
    public void setIntValue(int intValue, String valueIdInSharedPreferences) {
        this.intValue = intValue;
        //save value to sharedPreferences
//        activity.getPreferences(Context.MODE_PRIVATE).edit()
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putInt(valueIdInSharedPreferences, intValue)
                .apply();
    }

}
