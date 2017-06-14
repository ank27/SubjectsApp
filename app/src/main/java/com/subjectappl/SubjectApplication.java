package com.subjectappl;

import android.app.Application;

import com.subjectappl.Utils.Toaster;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

public class SubjectApplication extends Application {

    private static final String TAG = "MainApplication";
    public static Data data;
    public static SubjectApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        RealmConfiguration realmConfiguration = buildDatabase();
        Realm.setDefaultConfiguration(realmConfiguration);
        data = new Data(this);
        Toaster.setContext(this);
    }



    public RealmConfiguration buildDatabase(){
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();

        try {
             Realm.getInstance(realmConfiguration);
            return realmConfiguration;
        } catch (RealmMigrationNeededException e){
            try {
                Realm.deleteRealm(realmConfiguration);
                return realmConfiguration;
            } catch (Exception ex){
                throw ex;
            }
        }
    }


}

