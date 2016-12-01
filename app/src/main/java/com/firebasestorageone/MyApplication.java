package com.firebasestorageone;

import android.app.Application;

import com.firebasestorageone.utils.FirebaseUtils;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by hiren.patel on 30-11-2016.
 */
public class MyApplication extends Application {

    private final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseUtils.init();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
