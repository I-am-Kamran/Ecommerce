package com.example.marvin.ecommerce.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.marvin.ecommerce.Model.CartModal;
import com.example.marvin.ecommerce.Model.Upload;
import com.example.marvin.ecommerce.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class DescriptionFragment extends Fragment implements View.OnClickListener
{
    public ImageView imageView;
    public TextView txtName,txtPrice,txtDes,txtQuant;
    private Button btnAddToCart,btnBuyNow,btnGotoCart;
    Toolbar toolbarDescriptionFragment;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    String id;

    String name;
    String price;
    String description;
    String quantity;
    String image;

    public DescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_description, container, false);

        btnAddToCart=view.findViewById(R.id.addToCart);
        btnBuyNow=view.findViewById(R.id.buyNow);
        btnGotoCart=view.findViewById(R.id.goToCart);

        btnAddToCart.setOnClickListener(this);
        btnBuyNow.setOnClickListener(this);
        btnGotoCart.setOnClickListener(this);

        mDatabase=FirebaseDatabase.getInstance();
        mReference=mDatabase.getReference("cart");

        imageView=view.findViewById(R.id.imagedesc);
        txtPrice=view.findViewById(R.id.price1);
        txtName=view.findViewById(R.id.name);
        txtDes=view.findViewById(R.id.description1);
        txtQuant=view.findViewById(R.id.quantity1);

        /**toolbar*/
        toolbarDescriptionFragment=view.findViewById(R.id.toolbar_descriptionFragment);
        toolbarDescriptionFragment.setTitle("Details");
        toolbarDescriptionFragment.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbarDescriptionFragment.setTitleTextColor(Color.WHITE);

        toolbarDescriptionFragment.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.frame,new ShowDatabaseFragment());
                ft.commit();
            }
        });



        Bundle b=getArguments();
             id=b.getString("productId");
             name = b.getString("Name");
             price = b.getString("Price");
            description = b.getString("Description");
            quantity = b.getString("Quantity");
            image = b.getString("Photo");

         Glide.with(getActivity()).load(image).into(imageView);

            txtName.setText("  "+name);
            txtPrice.setText("  ₹"+price+"/-");
          txtDes.setText("  "+description);
           txtQuant.setText("  "+quantity);

        return view;
    }

    @Override
    public void onClick(View view)
    {
        int id=view.getId();

        switch (id)
        {
            case R.id.addToCart:
                uploadIntoFirebase(view);
                btnAddToCart.setVisibility(View.GONE);
                btnGotoCart.setVisibility(View.VISIBLE);
                break;

            case R.id.goToCart:
               moveToCart();

                break;

        }

    }

    private void moveToCart()
    {
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.frame,new Cart_Fragment());
        ft.addToBackStack(null);
        ft.setTransition(android.R.anim.fade_in);
        ft.commit();
    }

    private void uploadIntoFirebase(View v)
    {
        int index=1;

        if (index==1)
        {


            CartModal upload = new CartModal();

            upload.setProductId(id);
            upload.setName(name);
            upload.setPrice(price);
            upload.setQuantity(quantity);
            upload.setPhotoUrl(image);

            mReference.child(id).setValue(upload);

            Snackbar.make(v, "Add In Cart Sucessfully!!", 3000).show();
        }
        btnAddToCart.setVisibility(View.GONE);
        btnGotoCart.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
}
