package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rejowan.cutetoast.CuteToast;
import com.vdx.designertoast.DesignerToast;

public class MainActivity extends AppCompatActivity {

    Button sign_up_button, login_button;
    TextInputLayout reg_name,reg_pass; //edittext app wont run

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sign_up_button = (Button)findViewById(R.id.sign_btn);
        login_button = (Button)findViewById(R.id.login_btn);

        reg_name = findViewById(R.id.reg_name_input);
        reg_pass = findViewById(R.id.reg_pass_input);

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivitySignUp();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openActivityApplyForVaccine();
                processLogin();
//                testToast(reg_name.getEditText().getText().toString());
//                passUserRootDB();
            }
        });
    }


    public void processLogin()
    {
        if (!usernameFieldCheck() | !passwordFieldCheck()) // if any of them is false
        {
            CuteToast.ct(this, "Fields Cannot Be Empty", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
        }
        else
        {
            validateLoginInfo();
        }
    }

    public boolean usernameFieldCheck()
    {
        String username = reg_name.getEditText().getText().toString();

        if(username.isEmpty())
        {
            reg_name.setError("Field Cannot Be Empty");
            return false;
        }
        else
        {
            reg_name.setError(null);
            reg_name.setErrorEnabled(false);
            return true;
        }
    }

    public boolean passwordFieldCheck()
    {
        String password = reg_pass.getEditText().getText().toString();

        if(password.isEmpty())
        {
            reg_pass.setError("Field Cannot Be Empty");
            return false;
        }
        else
        {
            reg_pass.setError(null);
            reg_pass.setErrorEnabled(false);
            return true;
        }
    }


    public void validateLoginInfo()
    {
        String user_entered_name = reg_name.getEditText().getText().toString().trim();
        String user_entered_pass = reg_pass.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_entered_pass);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String username_db_snap = snapshot.child("name").getValue().toString();
                    if(username_db_snap.equals(user_entered_name))
                    {
                        DesignerToast.Success(getBaseContext(), "SUCCESS", "Log in Successful!",Gravity.BOTTOM ,Toast.LENGTH_SHORT, DesignerToast.STYLE_DARK);
                        passUserRootDBandOpenActivityLogIn(); // and start next activity
                    }
                    else
                    {
                        CuteToast.ct(getBaseContext(), "Invalid Name", CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();
                        reg_name.setError("Invalid password");
                    }
                }
                else
                {
                    CuteToast.ct(getBaseContext(), "Invalid NID", CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();
                    reg_pass.setError("Invalid NID");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void openActivitySignUp()
    {
        Intent intent_signup = new Intent(this, SignUp.class);
        startActivity(intent_signup);
    }

    public void openActivityLogin()
    {
        Intent intent_signup = new Intent(this, Log.class);
        startActivity(intent_signup);
    }

    public void testToast(String txt)
    {
        Toast.makeText(MainActivity.this, txt, Toast.LENGTH_LONG).show();
    }

    public void passUserRootDBandOpenActivityLogIn()
    {
        String user_nid = reg_pass.getEditText().getText().toString().trim();

        Intent intent = new Intent(MainActivity.this, LogIn.class);
        intent.putExtra("user_nid", user_nid);
        startActivity(intent);
    }
}