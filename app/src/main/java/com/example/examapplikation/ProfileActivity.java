package com.example.examapplikation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePic = (ImageView) findViewById(R.id.ivProfilePic1);
        profileName = (TextView) findViewById(R.id.tvProfileName);
        profileEmail = (TextView) findViewById(R.id.TvProfileEmail);
        profileUpdate = (Button)  findViewById(R.id.btnProfileUpdate);
        changePassword = (Button) findViewById(R.id.btnChangePassword);

        // instance
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        // refrence of database we pull data from retrive data from user id
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        // storage refrence
        StorageReference storageReference = firebaseStorage.getReference(); // root storage

        storageReference.child (firebaseAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() { // exact path to image stored
            @Override
            public void onSuccess(Uri uri) { // retrive uri to imageView
           Picasso.get().load(uri).fit().centerCrop().into(profilePic); //retrive data using picasso plugin android studio  https://square.github.io/picasso/
                // will always fit into the image view

                //put on imageView
            }
        }) ;

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



        // goes to profile update page
        profileUpdate.setOnClickListener(new View.OnClickListener() { // if u wanna log in and u have an account
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ProfileUpdateActivity.class));
            }
        });

        // goes to change password page
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ProfilePasswordUpdateActivity.class));
            }
        });


    }


    // everything below is used in the menu

    private void Logout(){ // sign out method called in switchcase
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(ProfileActivity.this,MainActivity.class)); // vf krasch?
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
                startActivity(new Intent(ProfileActivity.this,HomeActivity.class));
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
