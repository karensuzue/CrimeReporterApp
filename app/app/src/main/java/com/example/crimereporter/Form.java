package com.example.crimereporter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Form extends AppCompatActivity {

    EditText etTitle;
    RadioGroup rgGroup;
    RadioButton rbViolentCrime;
    RadioButton rbPropertyCrime;
    RadioButton rbDrugActivity;
    RadioButton rbTrafficViolation;
   // RadioButton rb;
    Button btnSubmit;

    EditText etDescription;
    EditText etTime;
    CheckBox cbResolved;
    boolean resolved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        etTitle=findViewById(R.id.etTitle);
        rgGroup=findViewById(R.id.rgGroup);
        rbViolentCrime=findViewById(R.id.rbViolentCrime);
        rbPropertyCrime=findViewById(R.id.rbPropertyCrime);
        rbDrugActivity=findViewById(R.id.rbDrugActivity);
        rbTrafficViolation=findViewById(R.id.rbTrafficViolation);
        etDescription= findViewById(R.id.etDescription);
        etTime=findViewById(R.id.etTime);
        cbResolved=findViewById(R.id.cbResolved);
        btnSubmit=findViewById(R.id.btnSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etTitle.getText().toString().isEmpty()){
                    Toast.makeText(Form.this,"Please Enter All fields!",Toast.LENGTH_SHORT).show();
                }else{
                    String title=etTitle.getText().toString().trim();
                }

                if(rgGroup.getCheckedRadioButtonId()==-1){
                    Toast.makeText(Form.this,"Please Enter All fields!",Toast.LENGTH_SHORT).show();
                }else{
                    int id=rgGroup.getCheckedRadioButtonId();
                    RadioButton rb= findViewById(rgGroup.getCheckedRadioButtonId());
                    String typeOfCrime=rb.getText().toString().trim();
                }

                if(etDescription.getText().toString().isEmpty()){
                    Toast.makeText(Form.this,"Please Enter All fields!",Toast.LENGTH_SHORT).show();
                }else{
                    String description=etDescription.getText().toString().trim();
                }

                if(etTime.getText().toString().isEmpty()){
                    Toast.makeText(Form.this,"Please Enter All fields!",Toast.LENGTH_SHORT).show();
                }else{
                    String time=etTime.getText().toString().trim();
                }

                if(!cbResolved.isChecked()){
                    Toast.makeText(Form.this,"Please Enter All fields!",Toast.LENGTH_SHORT).show();
                }else{
                    resolved=true;
                }

                //need to send data to entry in ListView

            }
        });


    }




}