package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vdx.designertoast.DesignerToast;

import org.w3c.dom.Text;

public class ApplyForVaccine extends AppCompatActivity {

    String selected_spinner_vaccine;
    String selected_spinner_location;

    boolean d1_stat;
    boolean d2_stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateViewInfoFromDB();
        setContentView(R.layout.activity_apply_for_vaccine);
        spinnersSettings();

        Button button =(Button) findViewById(R.id.apply_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkUserRequirements())
                {
                    updateUserDatabase();
                    DesignerToast.Success(ApplyForVaccine.this, "SUCCESS", "You are now eligible for Certificate!", Gravity.BOTTOM , Toast.LENGTH_SHORT, DesignerToast.STYLE_DARK);
                    passUserRootDBandStartLogIn();
                }
                else
                {
                    DesignerToast.Error(ApplyForVaccine.this, "FAILED", "Must Take all Primer Doses!", Gravity.BOTTOM , Toast.LENGTH_SHORT, DesignerToast.STYLE_DARK);
                }

            }
        });
    }

    public void passUserRootDBandStartLogIn()
    {
        String user_nid = getUserRoot();

        Intent intent = new Intent(ApplyForVaccine.this, LogIn.class);
        intent.putExtra("user_nid", user_nid);
        startActivity(intent);
    }

    public String getUserRoot()
    {
        Intent intent = getIntent(); // get user nid from main activity
        return intent.getStringExtra("user_nid");
    }

    public boolean checkUserRequirements()
    {
        if(d1_stat & d2_stat)
            return true;
        else
            return false;
    }


    public void updateUserDatabase()
    {
        DatabaseReference reference;

        reference = FirebaseDatabase.getInstance().getReference("Users").child(getUserRoot()).child("Vaccination Details");

        DatabaseHelperApplyVaccine data_helper = new DatabaseHelperApplyVaccine(selected_spinner_location, selected_spinner_vaccine);
        reference.setValue(data_helper);
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
                    TextView dose1_stat = (TextView) findViewById(R.id.dose_1_status);
                    TextView dose2_stat = (TextView) findViewById(R.id.dose_2_status);

                    name_info.setText(snapshot.child("name").getValue().toString());
                    nid_info.setText(snapshot.child("nid").getValue().toString());
                    contact_info.setText(snapshot.child("phone").getValue().toString());
                    occupation_info.setText(snapshot.child("occupation").getValue().toString());

                    if(snapshot.child("first_shot").getValue().toString().equals("Yes"))
                    {
                        dose1_stat.setText("Completed");
                        dose1_stat.setTextColor(Color.parseColor("#00FF00"));
                        d1_stat = true;
                    }
                    else
                    {
                        dose1_stat.setText("Incomplete");
                        dose1_stat.setTextColor(Color.parseColor("#FF0000"));
                        d1_stat = false;
                    }

                    if(snapshot.child("second_shot").getValue().toString().equals("Yes"))
                    {
                        dose2_stat.setText("Completed");
                        dose2_stat.setTextColor(Color.parseColor("#00FF00"));
                        d2_stat = true;
                    }
                    else
                    {
                        dose2_stat.setText("Incomplete");
                        dose2_stat.setTextColor(Color.parseColor("#FF0000"));
                        d2_stat = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void spinnersSettings()
    {
        Spinner vaccine_name = (Spinner) findViewById(R.id.vaccine_avail_spinner);
        ArrayAdapter<CharSequence> vaccine_name_adapter = ArrayAdapter.createFromResource(getBaseContext(),R.array.available_vaccine, R.layout.spinner_selected_text);
        vaccine_name_adapter.setDropDownViewResource(R.layout.spinner_drop_text);
        vaccine_name.setAdapter(vaccine_name_adapter);
        vaccine_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                selected_spinner_vaccine = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner location_name = (Spinner) findViewById(R.id.location_name_spinner);
        ArrayAdapter<CharSequence> location_name_adapter = ArrayAdapter.createFromResource(getBaseContext(),R.array.available_location, R.layout.spinner_selected_text);
        location_name_adapter.setDropDownViewResource(R.layout.spinner_drop_text);
        location_name.setAdapter(location_name_adapter);
        location_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                selected_spinner_location = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void testToast(String txt)
    {
        Toast.makeText(ApplyForVaccine.this, txt, Toast.LENGTH_LONG).show();
    }
}