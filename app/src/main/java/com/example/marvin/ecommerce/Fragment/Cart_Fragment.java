package com.example.marvin.ecommerce.Fragment;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marvin.ecommerce.AdapterClasses.CartRecyclerAdapter;
import com.example.marvin.ecommerce.Model.CartModal;
import com.example.marvin.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Cart_Fragment extends Fragment
{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private List<CartModal> uploads;
    private Button btn_Continue,btn_TotalPrice;
    private Toolbar toolbarCartFragment;
    private TextView txtPrice;
    private TextView txtQuantity;
    private TextView txtTotalAmount;

    private List<String> totalPriceList=new ArrayList<>();
    int sumOfPrice;


    public Cart_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cart_, container, false);

        /**Toolbar**/
        toolbarCartFragment=view.findViewById(R.id.toolbar_CartFragment);
        toolbarCartFragment.setTitle("My Cart");
        toolbarCartFragment.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbarCartFragment.setTitleTextColor(Color.WHITE);

        toolbarCartFragment.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.frame,new ShowDatabaseFragment());
                ft.setTransition(android.R.anim.fade_in);
                ft.commit();
            }
        });





        btn_Continue=view.findViewById(R.id.button_Continue);
        btn_Continue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.frame,new Continue_Fragment());
                ft.setTransition(android.R.anim.fade_in);
                ft.addToBackStack(null);
                ft.commit();

            }
        });

        btn_TotalPrice=view.findViewById(R.id.button_TotalAmount);
        txtPrice=view.findViewById(R.id.textPrice);
        txtQuantity=view.findViewById(R.id.text_quantity);
        txtTotalAmount=view.findViewById(R.id.textTotalAmount);

        recyclerView=view.findViewById(R.id.cartRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false)
                                      {
                                          @Override
                                          public boolean canScrollHorizontally() {
                                              return false;
                                          }

                                          @Override
                                          public boolean canScrollVertically() {
                                              return false;
                                          }
                                      });

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        uploads=new ArrayList<>();

        databaseReference=FirebaseDatabase.getInstance().getReference("cart");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                if (uploads!=null  && totalPriceList!=null)
                {
                    uploads.clear();
                    totalPriceList.clear();

                }


                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    CartModal  upload=postSnapshot.getValue(CartModal.class);
                    uploads.add(upload);
                    totalPriceList.add(upload.getPrice());
                }


               // Toast.makeText(getActivity(), ""+totalPriceList.toString(), Toast.LENGTH_SHORT).show();
                sumOfPrice=0;
                for (int i=0; i<totalPriceList.size(); i++)
                {
                    sumOfPrice=sumOfPrice+Integer.parseInt(totalPriceList.get(i));
                }
               // Toast.makeText(getActivity(), ""+sumOfPrice, Toast.LENGTH_SHORT).show();

                btn_TotalPrice.setText("Total Price\n"+"₹"+sumOfPrice);
                txtPrice.setText("₹"+sumOfPrice);
                txtTotalAmount.setText("₹"+sumOfPrice);

              //  int size=uploads.size();

                if(uploads.size()!=0) {
                    txtQuantity.setText(""+uploads.size());
                }

                adapter=new CartRecyclerAdapter(getContext(),uploads);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
