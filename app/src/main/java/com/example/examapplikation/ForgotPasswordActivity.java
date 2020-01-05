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

        
        resetPw.setOnClickListener(new View.OnClickListener() { // button press
            @Override
            public void onClick(View v) {
                String userEmail = pwEmail.getText().toString().trim(); // convert object textbox to string then trim

                if(userEmail.equals("")){
                    Toast.makeText(ForgotPasswordActivity.this,"Enter your registered email", Toast.LENGTH_SHORT).show();
                } else{

                    firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPasswordActivity.this, "Reset Email sucsessfully sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                            }else{
                                Toast.makeText(ForgotPasswordActivity.this, "Error in sending email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });

    }

    private void viewSetUp(){
        pwEmail= (EditText)findViewById(R.id.etPasswordEmail); //assign
        resetPw= (Button) findViewById(R.id.btnPasswordReset);
        firebaseAuth= FirebaseAuth.getInstance(); // instace of main class

    }
}
