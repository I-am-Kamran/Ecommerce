package com.example.marvin.ecommerce.HelperClass;

import android.app.Application;

import com.example.marvin.ecommerce.BroadcastReceiver.CheckedInternetConnection;

public class MyApplication extends Application
{
    private static MyApplication mInstance;

    @Override
    public void onCreate() {

        super.onCreate();
        mInstance=this;
    }

    public static synchronized MyApplication getInstance(){
        return mInstance;
    }

    public void setConnectivityListener(CheckedInternetConnection.ConnectivityReceiverListener listener)
    {
        CheckedInternetConnection.connectivityReceiverListener=listener;
    }
}
