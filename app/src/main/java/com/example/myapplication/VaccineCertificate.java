package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class VaccineCertificate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateViewInfoFromDB();
        setContentView(R.layout.activity_vaccine_certificate);
    }

    public String getUserRoot()
    {
        Intent intent = getIntent(); // get user nid from main activity
        return intent.getStringExtra("user_nid");
    }

    public void updateViewInfoFromDB()
    {
        DatabaseReference reference;

        reference = FirebaseDatabase.getInstance().getReference("Users").child(getUserRoot());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    TextView nid_info = (TextView) findViewById(R.id.user_nid_certi);
                    TextView name_info = (TextView) findViewById(R.id.user_name_certi);
                    TextView contact_info = (TextView) findViewById(R.id.user_contact_certi);
                    TextView occupation_info = (TextView) findViewById(R.id.user_occupation_certi);
                    TextView booster_name_info = (TextView) findViewById(R.id.user_booster_name_certi);
                    TextView booster_loc_info = (TextView) findViewById(R.id.user_booster_loc_certi);

                    name_info.setText(snapshot.child("name").getValue().toString());
                    nid_info.setText(snapshot.child("nid").getValue().toString());
                    contact_info.setText(snapshot.child("phone").getValue().toString());
                    occupation_info.setText(snapshot.child("occupation").getValue().toString());
                    booster_name_info.setText(snapshot.child("Vaccination Details").child("name_of_dose").getValue().toString());
                    booster_loc_info.setText(snapshot.child("Vaccination Details").child("vaccination_center").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}