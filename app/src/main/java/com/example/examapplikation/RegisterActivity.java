package com.example.examapplikation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examapplikation.Models.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    // assign to the (Widget) defined in the xml
    private EditText userName, userPassword, userEmail;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    private ImageView userProfilePic;
    String email,name,password;
    private FirebaseStorage firebaseStorage;
    private static int Pick_Image = 123;
    private StorageReference storageReference; // root
    Uri UserProfileimagePath;
    private ProgressDialog progressDialog;

    //#Region Choose profile pic during register
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // image choose

        if(requestCode == Pick_Image && resultCode == RESULT_OK && data.getData() !=null){
            UserProfileimagePath = data.getData();
            try{
                //convert to process
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),UserProfileimagePath); // convert image into bitmap
                userProfilePic.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //#EndRegion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // refrence function oncreate main functon
        viewSetUp();

        // instance of authetciator of this variable object of this main class
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);

        // storage refrence object
        storageReference = firebaseStorage.getReference();
         // store for every user id

        // when click image to set/change image
       userProfilePic.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType( "image/*"); //aplication/pdf // getting image
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select image"),Pick_Image); //  CHEKCS IF SUCSESSFULL OR NOT

           }
       });

        //#Region Register to firebase  here we also throw in the sendEmailVerfication fuction if task if sucsessful.

        regButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                 // validate if user have entered the details
            if (formValidation()){
                progressDialog.setMessage("Registering, Please wait...");
                progressDialog.show();

                // upload to authenticate firebase
                String user_email = userEmail.getText().toString().trim(); // convert to string
                String user_password = userPassword.getText().toString().trim(); // convert to string

                firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            // tell the user was sucsesfull or error thats what the addoncompleteLisener function dooes otherwise still sends
                            sendEmailVerification(); // sends to function to verification

                        } else{
                            Toast.makeText(RegisterActivity.this,"Registration Failed",Toast.LENGTH_SHORT);
                            progressDialog.dismiss();
                        }


                    }
                });
            }
            }
        });
        //#EndRegion

        userLogin.setOnClickListener(new View.OnClickListener() { // if u wanna log in and u have an account
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

    }


    //#Region Validates register form
    private boolean formValidation(){ // validates the form
        Boolean result = false;

         name = userName.getText().toString();
         password = userPassword.getText().toString();
         email = userEmail.getText().toString();

        if(name.isEmpty() || password.isEmpty() ||  email.isEmpty() || UserProfileimagePath == null){

            Toast.makeText(this,"Please fill in all the details required, remember to also choose a profile pic!",Toast.LENGTH_SHORT).show();
        }else{
            result = true; // if true
        }
        return result;

    }
    //#EndRegion

    //# user cannot log in without verifying their email. here we also send in the function SendUserProfileDataToDatabase()
    private void sendEmailVerification(){ // sending email verification when registering
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        sendUserProfileData(); // function sending to database
                        Toast.makeText(RegisterActivity.this,"Sucsessfully registered, verification mail has been sent",
                                Toast.LENGTH_SHORT).show();

                        firebaseAuth.signOut(); // so the user verifies!! first
                        finish();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    } else{ // if the server down or something
                        Toast.makeText(RegisterActivity.this,"Verification mail has not been sent",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    //#EndRegion

    // We also send data to Firebase database and firebase storage section. picture goes to storage section with uniqe id as the auth and the other info such ass paswsword and email goes to database.
    private void sendUserProfileData(){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference(firebaseAuth.getUid()); // user by id
        StorageReference picReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic"); //attaching child to main parent // creating a folder for every user with individual storage
        UploadTask uploadTask = picReference.putFile(UserProfileimagePath);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this,"Upload failed",
                        Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(RegisterActivity.this,"Upload sucsessfull",
                        Toast.LENGTH_SHORT).show();
            }
        });

    // class models. userpofile construcotr
        UserProfile userProfile = new UserProfile(email,name);
        reference.setValue(userProfile);
    }
    //#EndRegion


    //assign to the variables to the id in the xml!
    private void viewSetUp(){
        userName = (EditText) findViewById(R.id.etUserName);
        userPassword = (EditText) findViewById(R.id.etUserPassword);
        userEmail = (EditText) findViewById(R.id.etUserEmail);
        regButton = (Button) findViewById(R.id.btnRegister);
        userLogin = (TextView) findViewById(R.id.tvUserLogin);
        userProfilePic=(ImageView) findViewById(R.id.ivRegister);

    }
}
