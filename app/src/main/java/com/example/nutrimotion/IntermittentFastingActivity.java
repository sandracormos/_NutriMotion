package com.example.nutrimotion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IntermittentFastingActivity extends AppCompatActivity {


    private DatabaseReference databaseReference;
    NotificationManagerCompat notificationManagerCompat;
    Notification notification;
    NotificationManagerCompat notificationManagerCompat2;
    Notification notification2;

    private ProgressBar progressBar;
    private Button button12_12;
    private Button button14_10;
    private Button button16_8;
    private Button button18_6;

    private long startTimeMillis = 0; // Initialize the start time
    private long endTimeMillis = 0;   // Initialize the end time

    private boolean button12_12Clicked = false; //track button12_12 click
    private boolean button14_10Clicked = false;
    private boolean button16_8Clicked = false;
    private boolean button18_6Clicked = false;


    private PendingIntent fastingEndIntent;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermittent_fasting);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Initialize the Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize the AlarmManager and PendingIntent for fasting end
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        fastingEndIntent = createFastingEndIntent();

        progressBar = findViewById(R.id.IF_progress);
        button12_12 = findViewById(R.id.button_12_12);
        button14_10 = findViewById(R.id.button_14_10);
        button16_8 = findViewById(R.id.button_16_);
        button18_6 = findViewById(R.id.button_18_6);

        button12_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressAndColor(50, R.color.progressBarColor);
                enableStartAndEndButtons(); // Call this function to make the buttons visible and clickable
                button12_12Clicked = true;
            }
        });

        button14_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressAndColor(58, R.color.progressBarColor);
                enableStartAndEndButtons();
                button14_10Clicked = true;
            }
        });

        button16_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressAndColor(66, R.color.progressBarColor);
                enableStartAndEndButtons();
                button16_8Clicked = true;
            }
        });

        button18_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressAndColor(75, R.color.progressBarColor);
                enableStartAndEndButtons();
                button18_6Clicked = true;
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("myCh", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "myCh")
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setContentTitle("Fasting begins now!")
                .setContentText("“Fasting for the body is food for the soul.”");

        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(this);

        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this, "myCh")
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setContentTitle("Congratulations!");

        notification2 = builder2.build();
        notificationManagerCompat2 = NotificationManagerCompat.from(this);
    }

    private void setProgressAndColor(int progress, int colorResource) {
        progressBar.setProgress(progress, true);
        int color = ContextCompat.getColor(this, colorResource);
        progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    // Enable "Start Fasting" and "End Fasting" buttons
    private void enableStartAndEndButtons() {
        Button startFastingButton = findViewById(R.id.button_start);
        Button endFastingButton = findViewById(R.id.button_end);

        startFastingButton.setVisibility(View.VISIBLE);
        startFastingButton.setClickable(true);

        endFastingButton.setVisibility(View.VISIBLE);
        endFastingButton.setClickable(true);
    }

    public void push1(View view) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission
            return;
        }
        startTimeMillis = System.currentTimeMillis();
        notificationManagerCompat.notify(1, notification);

        // Calculate the timestamp for fasting end
        long fastingEndMillis1 = startTimeMillis + (12 * 60 * 60 * 1000); // 12 hours
        long fastingEndMillis2 = startTimeMillis + (14 * 60 * 60 * 1000); // 14 hours
        long fastingEndMillis3 = startTimeMillis + (16 * 60 * 60 * 1000); // 16 hours
        long fastingEndMillis4 = startTimeMillis + (18 * 60 * 60 * 1000); // 18 hours

        if (button12_12Clicked) {
            // The "button12_12" was clicked before "push1"
            scheduleNotification(fastingEndMillis1);
        } else if(button14_10Clicked){
            scheduleNotification(fastingEndMillis2);
        } else if(button16_8Clicked){
            scheduleNotification(fastingEndMillis3);
        } else if(button18_6Clicked){
            scheduleNotification(fastingEndMillis4);
        }
    }

    private PendingIntent createFastingEndIntent() {
        Intent intent = new Intent(this, IntermittentFastingActivity.class);
        intent.setAction("FASTING_END_ACTION"); // Add an action to differentiate this intent
        intent.putExtra("NOTIFICATION_MESSAGE", "Congratulations! Your fasting duration has ended!");

        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }

    private void scheduleNotification(long triggerAtMillis) {
        fastingEndIntent = createFastingEndIntent(); // Create a new intent with the same action
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, fastingEndIntent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ("FASTING_END_ACTION".equals(intent.getAction())) {
            // The fasting duration has ended; show a notification
            String message = intent.getStringExtra("NOTIFICATION_MESSAGE");
            sendNotification(message);
        }
    }

    public void push2(View view) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission
            return;
        }
        endTimeMillis = System.currentTimeMillis();
        showFastingDurationNotification();


        // Calculate the fasting duration
        long fastingDurationMillis = endTimeMillis - startTimeMillis;
        long hours = fastingDurationMillis / (60 * 60 * 1000);
        long minutes = (fastingDurationMillis % (60 * 60 * 1000)) / (60 * 1000);

        // Save the fasting duration to Firebase
        saveFastingDurationToFirebase(hours, minutes);
    }

    private void saveFastingDurationToFirebase(long hours, long minutes) {
        // Create a data structure to hold the fasting duration
        FastingDuration fastingDuration = new FastingDuration(hours, minutes);

        // Push the fasting duration to Firebase under a unique key
        databaseReference.child("fastingDurations").push().setValue(fastingDuration);
    }

    private void showFastingDurationNotification() {
        long fastingDurationMillis = endTimeMillis - startTimeMillis;
        long hours = fastingDurationMillis / (60 * 60 * 1000);
        long minutes = (fastingDurationMillis % (60 * 60 * 1000)) / (60 * 1000);

        String durationText = hours + " hours and " + minutes + " minutes";

        String notificationMessage = "Achieved: " + durationText + "!";
        // Build and send the notification with this message
        sendNotification(notificationMessage);
    }

    private void sendNotification(String message) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (notificationManagerCompat2 != null) {
            notificationManagerCompat2.notify(2, createNotification2(message));
        }
    }

    private Notification createNotification2(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("myCh", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "myCh")
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setContentTitle("Congratulations!")
                .setContentText(message);

        return builder.build();
    }
}


class FastingDuration {

    private long hours;
    private long minutes;

    public FastingDuration() {
        // Default constructor required for Firebase
    }

    public FastingDuration(long hours, long minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }
}