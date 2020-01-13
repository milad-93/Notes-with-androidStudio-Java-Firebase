package com.example.examapplikation.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;
import com.example.examapplikation.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public  class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    public TextView text_title, text_content;

    public NoteViewHolder(@NonNull View itemView){

        super (itemView);
        text_title = itemView.findViewById(R.id.text_title);
        text_content = itemView.findViewById(R.id.text_content);
        itemView.setOnCreateContextMenuListener(this); // context to this view

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) { // on clik update delete on every object
    menu.add(0,0,getAdapterPosition(),"Edit Note");
    menu.add(0,0,getAdapterPosition(),"Delete Note");

    }
}
