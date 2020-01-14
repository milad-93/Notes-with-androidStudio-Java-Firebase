package com.example.examapplikation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText pwEmail;
    private Button resetPw;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        viewSetUp();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resetPassword();




    }
   //#Region resetPassword
    private void resetPassword(){
        resetPw.setOnClickListener(new View.OnClickListener() { // button press
            @Override
            public void onClick(View v) {
                String emailUserInput = pwEmail.getText().toString().trim(); // convert object textbox to string then trim

                if(emailUserInput.equals("")){
                    Toast.makeText(ForgotPasswordActivity.this,"Enter your registered email", Toast.LENGTH_SHORT).show();
                } else{

                    firebaseAuth.sendPasswordResetEmail(emailUserInput).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPasswordActivity.this, "Reset Email sucsessfully sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                            }else{
                                Toast.makeText(ForgotPasswordActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    } //#Endregion

    //#Region views

    private void viewSetUp(){
        pwEmail= (EditText)findViewById(R.id.EditText_password_email); //assign
        resetPw= (Button) findViewById(R.id.btn_reset_password);
        firebaseAuth= FirebaseAuth.getInstance(); // instace of main class

    }

    //#Endreigon
}
