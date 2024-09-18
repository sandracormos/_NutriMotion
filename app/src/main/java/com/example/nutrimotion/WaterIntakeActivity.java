package com.example.nutrimotion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class WaterIntakeActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "WaterIntakeChannel";
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;

    private View progressLine;
    private int totalWidth;
    private double currentProgress = 0; // Using a double to represent progress as a fraction

    private double intake100ml = 0;
    private double intake200ml = 0;
    private double intake500ml = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_intake);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();


        progressLine = findViewById(R.id.progressLine);
        totalWidth = 300;

        ImageView glassOfWater100ml = findViewById(R.id.glass_of_water_100ml);
        ImageView glassOfWater200ml = findViewById(R.id.glass_of_water_200ml);
        ImageView glassOfWater500ml = findViewById(R.id.glass_of_water_500ml);
        TextView messageTextView = findViewById(R.id.messageTextView);
        glassOfWater100ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intake100ml == 3 || intake100ml + 0.15 >= 3) {   // 5% of 2l = 1dl .... 0.15 in a 300 long progressline
                    showNotification("Water Intake Goal Achieved", "Congratulations!");
                } else {
                    // Show the text message for adding 100ml
                    messageTextView.setText("+100ml");
                    messageTextView.setVisibility(View.VISIBLE);

                    // Use a Handler to hide the message after 2 seconds
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageTextView.setVisibility(View.INVISIBLE);
                        }
                    }, 2000); // 2000 milliseconds (2 seconds)

                    // Adjust the progress and intake for 100ml
                    currentProgress += 0.15;
                    intake100ml += 0.15;

                    // Limit the progress (100%)
                    if (currentProgress > 3) {
                        currentProgress = 3;
                    }

                    updateProgressLine();
                }
            }
        });

        glassOfWater200ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intake200ml == 3 || intake200ml + 0.31 >= 3) {
                    showNotification("Water Intake Goal Achieved", "Congratulations!");
                } else {
                    // Show the text message for adding 200ml
                    messageTextView.setText("+200ml");
                    messageTextView.setVisibility(View.VISIBLE);

                    // Use a Handler to hide the message after 2 seconds
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public
                        void run() {
                            messageTextView.setVisibility(View.INVISIBLE);
                        }
                    }, 2000); // 2000 milliseconds (2 seconds)

                    // Adjust the progress and intake for 200ml
                    currentProgress += 0.31;
                    intake200ml += 0.31;

                    // Limit the progress (100%)
                    if (currentProgress > 3) {
                        currentProgress = 3;
                    }

                    updateProgressLine();
                }
            }
        });

        glassOfWater500ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intake500ml == 3 || intake500ml + 0.82 >= 3) {
                    showNotification("Water Intake Goal Achieved", "Congratulations!");
                } else {
                    // Show the text message for adding 500ml
                    messageTextView.setText("+500ml");
                    messageTextView.setVisibility(View.VISIBLE);

                    // Use a Handler to hide the message after 2 seconds
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public
                        void run() {
                            messageTextView.setVisibility(View.INVISIBLE);
                        }
                    }, 2000); // 2000 milliseconds (2 seconds)

                    // Adjust the progress and intake for 500ml
                    currentProgress += 0.82;
                    intake500ml += 0.82;

                    // Limit the progress (100%)
                    if (currentProgress > 3) {
                        currentProgress = 3;
                    }

                    updateProgressLine();
                }
            }
        });
        ImageView raindrop100ml = findViewById(R.id.raindrop3);
        ImageView raindrop200ml = findViewById(R.id.raindrop2);
        ImageView raindrop500ml = findViewById(R.id.raindrop);

        raindrop100ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentProgress > 0) {
                    // Show the text message for decreasing 100ml
                    messageTextView.setText("-100ml");
                    messageTextView.setVisibility(View.VISIBLE);

                    // Use a Handler to hide the message after 2 seconds
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageTextView.setVisibility(View.INVISIBLE);
                        }
                    }, 2000); // 2000 milliseconds (2 seconds)

                    // Adjust the progress and intake for -100ml
                    currentProgress -= 0.15;
                    intake100ml -= 0.15;

                    // Limit the progress (minimum 0%)
                    if (currentProgress < 0) {
                        currentProgress = 0;
                    }

                    updateProgressLine();
                }
            }
        });

        raindrop200ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentProgress > 0) {
                    // Show the text message for decreasing 200ml
                    messageTextView.setText("-200ml");
                    messageTextView.setVisibility(View.VISIBLE);

                    // Use a Handler to hide the message after 2 seconds
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageTextView.setVisibility(View.INVISIBLE);
                        }
                    }, 2000); // 2000 milliseconds (2 seconds)

                    // Adjust the progress and intake for -200ml
                    currentProgress -= 0.31;
                    intake200ml -= 0.31;

                    // Limit the progress (minimum 0%)
                    if (currentProgress < 0) {
                        currentProgress = 0;
                    }

                    updateProgressLine();
                }
            }
        });

        raindrop500ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentProgress > 0) {
                    // Show the text message for decreasing 500ml
                    messageTextView.setText("-500ml");
                    messageTextView.setVisibility(View.VISIBLE);

                    // Use a Handler to hide the message after 2 seconds
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageTextView.setVisibility(View.INVISIBLE);
                        }
                    }, 2000); // 2000 milliseconds (2 seconds)

                    // Adjust the progress and intake for -500ml
                    currentProgress -= 0.82;
                    intake500ml -= 0.82;

                    // Limit the progress (minimum 0%)
                    if (currentProgress < 0) {
                        currentProgress = 0;
                    }

                    updateProgressLine();
                }
            }
        });

    }

    private void updateProgressLine() {
        // Calculate the width for the progress based on the current progress
        int newWidth = (int) (currentProgress * totalWidth);

        // Create new layout parameters for the progress line
        ViewGroup.LayoutParams params = progressLine.getLayoutParams();
        params.width = newWidth;

        // Update the layout parameters of the progress line
        progressLine.setLayoutParams(params);

        // Make the progress line visible if currentProgress is greater than 0 but less than the total width, otherwise, hide it
        if (currentProgress > 0 && currentProgress < totalWidth) {
            progressLine.setVisibility(View.VISIBLE);
        } else {
            progressLine.setVisibility(View.GONE);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Water Intake Channel";
            String description = "Channel for Water Intake Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(String title, String content) {
        Notification.Builder builder = null;
        // Check if the Android version is Oreo (API level 26) or higher
        // applicable for devices running Android Oreo or newer
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.glass_of_water)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true);
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


}