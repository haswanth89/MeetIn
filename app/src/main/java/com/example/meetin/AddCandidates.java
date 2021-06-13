package com.example.meetin;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddCandidates extends RecyclerView.ViewHolder{
    CheckBox candidate;
    public AddCandidates(@NonNull View itemView) {
        super(itemView);
        candidate = itemView.findViewById(R.id.candidates);
    }
}