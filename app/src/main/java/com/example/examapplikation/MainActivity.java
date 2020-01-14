package com.example.examapplikation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private EditText email,password;
    private TextView forgotPasword, userRegistration;
    private FirebaseAuth firebaseAuth;
    private Button login;
    private ProgressDialog progressDialog;
    String emailFieldValidate,passwordFieldValidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // no rotation activated
        getSupportActionBar().hide(); // hide actionbar
        viewSetUp(); // check function below
        firebaseAuth = firebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this); // user click on log in first gonna check the function if it match requirement(if its in the database and can login)

        // check user already logged in the app or not in database then we direct them to second activity dont ask them to log in again
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            finish();
            startActivity(new Intent(MainActivity.this ,HomeActivity.class));
        }


        ClickEvents(); // check function below
        userLogIn(); // check function below



    }



    //#UserLogin
public void userLogIn()
{
    login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(FormValidation()){
                progressDialog.setMessage("Logging in..");
                progressDialog.show();
                String user_email = email.getText().toString().trim(); // convert to string
                String user_password = password.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity. this,"Welcome back.. ", Toast.LENGTH_LONG).show();;
                            emailVerification(); // checking if verification done
                        }else{
                            Toast.makeText(MainActivity. this,"Password does not match this account", Toast.LENGTH_LONG).show();;
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        }
    });
}

    //#EndRegion



    //#Region Views
    private void viewSetUp(){
        email = (EditText) findViewById(R.id.editText_email);
        password = (EditText) findViewById(R.id.editText_password);
        userRegistration = (TextView)findViewById(R.id.txtView_register);
        login = (Button) findViewById(R.id.btnLogin);
        forgotPasword = (TextView)findViewById(R.id.txtView_forgot_password);

    }
    //#EndRegion

    private void ClickEvents(){

        // register
        userRegistration.setOnClickListener(new View.OnClickListener() { // button on main to register
            @Override
            public void onClick(View v) { //button
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
        // forgot passsword
        forgotPasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
            }
        });

    }
    //#Region Check if Email verification when log in
    private void emailVerification(){ // verify email before logging in

        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean accountEmail = firebaseUser.isEmailVerified();

        if(accountEmail){
            finish();
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }else {

            Toast.makeText(this,"Please verify your email",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
    //#EndRegion

    //#Region Error handle on form
    private boolean FormValidation(){ // validates the form
        Boolean result = false;

        passwordFieldValidate = password.getText().toString();
        emailFieldValidate = email.getText().toString();

        if(passwordFieldValidate.isEmpty() ||  emailFieldValidate.isEmpty()){

            Toast.makeText(this,"Please fill in all the details required",Toast.LENGTH_SHORT).show();
        }else{
            result = true; // if true
        }
        return result;

    }
    //#Endregion
}

