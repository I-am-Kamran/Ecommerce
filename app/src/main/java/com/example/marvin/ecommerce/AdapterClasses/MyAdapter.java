package com.example.marvin.ecommerce.AdapterClasses;

import com.bumptech.glide.Glide;
import com.example.marvin.ecommerce.Fragment.DescriptionFragment;
import com.example.marvin.ecommerce.Model.Upload;
import com.example.marvin.ecommerce.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
{
    private Context context;
    private List<Upload> uploads ;

    public MyAdapter(Context context, List<Upload> uploads)
    {
        this.context = context;
        this.uploads = uploads;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);

        MyViewHolder viewHolder=new MyViewHolder(view,context,uploads);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        Upload upload=uploads.get(position);

        holder.textPrice.setText("₹"+upload.getPrice());
        holder.textName.setText(upload.getName());
        holder.textName.setTextColor(Color.BLACK);

        Glide.with(context).load(upload.photoUrl).into(holder.imageCard);

    }

    @Override
    public int getItemCount()
    {
        return uploads.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView textName,textPrice;
        public ImageView imageCard;

        public Context context;
        public List<Upload> uploads;

        public MyViewHolder(View view,Context context,List<Upload> uploads)
        {
            super(view);

            this.context=context;
            this.uploads=uploads;
            view.setOnClickListener(this);

            textName=view.findViewById(R.id.cardName);
            textPrice=view.findViewById(R.id.cardPrice);
            imageCard=view.findViewById(R.id.cardImage);

        }

        @Override
        public void onClick(View view)
        {
            int position=getAdapterPosition();
            Upload upload=uploads.get(position);

            DescriptionFragment df=new DescriptionFragment();
            Bundle bundle=new Bundle();

            bundle.putString("productId",upload.getProductId());
            bundle.putString("Name",upload.getName());
            bundle.putString("Price",upload.getPrice());
            bundle.putString("Photo",upload.getPhotoUrl());
            bundle.putString("Description",upload.getDescription());
            bundle.putString("Quantity",upload.getQuantity());

            df.setArguments(bundle);

            AppCompatActivity ac= (AppCompatActivity) view.getContext();

            FragmentManager fm=ac.getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.frame,df);
            ft.addToBackStack(null);
            ft.commit();

        }
    }



}
