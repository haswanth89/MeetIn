package com.example.meetin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ListOfMeetings extends AppCompatActivity {

    RecyclerView recyclerView;
    ListAdapter adapter;
    static ArrayList<Model> meetings1=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_meetings);

        recyclerView=findViewById(R.id.meetings_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ListAdapter(meetings1,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
    public static void addArrayList(ArrayList<Model> meetings)
    {
        meetings1 = meetings;
    }
}