package com.example.meetin;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class participantAdapter extends RecyclerView.Adapter<AddCandidates> {

    static ArrayList<String> candidatenam;
    static ArrayList<String> selected = new ArrayList<>();
    Context context;

    public participantAdapter(@NonNull ArrayList<String> candidate, Context context) {
        this.candidatenam = candidate;
        this.context = context;
    }
    @NonNull
    @Override
    public AddCandidates onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        selected.clear();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.participants, parent, false);
        return new AddCandidates(view);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull AddCandidates holder, int position) {
        holder.candidate.setText(candidatenam.get(position));
        holder.candidate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true)
                {
                    selected.add(candidatenam.get(position));
                }
                else
                {
                    selected.remove(candidatenam.get(position));
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return candidatenam.size();
    }
    public static ArrayList<String> getSelected(){return selected;}
}
