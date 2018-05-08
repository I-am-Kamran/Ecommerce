package com.example.marvin.ecommerce.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.example.marvin.ecommerce.R;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextFirst_name;
    private TextInputEditText editTextLast_name;
    private TextInputEditText editTextPhone_number;


    private TextInputLayout editLayoutEmail;
    private TextInputLayout editLayoutPassword;
    private TextInputLayout editLayoutFirst_name;
    private TextInputLayout editLayoutLast_name;
    private TextInputLayout editLayoutPhone_number;

    private RadioButton radioButtonMale,radioButtonFemale;
    private Button buttonRegister;
    private Toolbar toolbar_Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        toolbar_Register=findViewById(R.id.toolbar_Registration);
       toolbar_Register.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
       toolbar_Register.setTitle("SignUp");
       toolbar_Register.setTitleTextColor(Color.WHITE);
       toolbar_Register.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent = new Intent(RegistrationActivity.this,LoginPageActivity.class);
               startActivity(intent);
               overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
               finish();
           }
       });
    }
}
