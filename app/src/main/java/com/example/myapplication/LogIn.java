package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vdx.designertoast.DesignerToast;

public class LogIn extends AppCompatActivity {

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Button apply_button = (Button) findViewById(R.id.registration_btn);
        Button certificate_button = (Button) findViewById(R.id.certificate_btn);
        Button check_stat_button = (Button) findViewById(R.id.check_status_btn);

        dialog = new Dialog(this);

        apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserRootDBandStartActivityApplyForVaccine();
            }
        });

        certificate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyUserRequirements(); // also calls passUserRootDBandStartVaccineCertificate() on condition
            }
        });

        check_stat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDoseTaken();
            }
        });

    }

    public void dialogCongrats()
    {
        dialog.setContentView(R.layout.congrats_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button ok_button = dialog.findViewById(R.id.dialog_btn);

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void checkDoseTaken()
    {
        DatabaseReference reference;

        reference = FirebaseDatabase.getInstance().getReference("Users").child(getUserRoot());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    if(snapshot.child("first_shot").getValue().toString().equals("Yes") & snapshot.child("second_shot").getValue().toString().equals("Yes"))
                    {
                        dialogCongrats();
                    }
                    else
                    {
                        DesignerToast.Warning(LogIn.this, "NOT ELIGIBLE", "You Haven't Taken All The Vaccines!", Gravity.BOTTOM , Toast.LENGTH_SHORT, DesignerToast.STYLE_DARK);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void verifyUserRequirements()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(getUserRoot()).child("Vaccination Details");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    passUserRootDBandStartVaccineCertificate();
                }
                else if(!snapshot.exists())
                {
                    DesignerToast.Error(LogIn.this, "FAILED", "Register First to Get Certificate!", Gravity.BOTTOM , Toast.LENGTH_SHORT, DesignerToast.STYLE_DARK);
                }
                else
                {
                    testToast("unknown snapshot error");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                testToast("onCancelled()");
            }
        });
    }

    public String getUserRoot()
    {
        Intent intent = getIntent(); // get user nid from main activity
        return intent.getStringExtra("user_nid");
    }

    public void passUserRootDBandStartActivityApplyForVaccine()
    {
        String user_nid = getUserRoot();

        Intent intent = new Intent(LogIn.this, ApplyForVaccine.class);
        intent.putExtra("user_nid", user_nid);
        startActivity(intent);
    }

    public void passUserRootDBandStartVaccineCertificate()
    {
        String user_nid = getUserRoot();

        Intent intent = new Intent(LogIn.this, VaccineCertificate.class);
        intent.putExtra("user_nid", user_nid);
        startActivity(intent);
    }


    public void testToast(String txt)
    {
        Toast.makeText(LogIn.this, txt, Toast.LENGTH_LONG).show();
    }
}