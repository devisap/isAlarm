package com.id.ac.stiki.doleno.isalarm.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {
    static final String PREF = "PREF";
    static final String GROUP_PREF = "GROUP_PREF";
    static final String CODE_PREF = "CODE_PREF";

    public static void saveGroup(Context context, int i){
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit().putInt(GROUP_PREF, i).apply();
    }

    public static int getGroup(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        if(pref.contains(GROUP_PREF)){
            return pref.getInt(GROUP_PREF, 0);
        }
        return 0;
    }

    public static void removeGroup(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        if(pref.contains(GROUP_PREF)){
            pref.edit().remove(GROUP_PREF).apply();
        }
    }

    public static void saveCode(Context context, int i){
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit().putInt(CODE_PREF, i).apply();
    }

    public static int getCode(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        if(pref.contains(CODE_PREF)){
            return pref.getInt(CODE_PREF, 0);
        }
        return 0;
    }

    public static void removeCode(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        if(pref.contains(CODE_PREF)){
            pref.edit().remove(CODE_PREF).apply();
        }
    }
}
