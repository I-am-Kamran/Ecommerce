package com.example.marvin.ecommerce.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marvin.ecommerce.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends Fragment {

    RatingBar ratingBar;
    TextView textViewRating;
    Button btnRating;



    public RatingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_rating, container, false);

        textViewRating=view.findViewById(R.id.textRating);
        ratingBar=view.findViewById(R.id.ratingBar);
        btnRating=view.findViewById(R.id.buttonRating);

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                ratingBar.setRating(5);
                textViewRating.setText("Excellent");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        FragmentManager fm=getFragmentManager();
                        FragmentTransaction ft=fm.beginTransaction();
                        ft.replace(R.id.frame,new ShowDatabaseFragment());
                        ft.addToBackStack(null);
                        ft.commit();

                        Toast.makeText(getContext(), "Thanks! for your feedback", Toast.LENGTH_SHORT).show();
                    }
                },2000);



            }
        });



        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

              //textViewRating.setText(" "+String.valueOf(v));
               if(v<2.0)
                {
                    textViewRating.setText("Good");
                }
                else if (v>=2.0 && v<=4.0)
                    {
                        textViewRating.setText("Best");
                    }
                    else
                        {
                            textViewRating.setText("Excellent");
                        }

            }
        });

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

}
