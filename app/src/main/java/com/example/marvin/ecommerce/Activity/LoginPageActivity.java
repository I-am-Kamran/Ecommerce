package com.example.marvin.ecommerce.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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

import static com.example.marvin.ecommerce.R.id.buttonFacebookLogin;
/*import static com.example.marvin.ecommerce.R.id.thing_proto;*/

public class LoginPageActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    private TextView textViewSkip;
    private TextView textViewLinkRegister;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private Button btnLogin;

    private ProgressBar progressBar;

    //For Facebook
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginManager loginManager;
    private Button btnFacebookLogin;
    private URL urlProfilePicture;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String birthday;
    private String gender;
    private String TAG = "LoginFragment";

    //For Google
    private Button loginButtonGoogle ;
    private static final String TAGG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String FILE = "myEmailPass";
    View view;

    private FirebaseAuth auth;
    private InputValidation inputValidation;

    public LoginPageActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        FacebookSdk.sdkInitialize(FacebookSdk.getApplicationContext());
        btnFacebookLogin=findViewById(buttonFacebookLogin);

        loginButtonGoogle=findViewById(R.id.buttonGoogleLogin);
        loginGoogle();

        textViewSkip=findViewById(R.id.skip);
        textViewLinkRegister=findViewById(R.id.textViewLinkRegister);

        textInputLayoutEmail=findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword=findViewById(R.id.textInputLayoutPassword);
        textInputEditTextEmail=findViewById(R.id.textInputEmail);
        textInputEditTextPassword=findViewById(R.id.textInputPassword);
        btnLogin=findViewById(R.id.loginButton);
        progressBar=findViewById(R.id.progress_bar);


        initListeners();
        initObjects();
    }

    private void initListeners()
    {
        setUpFacebook();
        btnFacebookLogin.setOnClickListener(this);
        loginButtonGoogle.setOnClickListener(this);
        textViewSkip.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }


    private void initObjects()
    {
        sharedPreferences=this.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        inputValidation=new InputValidation(this);
        //get firebase auth instance
        auth= FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.buttonFacebookLogin:
                loginWithFacebook();
                break;

            case R.id.buttonGoogleLogin:
                loginWithGoogle();
                break;

            case R.id.skip:

                Intent i = new Intent(LoginPageActivity.this, NavigationList_Activity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
                break;

            case R.id.textViewLinkRegister:

                Intent intent = new Intent(LoginPageActivity.this,RegistrationActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
                break;

            case R.id.loginButton:
                emailAndPasswordSignIn();
        }

    }

    private void emailAndPasswordSignIn() {

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
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);

                if (!task.isSuccessful()){
                    if (password.length()<10){
                        textInputLayoutPassword.setError("Please enter 10 digit password");
                    } else{
                        Toast.makeText(LoginPageActivity.this, "Auth Failed", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    editor.putString("email",email);
                    editor.putString("password",password);
                    editor.commit();

                    restartActivity();

                    Intent intent=new Intent(LoginPageActivity.this,NavigationList_Activity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    finish();


                }
            }
        });
    }

    private void loginGoogle()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void loginWithGoogle()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        Toast.makeText(this, "Sign in", Toast.LENGTH_SHORT).show();
    }



    private void loginWithFacebook() {

        accessTokenTracker.startTracking();
        loginManager.logInWithReadPermissions(this,
                Arrays.asList("public_profile","email","user_birthday"));
    }

    private void setUpFacebook()
    {    loginManager=LoginManager.getInstance();
        callbackManager=CallbackManager.Factory.create();
        accessTokenTracker=new AccessTokenTracker() {
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
                            requestObjectUser(loginResult.getAccessToken());
                        } else {
                            LoginManager.getInstance().logOut();
                            Toast.makeText(LoginPageActivity.this, "Error permissions", Toast.LENGTH_SHORT).show();
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

                            Toast.makeText(LoginPageActivity.this, "Method called "+firstName+lastName, Toast.LENGTH_LONG).show();
                            editor.putString("facebook_first_name",firstName);
                            editor.putString("facebook_last_name",lastName);
                            editor.putString("facebook_email",email);
                            editor.putString("facebook_image_url",urlProfilePicture.toString());
                            editor.commit();

                            restartActivity();

                            Intent intent=new Intent(LoginPageActivity.this, NavigationList_Activity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                            finish();

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

    private void restartActivity()
    {
        Intent intent = this.getIntent();
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode,resultCode,data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    /**
     **google method
     **/
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handle_sign_in_result:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount account = result.getSignInAccount();
            String gName = account.getDisplayName();
            String gEmail = account.getEmail();
            String gPhotoUrl = account.getPhotoUrl().toString();


            Intent intent = new Intent(this, NavigationList_Activity.class);
            intent.putExtra("gName", gName);
            intent.putExtra("gEmail", gEmail);
            intent.putExtra("gPhotoUrl", gPhotoUrl);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            finish();
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
