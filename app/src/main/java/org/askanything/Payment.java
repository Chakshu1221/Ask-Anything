package org.askanything;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Payment extends AppCompatActivity implements PaymentResultListener {
    String amount;
    String keyId;
    String order_id;
    String prefillemail;
    String prefillphone;
    String receipt;
    GoogleSignInAccount account;
    String imgurl;
    ArrayList<String> arr;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AskAnything);
        setContentView((int) R.layout.activity_payment);
        try{
            getSupportActionBar().hide();
        }catch (Exception e){

        }
        account = GoogleSignIn.getLastSignedInAccount(Payment.this);
        arr =getIntent().getStringArrayListExtra("array");

        Checkout.preload(getApplicationContext());
        getData();

    }

    private void successPayment(ArrayList<String> arr) {
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                        getCurrentUser().getUid()).child("Personal Data").get().
                addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()){
                                DataSnapshot dataSnapshot = task.getResult();
                                String cusomerName = dataSnapshot.child("name").getValue().toString();
                                String cusomerEmail = dataSnapshot.child("email").getValue().toString();
                                AppointmentModel appointmentModel = new AppointmentModel(arr.get(0), arr.get(1), arr.get(2), arr.get(3), arr.get(4), arr.get(5), "pending", "" + cusomerName, "" + cusomerEmail,
                                        "" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser()
                                                .getUid()).child("My Appointments").child(arr.get(2))
                                        .setValue(appointmentModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                reference.child("All Appointments").child("" + FirebaseAuth.getInstance().getCurrentUser().getUid() + arr.get(2))
                                                        .setValue(appointmentModel);
                                                Snackbar.make(Payment.this.findViewById(android.R.id.content),
                                                        (CharSequence) "Appointment Sent", Snackbar.LENGTH_LONG).setBackgroundTint(Color.WHITE).show();
                                                Snackbar.make(Payment.this.findViewById(android.R.id.content),(CharSequence)
                                                                "Appointment Sent! Please go in your Appointment Section to confirm payment", Snackbar.LENGTH_INDEFINITE)
                                                        .setAction("OK", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                startActivity(new Intent(Payment.this,MainActivity.class));
                                                            }
                                                        }).setBackgroundTint(Color.WHITE).show();


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(Payment.this.findViewById(android.R.id.content),
                                                        (CharSequence) e.getMessage(), Snackbar.LENGTH_LONG).setBackgroundTint(Color.WHITE).show();

                                            }
                                        });
                            }
                            else if (account!=null){
                                AppointmentModel appointmentModel = new AppointmentModel(arr.get(0), arr.get(1), arr.get(2), arr.get(3), arr.get(4), arr.get(5), "pending", "" + account.getDisplayName(), "" + account.getEmail(),
                                        "" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser()
                                                .getUid()).child("My Appointments").child(arr.get(2))
                                        .setValue(appointmentModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                reference.child("All Appointments").child("" + FirebaseAuth.getInstance().getCurrentUser().getUid() + arr.get(2))
                                                        .setValue(appointmentModel);
                                                Snackbar.make(Payment.this.findViewById(android.R.id.content),
                                                        (CharSequence) "Appointment Sent", Snackbar.LENGTH_LONG).setBackgroundTint(Color.WHITE).show();
                                                Snackbar.make(Payment.this.findViewById(android.R.id.content),(CharSequence)
                                                                "Appointment Sent! Please go in your Appointment Section to confirm payment", Snackbar.LENGTH_INDEFINITE)
                                                        .setAction("OK", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                startActivity(new Intent(Payment.this,MainActivity.class));
                                                            }
                                                        }).setBackgroundTint(Color.WHITE).show();


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(Payment.this.findViewById(android.R.id.content),
                                                        (CharSequence) e.getMessage(), Snackbar.LENGTH_LONG).setBackgroundTint(Color.WHITE).show();

                                            }
                                        });
                            }
                            else{
                                Toast.makeText(Payment.this, "Please update your profile in order to sent an Appointment ", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }

                });

    }

    private void getData() {
        Volley.newRequestQueue(getApplicationContext()).
                add(new StringRequest(0, "https://joshichakshu.000webhostapp.com/TattooTempleHld/Razorpay/",
                        new Response.Listener<String>() {
            public void onResponse(String response) {
                Payment.this.responce(response);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Payment.this, ""+error.getMessage(), Toast.LENGTH_LONG).show();
                Payment.this.onBackPressed();
            }
        }));
    }

    /* access modifiers changed from: private */
    public void responce(String response) {

        try {
            JSONObject obj = new JSONObject(response);
            this.order_id = obj.getString("order_id");
            this.keyId = obj.getString("keyId");
            this.amount = obj.getString("amount");
            String string = obj.getString("receipt");
            this.receipt = string;
            makepayment(this.order_id, this.keyId, this.amount, string, this.prefillemail, this.prefillphone);
        } catch (JSONException e) {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void makepayment(String order_id2, String keyId2, String amount2, String receipt2, String prefillemail2, String prefillphone2) {
        Checkout checkout = new Checkout();
        checkout.setKeyID(keyId2);
        checkout.setImage(R.drawable.icon);

        try {
            JSONObject options = new JSONObject();
            options.put(AppMeasurementSdk.ConditionalUserProperty.NAME, "Chakshu Software Creation");
            options.put("description", "Appointment Charge at Tattoo Temple Haldwani for Tattoo  "+arr.get(2));
            options.put("image", ""+imgurl);
            options.put("order_id", order_id2);
            options.put(FirebaseAnalytics.Param.CURRENCY, "INR");
            options.put("amount", amount2);
            options.put("prefill.email", prefillemail2);
            options.put("prefill.contact", prefillphone2);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);
            checkout.open(this, options);
        } catch (Exception e) {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), CheckOutDesign.class));
            finish();
        }
    }

    public void onPaymentSuccess(String s) {
        successPayment(arr);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }


    public void onPaymentError(int i, String s) {
        Toast.makeText(Payment.this, "Payment Failed", Toast.LENGTH_LONG).show();
        startActivity(new Intent(Payment.this, CheckOutDesign.class));
        finish();
    }



    }
