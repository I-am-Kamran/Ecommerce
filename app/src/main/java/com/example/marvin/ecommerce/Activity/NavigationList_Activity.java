package com.example.marvin.ecommerce.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.marvin.ecommerce.BroadcastReceiver.CheckedInternetConnection;
import com.example.marvin.ecommerce.Fragment.AdminFragment;
import com.example.marvin.ecommerce.Fragment.Cart_Fragment;
import com.example.marvin.ecommerce.Converter;
import com.example.marvin.ecommerce.Fragment.LoginPage_Fragment;
import com.example.marvin.ecommerce.Fragment.RatingFragment;
import com.example.marvin.ecommerce.R;
import com.example.marvin.ecommerce.Fragment.ShowDatabaseFragment;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationList_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

private View mNavHeader;
private CircleImageView imageView;
private TextView tvEmail,tvName;
private GoogleApiClient googleApiClient;
private DrawerLayout drawer;
private int cart_count=0;
private DatabaseReference mReference;

SharedPreferences sharedPreferences;
SharedPreferences.Editor editor;
String name,email;
int size=0;



//for Google
    private String google_userName;
    private String google_Email;
    private String google_profilePic;

    //for Facebook
    String TAG = "NavigationListActivity";
    private String fb_firstName;
    private String fb_LastName;
    private String fb_Email;
    private String fb_profileUrl;

private Menu menu;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_list_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
       sharedPreferences=getSharedPreferences("myEmailPass",Context.MODE_PRIVATE);
       mReference=FirebaseDatabase.getInstance().getReference();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mNavHeader=navigationView.getHeaderView(0);
        tvEmail=mNavHeader.findViewById(R.id.textViewEmail);
        tvName=mNavHeader.findViewById(R.id.textViewName);
        imageView=mNavHeader.findViewById(R.id.imageView);

        //get data from the previous activity
       Bundle b=getIntent().getExtras();

        if (b!=null)
        {
                String Name = b.getString("gName");
                String Email = b.getString("gEmail");
                String PhotoUrl = b.getString("gPhotoUrl");
                tvName.setText(Name);
                tvEmail.setText(Email);
                Glide.with(this).load(PhotoUrl).asBitmap().thumbnail(0.0f)
                        .into(new BitmapImageViewTarget(imageView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                super.setResource(resource);
                                RoundedBitmapDrawable cd = RoundedBitmapDrawableFactory
                                        .create(getApplicationContext().getResources(), resource);
                                cd.setCircular(true);

                                imageView.setImageDrawable(cd);
                            }
                        });
            FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
            ft1.replace(R.id.frame, new ShowDatabaseFragment());
            ft1.commit();
        }

        GoogleSignInOptions signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
         googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
         checkConnection();



         /***facebook**/
        fb_firstName= sharedPreferences.getString("facebook_first_name", null);
        fb_LastName = sharedPreferences.getString("facebook_last_name", null);
        fb_profileUrl = sharedPreferences.getString("facebook_image_url", null);
        fb_Email=sharedPreferences.getString("facebook_email",null);

      //  tvName.setText(first_name+" "+last_name);
     //   new NavigationList_Activity.DownloadImage(imageView).execute(imageUrl);

        Toast.makeText(this, ""+fb_firstName+fb_LastName, Toast.LENGTH_SHORT).show();
         if (fb_Email!=null || fb_firstName!=null || fb_LastName!=null || fb_profileUrl!=null)
         {
             tvName.setText(fb_firstName+" "+fb_LastName);
             tvEmail.setText(fb_Email);
             new NavigationList_Activity.DownloadImage(imageView).execute(fb_profileUrl);

             FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
             ft1.replace(R.id.frame, new ShowDatabaseFragment());
             ft1.commit();

         }
      /*   else if (google_userName !=null || google_Email !=null || google_profilePic !=null) {


             FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
             ft1.replace(R.id.frame, new ShowDatabaseFragment());
             ft1.commit();
         }*/


    }

    /**Method to manually chech internet connection**/
    private void checkConnection()
    {
        boolean isConnected=CheckedInternetConnection.isConnected();
        showSnack(isConnected);
    }
    /**Showing the status in SnackBar*/
    private void showSnack(boolean isConnected)
    {
        String message;
        int color;
        if (isConnected)
        {
            message="Connected to Internet";
            color= Color.WHITE;
        }
        else
            {
                message="Not Connected to Internet";
                color=Color.RED;
            }
        Snackbar snackbar=Snackbar.make(findViewById(R.id.frame),message,Snackbar.LENGTH_LONG);

        View sbView=snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         /*Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_list_, menu);*/
         this.menu=menu;
        getMenuInflater().inflate(R.menu.icons_set,menu);
        countTotalChildFirebase();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart_action)
        {
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.frame,new Cart_Fragment());
            ft.addToBackStack(null);
            ft.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_admin)
        {
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.frame,new AdminFragment());
            ft.addToBackStack(null);
            ft.commit();
        }
        else if (id == R.id.nav_account)
        {

        } else if (id == R.id.nav_cart)
        {
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.frame,new Cart_Fragment());
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share)
        {
            shareApp();

        }
        else if (id == R.id.nav_rating)
        {
            ratingMyApp();
        }

        else if (id == R.id.nav_logout)
        {
            LoginManager.getInstance().logOut();
            sharedPreferences.edit().clear().apply();
           // fbLogout();

            tvName.setText(" ");
            tvEmail.setText("Login");
            imageView.setImageResource(R.mipmap.ic_launcher);
            FirebaseAuth.getInstance().signOut();
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status)
                {
                    tvEmail.setText("Login");
                    tvName.setText(" ");
                    imageView.setImageResource(R.mipmap.ic_launcher);

                    tvEmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view)
                        {

                            Intent i=new Intent(NavigationList_Activity.this,MainActivity.class);
                            startActivity(i);

                           // Toast.makeText(NavigationList_Activity.this, "Ho gya", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void restartActivity()
    {
        Intent intent=this.getIntent();
        this.finish();
        startActivity(intent);
        this.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private void ratingMyApp()
    {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.frame,new RatingFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    private void shareApp()
    {
        Intent shareIntent=new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareText="This is a sample Handicraft App \n Click here to donload app http://www.mediafire.com/file/7cqboix1cutmym2/app-debug.apk";
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Handicraft");
        shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
        startActivity(Intent.createChooser(shareIntent,"Share Via"));
    }

    private void countTotalChildFirebase()
{
    mReference.child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            cart_count= (int) dataSnapshot.getChildrenCount();

            MenuItem menuItem = menu.findItem(R.id.cart_action);

            menuItem.setIcon(Converter.convertLayoutToImage(NavigationList_Activity.this,cart_count,R.drawable.ic_shopping_cart_white_24dp));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}
/***Inner Class**/
public class DownloadImage extends AsyncTask<String,Void,Bitmap>
{
    public DownloadImage(ImageView bmImage)
    {
        imageView= (CircleImageView) bmImage;
    }
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
//        Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment: getSupportFragmentManager().getFragments()){
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}


