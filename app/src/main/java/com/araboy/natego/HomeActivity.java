package com.araboy.natego;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    Button btnRedo, btnLogout, btnAddFood, btnTodaysInfo;
    TextView txtWelcome, txtTotalCals;

    String dateS;
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    boolean surveyComplete;
    boolean loggedOut = false;

    DocumentReference documentReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        instantiate();

        if(user!= null && userId != null) {
            fStore.collection(userId).document("Daily Food").collection(dateS)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        List<DocumentSnapshot> foods = Objects.requireNonNull(task.getResult()).getDocuments();
                        double sum = 0;
                        for (DocumentSnapshot ds : foods) {
                            sum += (double) ds.get("Calories");
                        }
                        txtTotalCals.setText(sum + "");
                    } else {

                    }
                }
            });

            if(user!= null && userId != null && loggedOut == false) {
                DocumentReference dName = fStore.collection("Users").document(userId);
                dName.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        assert value != null;
                        if(value != null) {
                            txtWelcome.setText("Hey, " + value.getString("Full Name") + "!");
                        }
                    }
                });
            }
            btnAddFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), AddFoodActivity.class));
                }
            });
            btnRedo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), SurveyActivity.class));
                    //Attempt to fix bug until end of try catch
                    try {
                        if (fAuth.getCurrentUser() != null) {
                            //Log.d(TAG, "")
                            user = fAuth.getCurrentUser();
                            userId = user.getUid();
                            Map<String, Object> isCompleteMap = new HashMap<>();
                            isCompleteMap.put("isComplete", false);
                            DocumentReference db = fStore.collection(userId).document("Survey");
                            db.set(isCompleteMap, SetOptions.merge());
                        }
                    } catch (Exception e) {
                        Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
            btnTodaysInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), TodaysInfoActivity.class));
                }
            });


    }

    public void logout(View view){
        loggedOut = true;
        FirebaseAuth.getInstance().signOut();//Logout
        startActivity(new Intent(getApplicationContext(),MainActivity2.class));
        finish();

    }

    public  void instantiate(){
        btnLogout = findViewById(R.id.btnLogout);
        txtWelcome = findViewById(R.id.txtWelcome);
        txtTotalCals = findViewById(R.id.txtTotalCaloriesValue);
        btnRedo = findViewById(R.id.btnRedo);
        btnAddFood = findViewById(R.id.btnAddFood);
        btnTodaysInfo = findViewById(R.id.btnTodaysInfo);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        if(user!= null) {
            userId = user.getUid();
        }
        dateS = getDate(new Date());
    }
/*
    public int getTotalCalories(){
        int sum = 0;
        fStore.collection(userId).document("Daily Food").collection("Sep 11 2020")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<DocumentSnapshot> foods = task.getResult().getDocuments();
                    for(DocumentSnapshot ds : foods){
                        sum += ds.get("Calories");
                    }
                }
            }
        });


        return 0;
    }

 */

    public static String getDate(Date date) {
        String month, day, year, dateWTime;
        dateWTime = date.toString();
        switch(dateWTime.substring(4, 7)) {
            case "Jan":
                month = "1";
                break;
            case "Feb":
                month = "2";
                break;
            case "Mar":
                month = "3";
                break;
            case "Apr":
                month = "4";
                break;
            case "May":
                month = "5";
                break;
            case "Jun":
                month = "6";
                break;
            case "Jul":
                month = "7";
                break;
            case "Aug":
                month = "8";
                break;
            case "Sep":
                month = "9";
                break;
            case "Oct":
                month = "10";
                break;
            case "Nov":
                month = "11";
                break;
            case "Dec":
                month = "12";
                break;
            default:
                month = "else";
                break;
        }

        day = dateWTime.substring(8, 10);

        year = dateWTime.substring(dateWTime.length()-4, dateWTime.length());

        return month+"-"+day+"-"+year;

    }

}