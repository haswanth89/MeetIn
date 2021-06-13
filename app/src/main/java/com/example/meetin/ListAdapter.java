package com.example.meetin;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<myviewhoder> {
    ArrayList<Model> data;
    Context context;

    public ListAdapter(ArrayList<Model> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_meeting_layout, parent, false);
        return new myviewhoder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull myviewhoder holder, int position) {
        final Model temp = data.get(position);

        holder.meetingDate.setText(data.get(position).getDate());
        holder.meetingTime.setText(data.get(position).getStarttime() + " - " + data.get(position).getEndtime());
        holder.participants.setText(String.join(",",data.get(position).getParticipants()));

        holder.participants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(context, MainActivity.class);
                editIntent.putExtra("date", temp.getDate());
                editIntent.putExtra("startTime", temp.getStarttime());
                editIntent.putExtra("endTime", temp.getEndtime());
                editIntent.putExtra("Did",temp.getDid());
                editIntent.putExtra("participants",temp.getParticipants());
                editIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(editIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
