package com.example.examapplikation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilePasswordUpdateActivity extends AppCompatActivity {

    private Button update;
    private EditText newPassword;
    private FirebaseUser firebaseUser;// firebase auth for password not reguular database!!!
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_password_update);

        viewSetUp();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // current inlogged user

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPasswordNew = newPassword.getText().toString(); // get pw

                firebaseUser.updatePassword(userPasswordNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProfilePasswordUpdateActivity.this,"Password changed",Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(ProfilePasswordUpdateActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }
        });


    }
    public void viewSetUp(){

        update = findViewById(R.id.btnUpdatePassword);
        newPassword = findViewById(R.id.etNewPassword);
    }
}
