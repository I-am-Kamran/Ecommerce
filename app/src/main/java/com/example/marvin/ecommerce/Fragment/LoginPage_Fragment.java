package com.example.marvin.ecommerce.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marvin.ecommerce.Activity.MainActivity;
import com.example.marvin.ecommerce.Activity.NavigationList_Activity;
import com.example.marvin.ecommerce.HelperClass.InputValidation;
import com.example.marvin.ecommerce.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginPage_Fragment extends Fragment implements View.OnClickListener {

    private static final String FILE = "filepath";
    private TextView textViewSkip;
    public Button btnLogin;
    private TextView textViewLinkRegister;
    ProgressBar progressBar;
    private FirebaseAuth auth;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;



    //for Google
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions;
    private Button loginButtonGoogle;

    private static final int RC = 0;


    //for facebook
    private Button loginButtonFacebook;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    public ProfileTracker profileTracker;
    private LoginManager loginManager;
    private URL urlProfilePicture;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String birthday;
    private String gender;
    private String TAG = "LoginFragment";

    private InputValidation inputValidation;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    View view;

    public LoginPage_Fragment(){
        //Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_page_, container, false);
      //  initViews();
        textViewSkip = view.findViewById(R.id.skip);

        textInputLayoutEmail=view.findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword=view.findViewById(R.id.textInputLayoutPassword);

        textInputEditTextEmail=view.findViewById(R.id.textInputEmail);
        textInputEditTextPassword=view.findViewById(R.id.textInputPassword);

        loginButtonFacebook = view.findViewById(R.id.buttonFacebookLogin);
        loginButtonGoogle=view.findViewById(R.id.buttonGoogleLogin);
        textViewLinkRegister=view.findViewById(R.id.textViewLinkRegister);

        btnLogin=view.findViewById(R.id.loginButton);

        progressBar=view.findViewById(R.id.progress_bar);

        initListeners();
        initObjects();

       /* GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();*/

        //facebook Code
   /*     FacebookSdk.sdkInitialize(getApplicationContext());
        loginManager=LoginManager.getInstance();
        callbackManager=CallbackManager.Factory.create();
        accessTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
            {

            }
        };
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                if (loginResult.getRecentlyGrantedPermissions().contains("email"))
                {
                    requestObjectUser(loginResult.getAccessToken());
                    Toast.makeText(getActivity(), "OnSuccess  called", Toast.LENGTH_SHORT).show();
                }

                else
                    {
                    LoginManager.getInstance().logOut();
                    Toast.makeText(getActivity(), "Error Permission", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        if (AccessToken.getCurrentAccessToken()!=null)
        {
            requestObjectUser(AccessToken.getCurrentAccessToken());
        }*/

        return view;
    }
    /**This method is used to Initialize the Views
 ***/
    private void initViews()
    {

    }


    /**
     * this method is to initialize listeners
     */
    private void initListeners()
    {
        btnLogin.setOnClickListener(this);
        textViewSkip.setOnClickListener(this);
        loginButtonGoogle.setOnClickListener(this);
        loginButtonFacebook.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    /**
     * this method to initialize the Objects
     */
    private void initObjects()
    {
        inputValidation = new InputValidation(getActivity());

        sharedPreferences = this.getActivity().getSharedPreferences(FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //get firebase auth instance
        auth=FirebaseAuth.getInstance();
      //  checkUser();

    }


    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        switch (id) {

            case R.id.buttonGoogleLogin:
                loginWithGoogle();
                break;

            case R.id.skip:

                Intent i = new Intent(getActivity(), NavigationList_Activity.class);
                startActivity(i);
                getActivity().finish();
                break;

            case R.id.buttonFacebookLogin:
                loginWithFacebook();
                break;

            case R.id.loginButton:
                EmailAndPasswordSignIn();
                break;

            case R.id.textViewLinkRegister:


        }

    }

    private void EmailAndPasswordSignIn()
    {
        if (!inputValidation
                .isInputEditTextField(textInputEditTextEmail,textInputLayoutEmail,"Enter Email"))
        {
            return;
        }

        if (!inputValidation.isInputEditTextField(textInputEditTextPassword,textInputLayoutPassword,"Enter Password"))
        {
            return;
        }

        final String email,password;

        email=textInputEditTextEmail.getText().toString().trim();
        password=textInputEditTextPassword.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        //authenticate the user
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);

                if (!task.isSuccessful()){
                    if (password.length()<10){
                        textInputLayoutPassword.setError("Please enter 10 digit password");
                    } else{
                        Toast.makeText(getActivity(), "Auth Failed", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    editor.putString("email",email);
                    editor.putString("password",password);
                    editor.commit();

                    restartActivity();

                    Intent intent=new Intent(getActivity(),NavigationList_Activity.class);
                    startActivity(intent);
                    getActivity().finish();


                }
            }
        });
    }

    /**Restart the Activity*/
    private void restartActivity() {
        Intent intent = getActivity().getIntent();
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        getActivity().finish();
    }

    /*  method for Login Facebook*/
    private void loginWithFacebook() {
        accessTokenTracker.startTracking();
        loginManager.logInWithReadPermissions(getActivity(),
                Arrays.asList("public_profile", "email", "user_birthday"));
    }

    /**method for setup facebook**/
    private void setUpFacebook() {
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {

            }
        };

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if (loginResult.getRecentlyGrantedPermissions().contains("email")) {
                            Toast.makeText(getActivity(), ""+loginResult, Toast.LENGTH_SHORT).show();
                            requestObjectUser(loginResult.getAccessToken());
                        } else {
                            LoginManager.getInstance().logOut();
                            Toast.makeText(getActivity(), "Error permissions", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("ERROR", error.toString());
                    }
                });

        if (AccessToken.getCurrentAccessToken() != null) {
            requestObjectUser(AccessToken.getCurrentAccessToken());
        }

    }
    private void requestObjectUser(final AccessToken currentAccessToken)
    {
        GraphRequest request=GraphRequest.newMeRequest(currentAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d(TAG, object.toString());
                        Log.d(TAG, response.toString());
                        try
                        {
                            userId=object.getString("id");
                            urlProfilePicture=new URL("https://graph.facebook.com/" + userId + "/picture?width=96&height=96");
                            if (object.has("first_name"))
                                firstName=object.getString("first_name");
                            if(object.has("last_name"))
                                lastName = object.getString("last_name");
                            if (object.has("email"))
                                email = object.getString("email");
                            if (object.has("birthday"))
                                birthday = object.getString("birthday");
                            if (object.has("gender"))
                                gender = object.getString("gender");

//
                            Toast.makeText(getActivity(), "Method called "+firstName+lastName, Toast.LENGTH_LONG).show();
                            editor.putString("facebook_first_name",firstName);
                            editor.putString("facebook_last_name",lastName);
                            editor.putString("facebook_image_url",urlProfilePicture.toString());
                            editor.commit();



                            //restart activity
                            restartActivity();

                            Intent intent=new Intent(getActivity(), NavigationList_Activity.class);
                            startActivity(intent);
                            getActivity().finish();
                           /* FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.mainActivity, new HomeFragment());
                            ft.commit();
*/
                        }catch (JSONException e){
                            e.printStackTrace();
                        }catch (MalformedURLException e){
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters=new Bundle();
        parameters.putString("fields","id, first_name, last_name, email, birthday, gender , location");
        request.setParameters(parameters);
        request.executeAsync();
    }




  /*  public void displayMessage(Profile currentProfile)
    {
        if(currentProfile!=null)
        {

            String fName = currentProfile.getName();
            String fPhotoUrl = currentProfile.getProfilePictureUri(150, 150).toString();

            Log.e("Name",""+fName);

            Intent intent=new Intent(getActivity(),NavigationList_Activity.class);
            intent.putExtra("fName",fName);
            intent.putExtra("fPhotoUrl", fPhotoUrl);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//it finshes the first activity

            startActivity(intent);

        }
    }*/





    // Google signIn Button
  /*  private void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC);
        btnSignIn.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Sign in", Toast.LENGTH_SHORT).show();
    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
           // handleSignInResult(result);
            GoogleSignInAccount account = result.getSignInAccount();

            try {
                String username_google, email_google, profile_pic_google="";
                username_google=account.getDisplayName();
                email_google=account.getEmail();
                profile_pic_google=account.getPhotoUrl().toString();


                editor.putString("username_google",username_google);
                editor.putString("email_google",email_google);
                editor.putString("profile_pic_google",profile_pic_google);
                editor.commit();

                restartActivity();

                Intent intent=new Intent(getActivity(),NavigationList_Activity.class);
                startActivity(intent);
                getActivity().finish();

        }
        catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    else {
            Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }

    /**method for login in google**/
    private void loginWithGoogle()
    {
        googleSignInOptions=new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .requestProfile().build();

        googleApiClient=new GoogleApiClient.Builder(getActivity()).addApi(Auth
        .GOOGLE_SIGN_IN_API,googleSignInOptions).build();

        Intent googleSignIn=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(googleSignIn,RC);
    }


 /*   @Override
    public void onStop() {
        super.onStop();
//        accessTokenTracker.stopTracking();
//        profileTracker.stopTracking();
//
      *//*  if (mAuthListener!=null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        *//*
    }

    @Override
    public void onResume() {
        super.onResume();
        //Profile profile=Profile.getCurrentProfile();

    }

   *//* private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handle_sign_in_result:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount account = result.getSignInAccount();
            String gName = account.getDisplayName();
            String gEmail = account.getEmail();
            String gPhotoUrl = account.getPhotoUrl().toString();


            Intent intent = new Intent(getActivity(), NavigationList_Activity.class);
            intent.putExtra("gName", gName);
            intent.putExtra("gEmail", gEmail);
            intent.putExtra("gPhotoUrl", gPhotoUrl);
            startActivity(intent);
            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
        }
    }*//*

   *//* @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed" + connectionResult);
    }*/


}
