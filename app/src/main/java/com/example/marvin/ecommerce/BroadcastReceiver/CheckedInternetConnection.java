package com.example.marvin.ecommerce.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.marvin.ecommerce.HelperClass.MyApplication;

public class CheckedInternetConnection extends BroadcastReceiver
{
    public static ConnectivityReceiverListener connectivityReceiverListener;
    public CheckedInternetConnection()
    {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        ConnectivityManager cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=cm.getActiveNetworkInfo();

        boolean isConnected=activeNetwork!=null && activeNetwork.isConnectedOrConnecting();

        if (connectivityReceiverListener!=null){
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }

    public static boolean isConnected()
    {
        ConnectivityManager cm= (ConnectivityManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=cm.getActiveNetworkInfo();
        return activeNetwork!=null && activeNetwork.isConnectedOrConnecting();
    }

    /**Interface*/
    public interface ConnectivityReceiverListener
    {
       void onNetworkConnectionChanged(boolean isConnected);
    }
}
