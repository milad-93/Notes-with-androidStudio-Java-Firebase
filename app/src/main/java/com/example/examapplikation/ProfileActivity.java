package com.example.examapplikation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examapplikation.Models.UserProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profilePic;
    private TextView profileName,profileEmail;
    private Button profileUpdate, changePassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // no rotation activated
        this.setTitle("-Profile information-");
        viewSetUp();
        // instance
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        // refrence of database we pull data from retrive data from user id
         databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        // storage refrence
         storageReference = firebaseStorage.getReference(); // root storage

      final MediaPlayer buttonSound = MediaPlayer.create(getApplicationContext(),R.raw.buttonsoumd);


        displayUserData();

        // goes to profile update page
        profileUpdate.setOnClickListener(new View.OnClickListener() { // if u wanna log in and u have an account
            @Override
            public void onClick(View v) {
                buttonSound.start();
                startActivity(new Intent(ProfileActivity.this, ProfileUpdateActivity.class));
            }
        });

        // goes to change password page
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSound.start();
                startActivity(new Intent(ProfileActivity.this, ProfilePasswordUpdateActivity.class));
            }
        });
    }
     //#region display userData
    private void displayUserData(){
        storageReference.child (firebaseAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() { // exact path to image stored
            @Override
            public void onSuccess(Uri uri) { // retrive uri to imageView
                Picasso.get().load(uri).fit().centerCrop().into(profilePic); //retrive data using picasso plugin android studio  https://square.github.io/picasso/
                // will always fit into the image view
                //put on imageView
            }
        });

        // acsess database and retrive data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Userprofile does not refer to this class of this activity but to the Constructor made in the class of Userprofile)
                // object of the class userprofile
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                profileName.setText(userProfile.getUserName());
                profileEmail.setText( userProfile.getUserEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { // if database error
                Toast.makeText(ProfileActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();

            }
        });
        //#EndRegion
    }



    //#region views
    private void viewSetUp(){
        profilePic = (ImageView) findViewById(R.id.ImageView_Profile_pic1);
        profileName = (TextView) findViewById(R.id.TextView_profile_name);
        profileEmail = (TextView) findViewById(R.id.TextView_profile_email);
        profileUpdate = (Button)  findViewById(R.id.btn_profile_update);
        changePassword = (Button) findViewById(R.id.btn_change_password);
        //#Endregion

    }

    //#Region Menu
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
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
                finish();
                break;
            }
            case R.id.HomeMenu:{
                startActivity(new Intent(ProfileActivity.this,HomeActivity.class));
                finish();
                break;

            }
        }

        return super.onOptionsItemSelected(item);
    }
    //#EndRegion

}
