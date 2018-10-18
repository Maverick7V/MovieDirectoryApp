package com.examole.moviedirectory.moviedirectory.Util;

import android.app.Activity;
import android.content.SharedPreferences;

/* a Class to store the last searched item
 * Using shared preferences to store the last searched item
 * Keeping default as "batman"
 */
public class Prefs {
    SharedPreferences sharedPreferences;

    public Prefs(Activity activity){
        sharedPreferences=activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void setSearch(String search){
        sharedPreferences.edit().putString("search",search).commit();
    }
    public String getSearch(){
        return sharedPreferences.getString("search","batman");
        //returning batman as the default value
    }
}
