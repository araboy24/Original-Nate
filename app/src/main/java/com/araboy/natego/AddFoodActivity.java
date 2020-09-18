package com.araboy.natego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddFoodActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;

    String name, dateS;
    Double calories, carbs, fat, protein;
    //int calories;

    Date date;
    String sDate;

    Button btnDone;
    EditText edtCalories, edtProtein, edtFat, edtCarbs, edtName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        try {
            instantiate();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean emptyField = false;
                try {
                    name = edtName.getText().toString();
                    calories = Double.parseDouble(edtCalories.getText().toString());
                    carbs = Double.parseDouble(edtCarbs.getText().toString());
                    protein = Double.parseDouble(edtProtein.getText().toString());
                    fat = Double.parseDouble(edtFat.getText().toString());
                } catch(Exception e){
                    emptyField = true;
                }
                if(emptyField == false) {
                    Map<String, Object> food = new HashMap<>();
                    food.put("Name", name);
                    food.put("Calories", calories);
                    food.put("Carbs", carbs);
                    food.put("Protein", protein);
                    food.put("Fat", fat);
                    if (user != null) {
                        DocumentReference docFood = fStore.collection(userId).document("Daily Food");
                        DocumentReference docDay = docFood.collection(sDate).document(name);
                        docDay.set(food, SetOptions.merge());
                    }
                }
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });

    }

    public void instantiate() throws ParseException {
        btnDone = findViewById(R.id.btnDone);
        edtCalories = findViewById(R.id.edtCalories);
        edtCarbs = findViewById(R.id.edtCarbs);
        edtFat = findViewById(R.id.edtFat);
        edtProtein = findViewById(R.id.edtProtein);
        edtName = findViewById(R.id.edtFoodName);
        date = new Date();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        if(user != null){
            userId = user.getUid();
        }
        sDate = getDate(date);

    }

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