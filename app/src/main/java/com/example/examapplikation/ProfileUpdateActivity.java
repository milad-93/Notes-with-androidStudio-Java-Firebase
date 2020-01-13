package com.example.examapplikation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.examapplikation.Models.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ProfileUpdateActivity extends AppCompatActivity {

    private EditText newUserName, newUserEmail;
    private Button updatedProfileSave;
    private ImageView ChangeProfilePic;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;



    private static int choose_profile_image = 123;
    private StorageReference storageReference; // root
    Uri UserProfileimagePath;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // image choose

        if(requestCode == choose_profile_image && resultCode == RESULT_OK && data.getData() !=null){
            UserProfileimagePath = data.getData();
            try{
                //convert to process
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),UserProfileimagePath); // convert image into bitmap
                ChangeProfilePic.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // no rotation activated
        // initiate
        viewSetUp();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // instance
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        // refrence of database we pull data from retrive data from user id
        final DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        // acsess database and retrive data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //models.userprofile
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                newUserName.setText(userProfile.getUserName());
                newUserEmail.setText(userProfile.getUserEmail());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { // if database error
                Toast.makeText(ProfileUpdateActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();

            }
        });

       final  StorageReference storageReference = firebaseStorage.getReference(); // root storage // change image

        storageReference.child (firebaseAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() { // exact path to image stored
            @Override
            public void onSuccess(Uri uri) { // retrive uri to imageView
                Picasso.get().load(uri).fit().centerCrop().into(ChangeProfilePic); //retrive data using picasso plugin android studio  https://square.github.io/picasso/
                // will always fit into the image view

                //put on imageView
            }
        }) ;

        updatedProfileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // retrive
                String name = newUserName.getText().toString();
                String email = newUserEmail.getText().toString();
                // create userProfile object
                UserProfile userProfile = new UserProfile(email,name); // om värden kstas om kolla detta kanske error

                databaseReference.setValue(userProfile); // updating in inque user id

            // storage upload to edit pic
                StorageReference picReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic"); //attaching child to main parent // creating a folder for every user with individual storage
                UploadTask uploadTask = picReference.putFile(UserProfileimagePath);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileUpdateActivity.this,"Upload failed",
                                Toast.LENGTH_SHORT).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ProfileUpdateActivity.this,"Upload sucsessfull",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                finish(); // back to last activity profile activity

            }
        });

        ChangeProfilePic.setOnClickListener(new View.OnClickListener() {// makes the image clickable to change the image.(choose the aavailable image)

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType( "image/*"); //aplication/pdf // getting image
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select image"),choose_profile_image); //  CHEKCS IF SUCSESSFULL OR NOT
            }
        });

    }

    public void viewSetUp(){
        newUserName = findViewById(R.id.EditText_name_update);
        newUserEmail= findViewById(R.id.EditText_email_update);
        ChangeProfilePic = findViewById(R.id.ImageView_change_picture);
        updatedProfileSave = findViewById(R.id.btn_profile_update_changes);

    }



    //#Region Menu
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(ProfileUpdateActivity.this,MainActivity.class));
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
                startActivity(new Intent(ProfileUpdateActivity.this,HomeActivity.class));
                finish();
                break;

            }
        }

        return super.onOptionsItemSelected(item);
    }
    //#EndRegion


}
