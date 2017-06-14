package com.subjectappl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.subjectappl.Models.Subject;
import com.subjectappl.Utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class Data {
    public final String TAG = "Data";
    static SubjectApplication mainApp;
    private Context context;
    ConnectionDetector connectionDetector;
    Realm mRealm;
    public static final String saveTag = "SubjectDATA";
    public static ArrayList<Subject> subject_list=new ArrayList<>();
    public Data(Context cont) {
        context = cont;
        mainApp = (SubjectApplication) cont;
        mRealm = Realm.getDefaultInstance();
        connectionDetector=new ConnectionDetector(cont);
        if (mRealm.where(Subject.class).findAll().size()==0) {
            loadJson(context);
        }
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("SubjectInfo.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void loadJson(Context context) {
        try {
            JSONArray jsonArray= new JSONArray(loadJSONFromAsset(context));
            Subject subject;
            ArrayList<Subject> subjectArrayList =new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject subjectJson = jsonArray.getJSONObject(i);
                int id=subjectJson.getInt("SubjectId");
                String subject_name= subjectJson.getString("SubjectName");
                String subject_image = subjectJson.getString("SubjectImage");
                String subject_desc = subjectJson.getString("SubjectDesc");

                subject = new Subject(id,subject_name,subject_desc,subject_image);
                subjectArrayList.add(subject);
            }
            SubjectApplication.data.subject_list= subjectArrayList;

            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            mRealm.copyToRealmOrUpdate(subjectArrayList);
            mRealm.commitTransaction();
            mRealm.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public boolean isConnectedToInternet(){
        if(!connectionDetector.isConnectedToInternet()) {
            Log.d(TAG,"not connected to internet");
            return false;
        }else {
            Log.d(TAG,"connected to internet");
            return true;
        }
    }

    public String loadData(String name) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString(name, "");
    }

    public void saveData(String name, String value) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        sp.edit().putString(name, value).commit();
    }

    public boolean loadBooleanData(String name) {
        SharedPreferences prefs = context.getSharedPreferences(saveTag, Context.MODE_PRIVATE);
        return prefs.getBoolean(name, false);
    }

    public void saveData(String name, Boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(saveTag, Context.MODE_PRIVATE).edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public void saveData(String name, double value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(saveTag, Context.MODE_PRIVATE).edit();
        editor.putLong(name, Double.doubleToRawLongBits(value));
        editor.commit();
    }

    public double loadDoubleData(String name) {
        SharedPreferences prefs = context.getSharedPreferences(saveTag, Context.MODE_PRIVATE);
        return Double.longBitsToDouble(prefs.getLong(name, 0));
    }

    public void saveData(String name, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(saveTag, Context.MODE_PRIVATE).edit();
        editor.putInt(name, value);
        editor.commit();
    }


    public void saveIntData(String name, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(saveTag, Context.MODE_PRIVATE).edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public int loadIntData(String name) {
        SharedPreferences prefs = context.getSharedPreferences(saveTag, Context.MODE_PRIVATE);
        int restoredInt = prefs.getInt(name, 0);
        return restoredInt;
    }

    public void saveLongData(String name, long value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(saveTag, Context.MODE_PRIVATE).edit();
        editor.putLong(name, value);
        editor.commit();
    }

    public long loadLongData(String name) {
        SharedPreferences prefs = context.getSharedPreferences(saveTag, Context.MODE_PRIVATE);
        long restoredInt = prefs.getLong(name, 0);
        return restoredInt;
    }

    public void clear() {
        SharedPreferences.Editor editor = context.getSharedPreferences(saveTag, Context.MODE_PRIVATE).edit();
        editor.clear().apply();
    }
}
