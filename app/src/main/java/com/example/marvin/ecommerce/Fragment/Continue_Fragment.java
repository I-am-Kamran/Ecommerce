package com.example.marvin.ecommerce.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marvin.ecommerce.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Continue_Fragment extends Fragment
{

    private Toolbar toolbarContinue;


    public Continue_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_continue_, container, false);

        toolbarContinue=view.findViewById(R.id.toolbar_continueFragment);
        toolbarContinue.setTitle("Enter Shipping Address");
        toolbarContinue.setTitleTextColor(Color.WHITE);
        toolbarContinue.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbarContinue.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.frame,new Cart_Fragment());
                ft.commit();
            }
        });

        return view;
    }

    @Override
    public void onStop()
    {
        super.onStop();

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

}
