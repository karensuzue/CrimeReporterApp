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

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Form extends AppCompatActivity {

    EditText etTitle;
    RadioGroup rgGroup;
    RadioButton rbViolentCrime;
    RadioButton rbPropertyCrime;
    RadioButton rbDrugActivity;
    RadioButton rbTrafficViolation;
    Button btnSubmit;

    EditText etDescription;
    EditText etTime;
    CheckBox cbResolved;
    boolean resolved;
    String title;
    RadioButton rb;
    String typeOfCrime;
    String description;
    String time;

    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference databaseReference= database.getReference();

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
                    title=etTitle.getText().toString().trim();
                }

                if(rgGroup.getCheckedRadioButtonId()==-1){
                    Toast.makeText(Form.this,"Please Enter All fields!",Toast.LENGTH_SHORT).show();
                }else{
                  //  int id=rgGroup.getCheckedRadioButtonId();
                    rb= findViewById(rgGroup.getCheckedRadioButtonId());
                    typeOfCrime=rb.getText().toString().trim();
                }

                if(etDescription.getText().toString().isEmpty()){
                    Toast.makeText(Form.this,"Please Enter All fields!",Toast.LENGTH_SHORT).show();
                }else{
                    description=etDescription.getText().toString().trim();
                }

                if(etTime.getText().toString().isEmpty()){
                    Toast.makeText(Form.this,"Please Enter All fields!",Toast.LENGTH_SHORT).show();
                }else{
                    time=etTime.getText().toString().trim();
                }

                if(!cbResolved.isChecked()){
                    Toast.makeText(Form.this,"Please Enter All fields!",Toast.LENGTH_SHORT).show();
                }else{
                    resolved=true;
                }


//

                CrimeReport report= new CrimeReport(title,typeOfCrime,description,time,resolved);
                databaseReference.child(time).setValue(report);

//                DatabaseReference myRef= database.getReference();
//                myRef=myRef.child(time);
//
//

                //need to send data to entry in ListView

            }
        });




    }

    public class CrimeReport{
        String title;
        String typeOfCrime;
        String description;
        String time;
        boolean resolved;

        public CrimeReport(){

        }

        public CrimeReport(String title, String typeOfCrime, String description, String time, boolean resolved){
            this.title=title;
            this.typeOfCrime=typeOfCrime;
            this.description=description;
            this.time=time;
            this.resolved=resolved;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTypeOfCrime() {
            return typeOfCrime;
        }

        public void setTypeOfCrime(String typeOfCrime) {
            this.typeOfCrime = typeOfCrime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public boolean isResolved() {
            return resolved;
        }

        public void setResolved(boolean resolved) {
            this.resolved = resolved;
        }
    }




}