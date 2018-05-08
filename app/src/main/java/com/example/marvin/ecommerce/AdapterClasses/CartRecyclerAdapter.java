package com.example.marvin.ecommerce.AdapterClasses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.marvin.ecommerce.Model.CartModal;
import com.example.marvin.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder>
{
    private Context context;
    private List<CartModal> cartModals;

    public CartRecyclerAdapter(Context context, List<CartModal> cartModals) {
        this.context = context;
        this.cartModals = cartModals;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_for_cart,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,context,cartModals);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        CartModal cartModal=cartModals.get(position);

        holder.name_Cart.setText(cartModal.getName());
        holder.price_Cart.setText("₹"+cartModal.getPrice());
        holder.quantity_Cart.setText("Total quantity "+cartModal.getQuantity());
        holder.textViewProductQuantity.setText(cartModal.getPosition()+"");

        Glide.with(context).load(cartModal.getPhotoUrl()).into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return cartModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView name_Cart,price_Cart,quantity_Cart;
        ImageView imageView;
        TextView textViewProductQuantity;

        Button btnQuantityMinus;
        Button btnQuantityPlus;
        Button btnRemoveFromCart;
        Button btnMoveToWishList;

        public String product_Id;
        public String product_Name;
        public String product_Price;
        public String product_PhotoUrl;
        public String product_Description;
        public String product_Quantity;

        int initialProductPrice;

        DatabaseReference databaseReferenceCart,databaseReferenceInitial;
        List<CartModal> cartModalList = new ArrayList<>();
        Context context;

        List<String> stringList;
        int quantity;
        int totalAmount;
        int countQuantity;
        public ViewHolder(View itemView, Context context, List<CartModal> cartModals)
        {
            super(itemView);
            this.cartModalList=cartModals;
            this.context=context;

            name_Cart=itemView.findViewById(R.id.NameId);
            price_Cart=itemView.findViewById(R.id.PriceId);
            imageView = itemView.findViewById(R.id.imageCart);
            quantity_Cart=itemView.findViewById(R.id.quantityId);

            btnQuantityPlus=itemView.findViewById(R.id.addQunatity);
            btnQuantityMinus=itemView.findViewById(R.id.subtractQuantity);
            textViewProductQuantity=itemView.findViewById(R.id.txtProductQuantity);

            btnRemoveFromCart=itemView.findViewById(R.id.Button_ItemRemove);
            btnMoveToWishList=itemView.findViewById(R.id.Button_moveToWhishList);

            btnRemoveFromCart.setOnClickListener(this);
            btnMoveToWishList.setOnClickListener(this);

            btnQuantityMinus.setOnClickListener(this);
            btnQuantityPlus.setOnClickListener(this);

            stringList = new ArrayList<String>();

            databaseReferenceCart = FirebaseDatabase.getInstance().getReference("cart");
            databaseReferenceInitial = FirebaseDatabase.getInstance().getReference("uploads");
        }

        @Override
        public void onClick(View view) {

            final int position = getAdapterPosition();

            CartModal cartModal=this.cartModalList.get(position);

            product_Id = cartModal.getProductId();
            product_Name = cartModal.getName();
            product_Price = cartModal.getPrice();
            product_PhotoUrl = cartModal.getPhotoUrl();
            product_Description = cartModal.getDescription();
            product_Quantity = cartModal.getQuantity();
            countQuantity=Integer.parseInt(cartModal.getPosition());

           DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cart").child(cartModal.getProductId());

            if (view.getId()==btnQuantityPlus.getId())
            {
                databaseReferenceInitial.child(product_Id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            String initialData = postSnapshot.getValue(String.class);
                            stringList.add(initialData);
                        }

                         initialProductPrice = Integer.parseInt(stringList.get(3));
                        if (countQuantity<Integer.valueOf(product_Quantity))
                        {
                            countQuantity++;

                            textViewProductQuantity.setText(""+countQuantity);
                            totalAmount=initialProductPrice*countQuantity;

                            price_Cart.setText(""+totalAmount);

                            updatePrice(product_Id,product_Name,String.valueOf(totalAmount),product_PhotoUrl,product_Quantity,String.valueOf(countQuantity));


                        }
                        else{
                            Toast.makeText(context, "Invalid", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            else if (view.getId()==btnQuantityMinus.getId())
            {
                databaseReferenceInitial.child(product_Id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            String initialData = postSnapshot.getValue(String.class);
                            stringList.add(initialData);
                        }

                        initialProductPrice = Integer.parseInt(stringList.get(3));

                        if (countQuantity>1)
                        {
                            countQuantity--;
                            textViewProductQuantity.setText(""+countQuantity);
                            totalAmount=initialProductPrice*countQuantity;

                            price_Cart.setText("₹"+totalAmount);

                            updatePrice(product_Id,product_Name,String.valueOf(totalAmount),product_PhotoUrl,product_Quantity,String.valueOf(countQuantity));
                        }
                        else {
                            Toast.makeText(context, "invalid", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            else if (view.getId()==btnRemoveFromCart.getId())
            {
                reference.removeValue();
            }
        }

        private void updatePrice(
                String product_Id,
                String product_Name,
                String product_Price,
                String product_PhotoUrl,
                String product_Quantity,
                String product_Position)
        {

            CartModal cartModal = new CartModal();
            cartModal.setProductId(product_Id);
            cartModal.setName(product_Name);
            cartModal.setPrice(product_Price);
            cartModal.setPhotoUrl(product_PhotoUrl);
            cartModal.setQuantity(product_Quantity);
            cartModal.setPosition(product_Position);

            DatabaseReference df = FirebaseDatabase.getInstance().getReference("cart").child(product_Id);
            df.setValue(cartModal);
        }
    }
}
