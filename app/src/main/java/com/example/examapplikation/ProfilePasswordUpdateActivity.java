package com.example.examapplikation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilePasswordUpdateActivity extends AppCompatActivity {

    private Button updatePasswprd;
    private EditText newPassword, newPassword2;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    String userPasswordNew, userPassword2New;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_password_update);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // no rotation activated
        this.setTitle("-Change Password-");

        viewSetUp();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // current inlogged user

        updatePasswprd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });


    }


    public void changePassword(){
        userPasswordNew = newPassword.getText().toString(); // get pw
        userPassword2New = newPassword2.getText().toString();

        if (userPasswordNew.equals(userPassword2New)){

            firebaseUser.updatePassword(userPasswordNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ProfilePasswordUpdateActivity.this,"Password changed",Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(ProfilePasswordUpdateActivity.this,"Something went wrong, password did not get changed",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{

            Toast.makeText(ProfilePasswordUpdateActivity.this,"Both fields much match in order to change password",Toast.LENGTH_LONG).show();
        }

    }

    public void viewSetUp(){

        updatePasswprd = findViewById(R.id.btnUpdate_Password);
        newPassword = findViewById(R.id.EditText_new_password);
        newPassword2 = findViewById(R.id.EditText_new_password2);

    }


    //#Region Menu
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(ProfilePasswordUpdateActivity.this,MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //create menu on toolbar
        getMenuInflater().inflate(R.menu.menu,menu); //inflated inside
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { // handle on click events on items on menu
        switch(item.getItemId()){
            case R.id.logoutMenu:{
                Logout();
            }
            case R.id.HomeMenu:{
                startActivity(new Intent(ProfilePasswordUpdateActivity.this,HomeActivity.class));
            }
        }

        return super.onOptionsItemSelected(item);
    }
    //#EndRegion
}
