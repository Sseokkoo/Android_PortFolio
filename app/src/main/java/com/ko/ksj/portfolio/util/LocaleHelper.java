package com.ko.ksj.portfolio.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.Preference;

import org.json.JSONArray;

import java.util.Locale;

public class LocaleHelper {
//    private static final String SELECTED_LANGUAGE = "Helper.Locale.Language";

    // the method is used to set the language at runtime
    public static Context setLocale(Context context, String language, String country) {
         persist(context, language, country);

        // updating the language for devices above android nougat
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            return updateResources(context, language, country);
//        }

        // for devices having lower version of android os
        return updateResourcesLegacy(context, language, country);
    }

    private static void persist(Context context, String language, String country) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SELECTED_LANGUAGE, language);
//        editor.apply();

        SharedPreferences pref = context.getSharedPreferences("Info",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("lang",language);
        editor.putString("country", country);
        editor.apply();
    }

    // the method is used update the language of application by creating
    // object of inbuilt Locale class and passing language argument to it
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language, String country) {
        Locale locale = new Locale(language, country);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }


    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language, String country) {
        Locale locale = new Locale(language, country);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }
}