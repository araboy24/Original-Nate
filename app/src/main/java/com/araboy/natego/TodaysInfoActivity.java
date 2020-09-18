package com.araboy.natego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

public class TodaysInfoActivity extends AppCompatActivity {
    TextView txtCalories, txtCarbs, txtFat, txtProtein, txtDate;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String userId;
    Button btnHome;
    String dateS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_info);
        instantiate();
        if(user != null){
            fStore.collection(userId).document("Daily Food").collection(dateS)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> foods = task.getResult().getDocuments();
                        double sumCal = 0;
                        double sumCarb = 0;
                        double sumProtein = 0;
                        double sumFat = 0;
                        for (DocumentSnapshot ds : foods) {
                            sumCal += (double) ds.get("Calories");
                            sumCarb += (double) ds.get("Carbs");
                            sumFat += (double) ds.get("Fat");
                            sumProtein += (double) ds.get("Protein");
                        }
                        txtCalories.setText("Calories: " + sumCal);
                        txtCarbs.setText("Carbohydrates: " + sumCarb);
                        txtFat.setText("Fat: " + sumFat);
                        txtProtein.setText("Protein: " + sumProtein);
                    } else {

                    }
                }
            });
        }

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
    }

    public  void instantiate() {

        txtDate = findViewById(R.id.txtDateTitle);
        txtCalories = findViewById(R.id.txtCal);
        txtCarbs = findViewById(R.id.txtCarb);
        txtFat = findViewById(R.id.txtFat);
        txtProtein = findViewById(R.id.txtProtein);
        btnHome = findViewById(R.id.btnHome);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }
        dateS = getDate(new Date());
        txtDate.setText(dateS);
    } //End instantiate

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