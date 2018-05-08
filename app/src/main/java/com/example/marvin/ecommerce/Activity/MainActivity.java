package com.example.marvin.ecommerce.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.marvin.ecommerce.Fragment.LoginPage_Fragment;
import com.example.marvin.ecommerce.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.mainActivity,new LoginPage_Fragment());
        ft.commit();*/

        Intent intent= new Intent(MainActivity.this,LoginPageActivity.class);
        startActivity(intent);
        finish();
    }
}
