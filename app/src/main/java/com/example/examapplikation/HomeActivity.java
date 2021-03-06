package com.example.examapplikation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


import com.example.examapplikation.Models.NotesList;
import com.example.examapplikation.ViewHolder.NoteViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class HomeActivity extends AppCompatActivity {


    private RecyclerView recyclerView; //https://camposha.info/android-firebase-realtime-database-with-recyclerview/
    private FloatingActionButton addNotePageButton;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference notesDb;
    FirebaseRecyclerOptions<NotesList> options;
    FirebaseRecyclerAdapter<NotesList, NoteViewHolder> adapter; // adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
            this.setTitle("Home - View notes");
            final MediaPlayer buttonSound = MediaPlayer.create(getApplicationContext(),R.raw.buttonsoumd);
            //iniate
            viewSetUp();
            // refrense
            notesDb = database.getReference("NoteList").child(firebaseAuth.getCurrentUser().getUid());
            // set view
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager( new LinearLayoutManager(this) );
            showEachRow(); // call function


            // buton to add note
            addNotePageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   buttonSound.start();
                    Intent intent = new Intent(HomeActivity.this, NoteInputActivity.class);
                    startActivity(intent);
                }
        });
    }


    //Region -used for edit and delete this section start reads whats in the post and open ups a window on the same window based on the  Viewholder and a xml file called note_options
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) { // viewholder

       if (item.getTitle().equals("Edit Note")){
           clickUpdateMenu(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
       } else if (item.getTitle().equals("Delete Note")){
           deleteNote(adapter.getRef(item.getOrder()).getKey());
       }

        return super.onContextItemSelected(item);
    }

    private void deleteNote(String adapter) { // delete
        MediaPlayer deleteSound = MediaPlayer.create(getApplicationContext(),R.raw.delete);// delete sound on note
        deleteSound.start();
        notesDb.child(adapter).removeValue();
    }

    private void clickUpdateMenu(final String key, NotesList item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("[ Note Edit Mode]");


        View update_layout = LayoutInflater.from(this).inflate(R.layout.note_options,null); // view model layou

        final EditText changed_title = update_layout.findViewById(R.id.edit_update_title);
        final EditText changed_content = update_layout.findViewById(R.id.edit_update_text);
        final EditText changed_time = update_layout.findViewById(R.id.edit_time);

        changed_title.setText(item.getTitle()); // get value to new
        changed_content.setText(item.getText());
        builder.setView(update_layout);
        builder.setPositiveButton("Save changes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String title = changed_title.getText().toString();
                String content = changed_content.getText().toString();
                String time  =  changed_time.getText().toString();

                NotesList notesList = new NotesList(title,content,time);
                notesDb.child(key).setValue(notesList);

                Toast.makeText(HomeActivity.this,"Note Updated", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
            builder.show();
    }
    //# EndRegion


    // Region to read data from dataBase using  Viewholder and model Notes and  a xml file called note_each_row + the home xml file.
    private void showEachRow(){ // recycler view
       options = new FirebaseRecyclerOptions.Builder<NotesList>()
               .setQuery(notesDb,NotesList.class)
               .build();

       adapter = new FirebaseRecyclerAdapter<NotesList, NoteViewHolder>(options) { // noteViewModel to model to get
           @Override
           protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull NotesList model) {
               holder.text_title.setText(model.getTitle());
               holder.text_content.setText(model.getText());
           }

           @NonNull
           @Override
           public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

               View itemView  = LayoutInflater.from(viewGroup.getContext())
                       .inflate(R.layout.notes_each_row,viewGroup,false); // inflate the rows

               return new NoteViewHolder(itemView);
           }
       };

       recyclerView.setAdapter(adapter);

        }
    //# end Region

//#start region initate to xml
    private void viewSetUp(){
        recyclerView = findViewById(R.id.recyclerView);// activity_home.xml
        addNotePageButton = findViewById(R.id.fab_button_addPage);
        firebaseAuth = FirebaseAuth.getInstance(); // get inSTACE
        database = FirebaseDatabase.getInstance();

    }
    // end region

    //# Menu
    private void Logout(){ // sign out method called in switchcase
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(HomeActivity.this,MainActivity.class));
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
            case R.id.ProfileMenu:{
                startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
                finish();
                break;
            }

            case R.id.HomeMenu:{
                startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                finish();
                break;

            }
        }

        return super.onOptionsItemSelected(item);
    }
}
 // Endregion