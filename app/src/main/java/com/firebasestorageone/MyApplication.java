package com.firebasestorageone;

import android.app.Application;
import android.content.Context;

import com.firebasestorageone.utils.FirebaseUtils;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by hiren.patel on 30-11-2016.
 */
public class MyApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        FirebaseUtils.init();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        /**
         * User anyone function from below as required,
         * Read comment proper
         */


        // If you want to sync with firebase server
//        FirebaseDatabase.getInstance().goOffline();

        // If you do not want to sync with firebase server
//        FirebaseDatabase.getInstance().goOnline();
    }

    public static Context getContext(){
        return context;
    }
}
