package com.example.marvin.ecommerce.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.marvin.ecommerce.Activity.Constants;
import com.example.marvin.ecommerce.AdapterClasses.MyAdapter;
import com.example.marvin.ecommerce.Model.Upload;
import com.example.marvin.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowDatabaseFragment extends Fragment
{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private DatabaseReference mDatabaseReference;
    private CircleProgressBar circleProgressBar;
    private LinearLayout materialcircleProgressBar;

    private List<Upload> uploads;

    boolean doubleBackToExitPressedOnce = false;

    public ShowDatabaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_show_database, container, false);

        mRecyclerView=view.findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        circleProgressBar=view.findViewById(R.id.materialProgressBar);
        circleProgressBar.setColorSchemeResources(R.color.colorPrimary);

        materialcircleProgressBar=view.findViewById(R.id.materialProgressBar_LinearLayout);


        uploads=new ArrayList<>();


        circleProgressBar.setVisibility(View.VISIBLE);
        materialcircleProgressBar.setVisibility(View.VISIBLE);

        mDatabaseReference= FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                circleProgressBar.setVisibility(View.GONE);
                materialcircleProgressBar.setVisibility(View.GONE);

                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Upload upload=postSnapshot.getValue(Upload.class);
                    uploads.add(upload);
                }
                mAdapter=new MyAdapter(getContext(),uploads);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;


    }

    /***
     onBackPressed()
     ****/
    @Override
    public void onResume() {
        super.onResume();

        if (getView()==null)
        {
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if (keyEvent.getAction()==KeyEvent.ACTION_UP && i==KeyEvent.KEYCODE_BACK)
                {
                    if (doubleBackToExitPressedOnce) {
                        getActivity().onBackPressed();
                        }

                        else
                            {
                                doubleBackToExitPressedOnce=true;
                                Toast.makeText(getActivity(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                            }

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    doubleBackToExitPressedOnce=false;
                                }
                            },3000);




                    return true;
                }
                return false;
            }
        });
    }
}
