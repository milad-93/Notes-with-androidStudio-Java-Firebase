package com.example.examapplikation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private int failedAttempts = 3;
    private TextView userRegistration;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgotPasword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); // hide actionbar
        // assign to the (Widget) id defined in the xml
        viewSetUp();

        Info.setText("Number of attemepts remaining: 3");

        firebaseAuth = firebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this); // user click on log in first gonna check the function if it match requirement(if its in the database and can login)

        // check user already logged in the app or not in database then we direct them to second activity dont ask them to log in again
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            finish();
            startActivity(new Intent(MainActivity.this ,HomeActivity.class));
        }

        //# Region on click listeners

        Login.setOnClickListener(new View.OnClickListener() { //validate
            @Override
            public void onClick(View v) { //button

                validate (Name.getText().toString(), Password.getText().toString());
            }
        });


        userRegistration.setOnClickListener(new View.OnClickListener() { // button on main to register
            @Override
            public void onClick(View v) { //button
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        forgotPasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
            }
        });
    }
    //#EndRegion


    //#Region LoggIn
    private void validate (String userName, String userPassword){
        progressDialog.setMessage("Logging in..");
        progressDialog.show();

   // addOncomplete listener checks if the task is sucsessful or not to the database
        firebaseAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                  //  Toast.makeText(MainActivity. this,"Login succsesful ", Toast.LENGTH_SHORT).show();;
                    emailVerification(); // checking if verification done
                }else{
                    Toast.makeText(MainActivity. this,"Login failed", Toast.LENGTH_SHORT).show();;
                    failedAttempts --; // decrease failed attemepts from 3-2-1 lock
                    Info.setText("Wrong password: "+failedAttempts +" Attempts remaining");
                  progressDialog.dismiss(); // so it doesnt run when its failed aswell!

                    if (failedAttempts== 0){
                        Login.setEnabled(false); // reached 0 disables button
                    }
                }
            }
        });
    }
    //#EndRegion

    //#Region Check if Email Verifed when logging in
    private void emailVerification(){ // if not VERIFED EMAIL!!!!!

        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean email = firebaseUser.isEmailVerified();

        if(email){
        finish();
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }else {

            Toast.makeText(this,"Please verify your email",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
    //#EndRegion


    //#Region Views
    private void viewSetUp(){
        Name = (EditText) findViewById(R.id.etName);
        Password = (EditText) findViewById(R.id.etPassword);
        Info = (TextView) findViewById(R.id.tvInfo);
        Login = (Button) findViewById(R.id.btnLogin);
        userRegistration = (TextView)findViewById(R.id.tvRegister);
        forgotPasword = (TextView)findViewById(R.id.tvForgotPassword);

    }
    //#EndRegion
}
