package org.askanything;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CheckOutDesign extends AppCompatActivity {


    //getIntent
    String name;
    String price;
    String imageurl;
    NestedScrollView scrollview;
    CheckBox note;


    //database

    String cusomerName;
    String cusomerEmail;
    String am_pm = "";
    //variabl
    ImageView imageView;
    TextView cprice;
    TextView calender;
    TextView timeDialog;
    Button book;
    RadioGroup radioGroup;
    private RadioButton radioButton;
    //////////////////////////

    final Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AskAnything);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out_design);

        //getIntent
        name = getIntent().getStringExtra("name");
        price = getIntent().getStringExtra("price");
        imageurl = getIntent().getStringExtra("imageurl");

        //inialization
        imageView = findViewById(R.id.iii);
        scrollview = findViewById(R.id.scroolview);
        note = findViewById(R.id.note);


        cprice = findViewById(R.id.cprice);
        calender = findViewById(R.id.date);
        timeDialog = findViewById(R.id.time);
        book = findViewById(R.id.book);
        radioGroup = findViewById(R.id.radioGrp);


        scrollview.post(new Runnable() {
            public void run() {
                scrollview.fullScroll(View.FOCUS_DOWN);
            }
        });
        Glide.with(this).load(imageurl).into(imageView);
        cprice.setText("Price : ₹ " + price);

        try {
            getSupportActionBar().setTitle(name);
        } catch (Exception e) {

        }

        /*DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };*/

        /*calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CheckOutDesign.this,date,myCalendar.
                        get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.
                        get(Calendar.DAY_OF_MONTH)).show();
            }
        });*/

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(CheckOutDesign.this);
                dialog.setContentView(R.layout.customdatepicker);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
                DatePicker datePicker = dialog.findViewById(R.id.datePickerTimeline);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, month);
                            myCalendar.set(Calendar.DAY_OF_MONTH, day);
                            dialog.dismiss();
                            updateLabel();
                        }
                    });
                }
            }
        });

        timeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CheckOutDesign.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if (selectedHour < 12) {
                            am_pm = "AM";
                        } else {
                            am_pm = "PM";
                        }
/*
                        if (selectedHour<9){
                            Snackbar.make(CheckOutDesign.this.findViewById(android.R.id.content),(CharSequence)
                                            "No Appointment Accepted Before 9 AM. Please Choose Different Time Slot.", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                        }
                                    }).setBackgroundTint(Color.WHITE).show();

                        }
*/
                        if (selectedHour > 17) {
                            Snackbar.make(CheckOutDesign.this.findViewById(android.R.id.content), (CharSequence)
                                            "No Appointment Accepted After 5 PM. Please Choose Different Time Slot.", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                        }
                                    }).setBackgroundTint(Color.WHITE).show();
                        }
                        timeDialog.setText(selectedHour + ":" + selectedMinute + "  " + am_pm);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Your Preferred Time");
                mTimePicker.show();


            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = calender.getText().toString().trim();
                String time = timeDialog.getText().toString().trim();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                String payData = (String) radioButton.getText();


                if (date == null) {
                    Snackbar.make(CheckOutDesign.this.findViewById(android.R.id.content),
                            (CharSequence) "Please Choose Preferred Appointment Date", Snackbar.LENGTH_LONG).setBackgroundTint(Color.WHITE).show();
                    calender.requestFocus();
                } else if (date.equals("Choose Preferred Appointment Date")) {
                    Snackbar.make(CheckOutDesign.this.findViewById(android.R.id.content),
                            (CharSequence) "Please Choose Preferred Appointment Date", Snackbar.LENGTH_LONG).setBackgroundTint(Color.WHITE).show();
                    calender.requestFocus();
                } else if (date.isEmpty()) {
                    Snackbar.make(CheckOutDesign.this.findViewById(android.R.id.content),
                            (CharSequence) "Please Choose Preferred Appointment Date", Snackbar.LENGTH_LONG).setBackgroundTint(Color.WHITE).show();
                    calender.requestFocus();
                } else if (time.equals("Choose Preferred Appointment Time")) {
                    Snackbar.make(CheckOutDesign.this.findViewById(android.R.id.content),
                            (CharSequence) "Please Choose Preferred Appointment Time", Snackbar.LENGTH_LONG).setBackgroundTint(Color.WHITE).show();
                    timeDialog.requestFocus();
                } else if (time.isEmpty()) {
                    Snackbar.make(CheckOutDesign.this.findViewById(android.R.id.content),
                            (CharSequence) "Please Choose Preferred Appointment Time", Snackbar.LENGTH_LONG).setBackgroundTint(Color.WHITE).show();
                    timeDialog.requestFocus();
                } else if (time == null) {
                    Snackbar.make(CheckOutDesign.this.findViewById(android.R.id.content),
                            (CharSequence) "Please Choose Preferred Appointment Time", Snackbar.LENGTH_LONG).setBackgroundTint(Color.WHITE).show();
                    timeDialog.requestFocus();
                } else if (!note.isChecked()) {
                    Snackbar.make(CheckOutDesign.this.findViewById(android.R.id.content),
                            (CharSequence) "Please accept terms and conditions", Snackbar.LENGTH_LONG).setBackgroundTint(Color.WHITE).show();
                    timeDialog.requestFocus();
                } else {

                    if (payData.equals("Pay Now")) {
                        startActivity(new Intent(CheckOutDesign.this, Payment.class));
                    } else {

                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                                        getCurrentUser().getUid()).child("Personal Data").get().
                                addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DataSnapshot dataSnapshot = task.getResult();
                                            cusomerName = dataSnapshot.child("name").getValue().toString();
                                            cusomerEmail = dataSnapshot.child("email").getValue().toString();
                                            AppointmentModel appointmentModel = new AppointmentModel(imageurl, price, name, date, time, payData, "pending", "" + cusomerName, "" + cusomerEmail,
                                                    "" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                            reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser()
                                                            .getUid()).child("My Appointments").child(name)
                                                    .setValue(appointmentModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            reference.child("All Appointments").child("" + FirebaseAuth.getInstance().getCurrentUser().getUid() + name)
                                                                    .setValue(appointmentModel);

                                                            Snackbar.make(CheckOutDesign.this.findViewById(android.R.id.content),
                                                                    (CharSequence) "Appointment Sent", Snackbar.LENGTH_LONG).setBackgroundTint(Color.WHITE).show();
                                                            Snackbar.make(CheckOutDesign.this.findViewById(android.R.id.content),(CharSequence)
                                                                            "Appointment Sent! Please go in your Appointment Section to confirm payment", Snackbar.LENGTH_INDEFINITE)
                                                                    .setAction("OK", new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View view) {
                                                                            startActivity(new Intent(CheckOutDesign.this,MainActivity.class));
                                                                        }
                                                                    }).setBackgroundTint(Color.WHITE).show();

                                                            calender.setText("Choose Preferred Appointment Date");
                                                            timeDialog.setText("Choose Preferred Appointment Time");
                                                            note.setChecked(false);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Snackbar.make(CheckOutDesign.this.findViewById(android.R.id.content),
                                                                    (CharSequence) e.getMessage(), Snackbar.LENGTH_LONG).setBackgroundTint(Color.WHITE).show();

                                                        }
                                                    });
                                        }
                                    }

                                });
                    }

                }
            }
        });

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.datepicker, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        /*if (itemId == R.id.datepicker) {
            startActivity(new Intent(CheckOutDesign.this,DatePage.class));
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void updateLabel() {
        String myFormat = " E, dd/MM/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        calender.setText(dateFormat.format(myCalendar.getTime()));
    }


}