package com.example.meetin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static android.content.ContentValues.TAG;
import static javax.mail.internet.InternetAddress.parse;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText time1, time2, date, participant1, participant2;
    int hour1, minute1, hour2, minute2;
    Button create, upcoming;
    String Did, participants;
    RecyclerView canditatesview;
    participantAdapter adapter;
    ArrayList<String> globalcand;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler h = new Handler();
        globalcand = new ArrayList<>();
        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getString("Name"));
                        globalcand.add(document.getString("Name").toString());
                    }
                } else {
                    Log.w(TAG, "E..rror getting documents.", task.getException());
                }
            }
        });
        time1 = findViewById(R.id.timeButton1);
        time2 = findViewById(R.id.timeButton2);
        date = findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        canditatesview = findViewById(R.id.participants_recycler);
        canditatesview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new participantAdapter(globalcand, getApplicationContext());
        canditatesview.setAdapter(adapter);
        create = findViewById(R.id.create);
        upcoming = findViewById(R.id.upcoming);
        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection("Interviews")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    ArrayList<Model> meetings = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getString("date"));
                                        Model attach = new Model();
                                        attach.setDate(document.getString("date"));
                                        attach.setParticipants((List<String>) document.get("participants"));
                                        attach.setStarttime(document.getString("StartTime"));
                                        attach.setEndtime(document.getString("EndTime"));
                                        attach.setDid(document.getId().toString());
                                        meetings.add(attach);
                                    }
                                    ListOfMeetings.addArrayList(meetings);
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });
                Intent upcomingIntent = new Intent(getApplicationContext(), ListOfMeetings.class);
                startActivity(upcomingIntent);
            }
        });

        date.setText(getIntent().getStringExtra("date"));
        time1.setText(getIntent().getStringExtra("startTime"));
        time2.setText(getIntent().getStringExtra("endTime"));
        Did = getIntent().getStringExtra("Did");
    }

    public void popTimePicker1(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener1 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour1, int selectedMinute1) {
                hour1 = selectedHour1;
                minute1 = selectedMinute1;
                time1.setText(String.format(Locale.getDefault(), "%02d:%02d", hour1, minute1));
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener1, hour1, minute1, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void popTimePicker2(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour2, int selectedMinute2) {
                hour2 = selectedHour2;
                minute2 = selectedMinute2;
                time2.setText(String.format(Locale.getDefault(), "%02d:%02d", hour2, minute2));
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener2, hour2, minute2, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        date.setText(currentDateString);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addToDatabase(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        ArrayList<String> participa = new ArrayList<>();
        participa = participantAdapter.getSelected();
        if (participa.size() < 2) {
            Toast.makeText(this, "Participants are less", Toast.LENGTH_LONG).show();
        } else {
            if (Did != null) {
                db.collection("Interviews").document(Did).delete();
            }
            if (date.getText().toString().length() < 1 || time1.getText().toString().length() < 1 || time2.getText().toString().length() < 1
                    || time1.getText().toString().compareTo(time2.getText().toString()) >= 0
            ) {
                Toast.makeText(this, "Errors occured", Toast.LENGTH_SHORT).show();
            } else {


                user.put("participants", participa);
                user.put("date", date.getText().toString());
                user.put("StartTime", time1.getText().toString());
                user.put("EndTime", time2.getText().toString());

                boolean[] flag = {false};
                String s = time1.getText().toString();
                String p = time2.getText().toString();
                String da = date.getText().toString();
                db.collection("Interviews")
                        .whereArrayContainsAny("participants", participa)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " @@ da" + da + document.getData());
                                        String date = document.getString("date");
                                        String a = document.getString("StartTime");
                                        String b = document.getString("EndTime");
                                        if ( (
                                                (s.compareTo(a) >= 0 && s.compareTo(b) < 0)
                                                        || (p.compareTo(a) >= 0 && (p.compareTo(b) <= 0)))
                                                &&  da.compareTo(date)==0

                                        ) {
                                            flag[0] = true;
                                            break;
                                        }
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents%%: ", task.getException());
                                }
                            }
                        });
                Handler h = new Handler();
                ArrayList<String> finalParticipa = participa;
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (flag[0] == true)
                        {
                            Toast.makeText(MainActivity.this, "Participants are on other interview", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String[] mail = {""};
                            db.collection("Users")
                                    .whereIn("Name", finalParticipa)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d(TAG, document.getId() + " @@@ da" + da + document.getData());
                                                    mail[0] += document.getString("Mail")+",";
                                                }
                                                mail[0]=mail[0].substring(0,mail[0].length()-1);
                                                Log.d(TAG,mail[0]+"@@@@");
                                            } else {
                                                Log.d(TAG, "Error getting documents%%: ", task.getException());
                                            }
                                        }
                                    });
                                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                            StrictMode.setThreadPolicy(policy);
                                            Handler hi = new Handler();
                                            hi.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    final String usern = "haswanth89@gmail.com";
                                                    final String passw = "9705447749";
                                                    Properties props = new Properties();
                                                    props.put("mail.smtp.auth", "true");
                                                    props.put("mail.smtp.starttls.enable", "true");
                                                    props.put("mail.smtp.host", "smtp.gmail.com");
                                                    props.put("mail.smtp.port", "587");
                                                    Session session = Session.getInstance(props,
                                                            new javax.mail.Authenticator() {
                                                                @Override
                                                                protected PasswordAuthentication getPasswordAuthentication() {
                                                                    return new PasswordAuthentication(usern, passw);
                                                                }
                                                            });


                                                    try {
                                                        javax.mail.Message message = new MimeMessage(session);
                                                        message.setFrom(new InternetAddress(usern));
                                                        Log.d(TAG,mail[0]+"@@@@");
                                                        message.setRecipients(Message.RecipientType.TO, parse(mail[0]));
                                                        message.setSubject("Interview Scheduled");
                                                        message.setText("Your Interview has been scheduled \nDate : " + da + "\nTimings : " + s + " to " + p);
                                                        Transport.send(message);
                                                    } catch (AddressException e) {
                                                        e.printStackTrace();
                                                    } catch (MessagingException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            },3000);

                                            db.collection("Interviews").
                                                    add(user)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, "Error adding document", e);
                                                        }
                                                    });
                                            Toast.makeText(MainActivity.this, "Added to Database", Toast.LENGTH_LONG).show();

                                        }
                                    }

                        }, 5000);
                date.setText("");
                time1.setText("");
                time2.setText("");
            }
        }}}