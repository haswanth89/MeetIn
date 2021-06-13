package com.example.meetin;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class myviewhoder extends RecyclerView.ViewHolder{
    TextView participants,meetingDate,meetingTime;

    public myviewhoder(@NonNull View itemView) {
        super(itemView);
        participants=itemView.findViewById(R.id.participants);
        meetingDate = itemView.findViewById(R.id.meeting_date);
        meetingTime = itemView.findViewById(R.id.meeting_time);
    }
}
