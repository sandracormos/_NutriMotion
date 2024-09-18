package com.example.nutrimotion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Fortschrittsfotos extends AppCompatActivity {

    private AppCompatButton uploadPhotoButtonBefore;
    private AppCompatButton uploadPhotoButtonAfter;
    private ImageView beforeImageView;
    private ImageView afterImageView;
    private TextView beforeDateTextView;
    private TextView afterDateTextView;

    private TextView daysDifferenceView;
    private static final int PICK_IMAGE_REQUEST_BEFORE = 1;
    private static final int PICK_IMAGE_REQUEST_AFTER = 2;

    private static final String PREF_NAME = "PhotoData";
    private static final String PREF_BEFORE_PHOTO_PATH = "before_photo_path";
    private static final String PREF_BEFORE_PHOTO_DATE = "before_photo_date";
    private static final String PREF_AFTER_PHOTO_PATH = "after_photo_path";
    private static final String PREF_AFTER_PHOTO_DATE = "after_photo_date";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fortschrittsfotos);

        uploadPhotoButtonBefore = findViewById(R.id.btnUploadPhotoBefore);
        uploadPhotoButtonAfter = findViewById(R.id.btnUploadPhotoAfter);
        beforeImageView = findViewById(R.id.beforePhoto);
        afterImageView = findViewById(R.id.afterPhoto);
        beforeDateTextView = findViewById(R.id.beforeDateTextView);
        afterDateTextView = findViewById(R.id.afterDateTextView);
        daysDifferenceView = findViewById(R.id.daysDifferenceTextView);

        String text2 = "Upload photo BEFORE";
        String formattedText = text2.substring(0, 1).toUpperCase() + text2.substring(1).toLowerCase();
        uploadPhotoButtonBefore.setText(formattedText);

        String text3 = "Upload photo AFTER";
        String formattedText2 = text3.substring(0, 1).toUpperCase() + text3.substring(1).toLowerCase();
        uploadPhotoButtonAfter.setText(formattedText2);

        // Load and set the before photo if it exists in internal storage
        String beforePhotoPath = retrievePhotoPathFromPreferences(PREF_BEFORE_PHOTO_PATH);
        if (beforePhotoPath != null) {
            Bitmap beforeBitmap = BitmapFactory.decodeFile(beforePhotoPath);
            beforeImageView.setImageBitmap(beforeBitmap);
        }

        // Load and set the after photo if it exists in internal storage
        String afterPhotoPath = retrievePhotoPathFromPreferences(PREF_AFTER_PHOTO_PATH);
        if (afterPhotoPath != null) {
            Bitmap afterBitmap = BitmapFactory.decodeFile(afterPhotoPath);
            afterImageView.setImageBitmap(afterBitmap);
        }

        uploadPhotoButtonBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the phone's gallery for before photo
                Intent galleryIntentBefore = new Intent(Intent.ACTION_PICK);
                galleryIntentBefore.setType("image/*");
                startActivityForResult(galleryIntentBefore, PICK_IMAGE_REQUEST_BEFORE);
            }
        });

        uploadPhotoButtonAfter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Open the phone's gallery for after photo
                Intent galleryIntentAfter = new Intent(Intent.ACTION_PICK);
                galleryIntentAfter.setType("image/*");
                startActivityForResult(galleryIntentAfter, PICK_IMAGE_REQUEST_AFTER);
            }
        });
    }

    // Method to retrieve the photo path from SharedPreferences
    private String retrievePhotoPathFromPreferences(String prefKey) {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return preferences.getString(prefKey, null);
    }

    // Handle the result from the gallery intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE_REQUEST_BEFORE) {
                // Handle before photo
                Uri selectedImageUriBefore = data.getData();
                beforeImageView.setImageURI(selectedImageUriBefore);
                String photoDateBefore = getPhotoDate(selectedImageUriBefore);
                beforeDateTextView.setText("Date: " + photoDateBefore);

                // Calculate and display the time difference
                calculateAndDisplayTimeDifference();

                savePhotoDataFirestore(PREF_BEFORE_PHOTO_PATH, PREF_BEFORE_PHOTO_DATE); // For before photo

            } else if (requestCode == PICK_IMAGE_REQUEST_AFTER) {
                // Handle after photo
                Uri selectedImageUriAfter = data.getData();
                afterImageView.setImageURI(selectedImageUriAfter);
                String photoDateAfter = getPhotoDate(selectedImageUriAfter);
                afterDateTextView.setText("Date: " + photoDateAfter);

                // Calculate and display the time difference
                calculateAndDisplayTimeDifference();

                savePhotoDataFirestore(PREF_AFTER_PHOTO_PATH, PREF_AFTER_PHOTO_DATE);   // For after photo

            }
        }
    }

    private String saveImageToInternalStorage(Uri imageUri, String fileName, String prefKey) {
        try {
            // Open an input stream from the content resolver
            InputStream inputStream = getContentResolver().openInputStream(imageUri);

            // Decode the input stream into a Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Create or get the "photos" directory in the app's internal storage
            File internalStorage = createOrGetPhotosDirectory();

            // Create a new file in the "photos" directory with the given file name
            File photoFile = new File(internalStorage, fileName);

            // Save the bitmap to the file
            saveBitmapToFile(bitmap, photoFile);

            // Save the absolute path of the saved photo file in SharedPreferences
            savePhotoPathInPreferences(prefKey, photoFile.getAbsolutePath());

            // Return the absolute path of the saved photo file
            return photoFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Save the absolute path of the saved photo file in SharedPreferences
    private void savePhotoPathInPreferences(String prefKey, String photoFilePath) {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(prefKey, photoFilePath);
        editor.apply();
    }

    // Save the given bitmap to the specified file
    private void saveBitmapToFile(Bitmap bitmap, File file) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        }
    }

    // Create or get the "photos" directory in the app's internal storage
    private File createOrGetPhotosDirectory() {
        File internalStorage = new File(getFilesDir(), "photos");
        if (!internalStorage.exists()) {
            internalStorage.mkdirs();
        }
        return internalStorage;
    }

    // Calculate and display the time difference in days
    private void calculateAndDisplayTimeDifference() {
        String beforeDateStr = beforeDateTextView.getText().toString().replace("Date: ", "");
        String afterDateStr = afterDateTextView.getText().toString().replace("Date: ", "");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        try {
            Date beforeDate = dateFormat.parse(beforeDateStr);
            Date afterDate = dateFormat.parse(afterDateStr);

            long timeDifference = afterDate.getTime() - beforeDate.getTime();
            long daysDifference = timeDifference / (1000 * 60 * 60 * 24);

            // Display the time difference above the dates
            daysDifferenceView.setText("Difference: " + daysDifference + " days");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get the date the photo was taken from its URI
    private String getPhotoDate(Uri uri) {
        String[] projection = {MediaStore.Images.ImageColumns.DATE_TAKEN};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") long timestamp = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN));
            cursor.close();

            // Convert the timestamp to a readable date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            return dateFormat.format(new Date(timestamp));
        }

        return "Date not available";
    }

    private void savePhotoDataFirestore(String prefKeyPath, String prefKeyDate) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a map to represent the photo data
            Map<String, Object> photoData = new HashMap<>();
            photoData.put(prefKeyPath, getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(prefKeyPath, ""));
            photoData.put(prefKeyDate, getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(prefKeyDate, ""));

            // Store the photo data in Firestore under the user's document
            db.collection("users").document(currentUser.getUid()).collection("photoData")
                    .add(photoData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Handle successful save
                            Toast.makeText(Fortschrittsfotos.this, "Photo data saved successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failed save
                            Toast.makeText(Fortschrittsfotos.this, "Failed to save photo data", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}
