package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rejowan.cutetoast.CuteToast;
import com.vdx.designertoast.DesignerToast;

public class SignUp extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    EditText name_edit_text, phone_edit_text, nid_edit_text,occupation_edit_text;
    CheckBox first_shot_checkbox, second_shot_checkbox;
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // String name = name_edit_text.getText().toString(); || if nothing input then name == ""
        name_edit_text = (EditText)findViewById(R.id.name_edit_text);
        phone_edit_text = (EditText)findViewById(R.id.phone_edit_text);
        nid_edit_text = (EditText)findViewById(R.id.nid_edit_text);
        occupation_edit_text = (EditText)findViewById(R.id.occupation_edit_text);

        first_shot_checkbox = (CheckBox)findViewById(R.id.first_shot_checkbox);
        second_shot_checkbox = (CheckBox)findViewById(R.id.second_shot_checkbox);

        submit_btn = (Button)findViewById(R.id.submit_btn);


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(emptyFieldCheck())
                {
                    addToDatabase();
                    openActivityMain();
                }
            }
        });
    }

    public boolean emptyFieldCheck()
    {
        String name = name_edit_text.getText().toString();
        String phone = phone_edit_text.getText().toString();
        String nid = nid_edit_text.getText().toString();
        String occupation = occupation_edit_text.getText().toString();

        boolean field_empty_ok = false;

        if(name.isEmpty())
        {
            name_edit_text.setError("Field Cannot Be Empty");
            field_empty_ok = false;
        }
        else
        {
            name_edit_text.setError(null);
        }

        if(phone.isEmpty())
        {
            phone_edit_text.setError("Field Cannot Be Empty");
            field_empty_ok = false;
        }
        else
        {
            phone_edit_text.setError(null);
        }

        if(nid.isEmpty())
        {
            nid_edit_text.setError("Field Cannot Be Empty");
            field_empty_ok = false;
        }
        else
        {
            nid_edit_text.setError(null);
        }

        if(occupation.isEmpty())
        {
            occupation_edit_text.setError("Field Cannot Be Empty");
            field_empty_ok = false;
        }
        else
        {
            occupation_edit_text.setError(null);
        }


        if(!name.isEmpty() & !phone.isEmpty() & !nid.isEmpty() & !occupation.isEmpty())
        {
            field_empty_ok = true;
        }

        if(!field_empty_ok)
        {
            CuteToast.ct(this, "Fields Cannot Be Empty", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
        }

        return field_empty_ok;
    }

    public void addToDatabase()
    {
        rootNode = FirebaseDatabase.getInstance();
//        reference = rootNode.getReference("Users");
        reference = rootNode.getReference().child("Users"); // will create a node "User" if it doesn't exists or follow path existing path if exist

        String name = name_edit_text.getText().toString();
        String phone = phone_edit_text.getText().toString();
        String nid = nid_edit_text.getText().toString();
        String occupation = occupation_edit_text.getText().toString();

        String first_shot_taken;
        if(first_shot_checkbox.isChecked())
        {
            first_shot_taken = "Yes";
        }
        else
        {
            first_shot_taken = "No";
        }

        String second_shot_taken;
        if(second_shot_checkbox.isChecked())
        {
            second_shot_taken = "Yes";
        }
        else
        {
            second_shot_taken = "No";
        }

        DatabaseHelper data_helper = new DatabaseHelper(name, phone, nid, occupation, first_shot_taken, second_shot_taken);
        reference.child(nid).setValue(data_helper);

        DesignerToast.Success(getBaseContext(), "SUCCESS", "Registration Completed!", Gravity.BOTTOM ,Toast.LENGTH_SHORT, DesignerToast.STYLE_DARK);
    }

    public void openActivityMain()
    {
        Intent intent_signup = new Intent(this, MainActivity.class);
        startActivity(intent_signup);
    }

    public void checkExistingUser()
    {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("Users");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void testToast(String txt)
    {
        Toast.makeText(SignUp.this, txt, Toast.LENGTH_LONG).show();
    }
}

// FireBase
// create or navigate wil .child(); insert with .setValue();