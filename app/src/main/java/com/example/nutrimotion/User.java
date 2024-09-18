package com.example.nutrimotion;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import lombok.Data;



@RequiresApi(api = Build.VERSION_CODES.O)
@Data
public class User {
    private static String userId;
    private String firstName;
    private String lastName;
    private String password;
    public static String email;
    private static double height;
    private static double weight;
    private static Double kcalCount;

    private static Gender gender;

    private static Goal goal;

    private static int age;

    static Map<LocalDate, DayEntry> journal = new HashMap<>();

    static LocalDate currentJournalDay = LocalDate.now();

    public static Double recommendedFatIntake;
    public static Double recommendedProteinIntake;
    public static Double recommendedCarbsIntake;

    private List<PhotoData> photoDataList; // List to store photo data

    static FirebaseAuth mAuth;

    static FirebaseFirestore db = FirebaseFirestore.getInstance();


    // Default constructor (required for Firebase)
    public User() {
    }

    // Constructor for registration (without additional data like height, weight, gender)
    public User(String firstName, String lastName, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.userId = generateUserId();
    }

    // Constructor for complete user data
    public User(String firstName, String lastName, String password, String email, double height, double weight, Gender gender, Goal goal, Double kcalCount, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.goal = goal;
        this.kcalCount = kcalCount;
        this.age = age;
        this.userId = generateUserId();
        this.photoDataList = new ArrayList<>();
    }
    // BCrypt configuration parameter
    private static final int BCRYPT_LOG_ROUNDS = 10;

    public static String hashPassword(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[BCrypt.gensalt(BCRYPT_LOG_ROUNDS).getBytes().length];
        random.nextBytes(salt);
        password = BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_LOG_ROUNDS));
        return password;
    }

    public void addPhotoData(PhotoData photoData) {
        this.photoDataList.add(photoData);
    }

    public static String getuserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




    // Method to generate auto-generated ID
    private String generateUserId() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        return reference.push().getKey();
    }

    public String getId() {
        return userId;
    }


    public static void setHeight(Double height) {
        User.height = height;
    }

    public static void setWeight(double weight) {
        User.weight = weight;
    }

    public static void setKcalCount(Double kcalCount) {
        User.kcalCount = kcalCount;
    }
    public static void setGender(Gender gender) {
        User.gender = gender;
    }
    public static void setAge(Integer age) {
        User.age = age;
    }

    public static void setGoal(Goal goal) {
        User.goal = goal;
    }


    public static double getWeight() {
        return weight;
    }

    public static Double getHeight() {
        return height;
    }
    public static Double getKcalCount() {
        return kcalCount;
    }

    public static Gender getGender() {return gender;}

    public static Integer getAge(){return age;}

    public static Goal getGoal(){return goal;}

    public static Map<LocalDate, DayEntry> getJournal(){return journal;}


    public static void fetchUserData(Navigation_Menu menuClass)
    {
        CollectionReference userSubcollection = db.collection("users");
        userSubcollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Access individual documents within the collection
                        String email = document.getString("email");
                        String userMail = User.email;
                        if (email != null && email.equals(User.email)) {
                            Map<String, Object> updatedData = null;
                            if (document.contains("Goal")) {
                                User.goal = Goal.valueOf(document.getString("Goal"));
                                User.gender = Gender.valueOf(document.getString("Gender"));
                                User.weight = document.getDouble("Weight");
                                User.height = document.getDouble("Height");

                                //deserialize
                                Type typeOfHashMap = new TypeToken<Map<LocalDate, DayEntry>>(){}.getType();
                                String journalString = document.getString("Journal");
//                                if(journalString != null && !journalString.trim().isEmpty())
//                                    journal = new Gson().fromJson(journalString, typeOfHashMap);

                            } else if (User.goal == null) {
                                menuClass.launchBmiActivity();
                            }
                        }
                    }
                }

            }
        });
    }


    public static void postUserData() {

        CollectionReference userSubcollection = db.collection("users");
        userSubcollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Access individual documents within the collection
                        String email = document.getString("email");
                        String userMail = User.email;
                        if (email != null && email.equals(User.email)) {
                            Map<String, Object> updatedData = null;
                            if (!document.contains("goal"))  {
                                // Set the height, weight, gender when goal is not present
                                updatedData = new HashMap<>();

                                // Add conditions to update only if the values are present in the document
                                updatedData.put("Height", User.height);
                                updatedData.put("Weight",User.weight);
                                updatedData.put("Gender", User.gender);
                                updatedData.put("Goal", User.goal);
                                //serialize
                                Gson gson = new Gson();
                                String serailizedJournal = gson.toJson(journal);
                                updatedData.put("Journal", serailizedJournal);

                            }

                            if (!updatedData.isEmpty()) {
                                userSubcollection.document(document.getId()).update(updatedData);
                            }
                        }
                    }

                    // Do something with the data (e.g., display it, process it, etc.)

                } else {
                    // Handle errors
                    Exception exception = task.getException();
                    if (exception != null) {
                        exception.printStackTrace();
                    }
                }
            }
        });

    }
}
