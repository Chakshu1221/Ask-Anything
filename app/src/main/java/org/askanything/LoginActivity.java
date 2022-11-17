package org.askanything;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {
    TextView signup,login,forgotpassword,welcome;
    Button signinButton,signupButton;
    LinearLayout loginLayout;
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    ProgressBar progressBar;
    ImageView google;
    String picurl="";
    LoginButton facebook;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    EditText email,password,name;
    DatabaseReference reference;
    boolean doubleBackToExitPressedOnce = false;
    FirebaseUser firebaseUser;
    CallbackManager mCallbackManager;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.Theme_EasyLearn);
        setContentView(R.layout.activity_login);


        FacebookSdk.setClientToken(String.valueOf(R.string.facebook_client_token));
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        try{
            getSupportActionBar().hide();
        }catch (Exception e){
        }
        if (firebaseUser != null) {
            if (firebaseUser.isEmailVerified()){
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }

        processRequest();




        //typecasting
        signup=findViewById(R.id.signup);
        progressBar=findViewById(R.id.progressBar);
        google=findViewById(R.id.google);
        facebook=findViewById(R.id.facebook);
        loginLayout=findViewById(R.id.login_layout);
        login=findViewById(R.id.login);
        welcome=findViewById(R.id.welcome);
        forgotpassword=findViewById(R.id.buttonforogotpassword);
        signupButton=findViewById(R.id.buttonsignup);
        signinButton=findViewById(R.id.buttonsignin);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        name=findViewById(R.id.name);

        //initialization
        mAuth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference("Users");

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton facebook = findViewById(R.id.facebook);
        facebook.setReadPermissions("email", "public_profile");
        facebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
// ...





        //onCreate Animation
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom);
        welcome.startAnimation(animation);


        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.from_lefttoright);
        name.startAnimation(animation1);
        email.startAnimation(animation1);
        password.startAnimation(animation1);

        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down);
        signup.startAnimation(animation2);
        login.startAnimation(animation2);


        Animation animation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_to_up);
        signinButton.startAnimation(animation3);
        //loginLayout.startAnimation(animation3);

        Animation animation4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
        google.startAnimation(animation4);
        facebook.startAnimation(animation4);




        //signin  button clicked
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SignIn
                String email1=email.getText().toString().trim();
                String password1=password.getText().toString().trim();

                if (email1.isEmpty()){
                    email.setError("Please Enter Your Email");
                    email.requestFocus();
                }
                else if (email1.equals("")){
                    email.setError("Please Enter Your Email");
                    email.requestFocus();
                }

                else if (email1==null){
                    email.setError("Please Enter Your Email");
                    email.requestFocus();
                }
                else if (password1.isEmpty()){
                    password.setError("Please Enter Your Email");
                    password.requestFocus();
                }
                else if (password1.equals("")){
                    password.setError("Please Enter Your Email");
                    password.requestFocus();
                }
                else if (password1==null){
                    password.setError("Please Enter Your Email");
                    password.requestFocus();
                }
                else if (password1.length()<8){
                    password.setError("Password Length must be 8");
                    password.requestFocus();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email1,password1).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (!user.isEmailVerified()){
                                            user.sendEmailVerification();
                                            Snackbar.make(LoginActivity.this.findViewById(android.R.id.content),(CharSequence)
                                                            "Please confirm your Email to login", Snackbar.LENGTH_INDEFINITE)
                                                    .setAction("OK", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Snackbar.make(LoginActivity.this.findViewById(android.R.id.content),"",Snackbar.LENGTH_SHORT)
                                                                    .setBackgroundTint(Color.TRANSPARENT).show();
                                                        }
                                                    }).setBackgroundTint(Color.RED).show();
                                            progressBar.setVisibility(View.GONE);

                                        }
                                        else{
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                            finish();


                                        }

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }).addOnCanceledListener(new OnCanceledListener() {
                                @Override
                                public void onCanceled() {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "Task is canceled by the User", Toast.LENGTH_SHORT).show();
                                }
                            });

                }




            }
        });

        //signUp button clicked
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1=email.getText().toString().trim();
                String password1=password.getText().toString().trim();
                String name1=name.getText().toString().trim();


                if (name1.isEmpty()){
                    name.setError("Please Enter Your Name");
                    name.requestFocus();
                }
                else if (name1.equals("")){
                    name.setError("Please Enter Your Name");
                    name.requestFocus();
                }

                else if (name1==null){
                    name.setError("Please Enter Your Name");
                    name.requestFocus();
                }
                else if (email1.isEmpty()){
                    email.setError("Please Enter Your Email");
                    email.requestFocus();
                }
                else if (email1.equals("")){
                    email.setError("Please Enter Your Email");
                    email.requestFocus();
                }

                else if (email1==null){
                    email.setError("Please Enter Your Email");
                    email.requestFocus();
                }
                else if (password1.isEmpty()){
                    password.setError("Please Enter Your Email");
                    password.requestFocus();
                }
                else if (password1.equals("")){
                    password.setError("Please Enter Your Email");
                    password.requestFocus();
                }
                else if (password1==null){
                    password.setError("Please Enter Your Email");
                    password.requestFocus();
                }
                else if (password1.length()<8){
                    password.setError("Password Length must be 8");
                    password.requestFocus();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email1,password1).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        SignUpConstructor signUpConstructor=
                                                new SignUpConstructor(""+name
                                                        .getText().toString(),""+ email1,""+ picurl);
                                        reference.child(mAuth.
                                                        getCurrentUser().getUid()).child("Personal Data").setValue(signUpConstructor)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Snackbar.make(LoginActivity.this.findViewById(android.R.id.content),(CharSequence)
                                                                            "A confirmation email has been sent please verify email to get login", Snackbar.LENGTH_INDEFINITE)
                                                                    .setAction("OK", new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View view) {
                                                                            Snackbar.make(LoginActivity.this.findViewById(android.R.id.content),"",Snackbar.LENGTH_SHORT)
                                                                                    .setBackgroundTint(Color.TRANSPARENT).show();
                                                                        }
                                                                    }).setBackgroundTint(Color.RED).show();
                                                            FirebaseUser user = mAuth.getCurrentUser();
                                                            user.sendEmailVerification();
                                                            progressBar.setVisibility(View.GONE);



                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressBar.setVisibility(View.GONE);
                                                        Toast.makeText(LoginActivity.this, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });


        //signup view  clicked
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide2);
                loginLayout.startAnimation(animation);
                signup.setTextColor(Color.BLACK);
                login.setTextColor(Color.BLUE);
                name.setVisibility(View.VISIBLE);
                forgotpassword.setVisibility(View.GONE);
                signinButton.setVisibility(View.GONE);
                signupButton.setVisibility(View.VISIBLE);

            }
        });

        //login button clicked
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
                loginLayout.startAnimation(animation);
                signup.setTextColor(Color.BLUE);
                login.setTextColor(Color.BLACK);
                forgotpassword.setVisibility(View.VISIBLE);
                signinButton.setVisibility(View.VISIBLE);
                signupButton.setVisibility(View.GONE);
                name.setVisibility(View.GONE);
            }
        });

        //welcome text  click

        welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));

            }
        });

        //forgot password button click

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1=email.getText().toString().trim();
                if (email1.isEmpty()){
                    email.setError("Please Enter Your Email");
                    email.requestFocus();
                }
                else if (email1.equals("")){
                    email.setError("Please Enter Your Email");
                    email.requestFocus();
                }

                else if (email1==null){
                    email.setError("Please Enter Your Email");
                    email.requestFocus();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.sendPasswordResetEmail(email1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                Snackbar.make(LoginActivity.this.findViewById(android.R.id.content),
                                        (CharSequence) "Password reset email has been sent", Snackbar.LENGTH_LONG).setBackgroundTint(Color.RED).show();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Snackbar.make(LoginActivity.this.findViewById(android.R.id.content),(CharSequence) ""+e.getMessage(), Snackbar.LENGTH_LONG).setBackgroundTint(Color.RED).show();

                        }
                    });
                }
            }
        });

        //google signin

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLogin();
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginfacebook();

            }
        });
    }

    private void loginfacebook() {

    }
    private void handleFacebookAccessToken(AccessToken token) {
        Toast.makeText(this, ""+token, Toast.LENGTH_SHORT).show();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }


    private void processLogin() {
        Intent signInIntent=mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,101);
    }

    //process request of googler method
    private void processRequest() {
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(LoginActivity.this,gso);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==101){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account=task.getResult(ApiException.class);
                firebaseAuthwithGoogle(account.getIdToken());

            } catch (ApiException e) {
                Snackbar.make(LoginActivity.this.findViewById(android.R.id.content),(CharSequence) ""+e.getMessage(), Snackbar.LENGTH_LONG).setBackgroundTint(Color.RED).show();

            }
        }
        //mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void firebaseAuthwithGoogle(String idToken) {
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //SignUpConstructor signUpConstructor=new SignUpConstructor(""+ account.getDisplayName(),""+ account.getEmail());
                            //reference.child(mAuth.getCurrentUser().getUid()).child("Personal Data").setValue(signUpConstructor);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();

                        }
                        else {
                            Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                            Snackbar.make(LoginActivity.this.findViewById(android.R.id.content),(CharSequence) "Authentication Failed", Snackbar.LENGTH_LONG).setBackgroundTint(Color.RED).show();

                        }
                    }
                });
    }

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            //System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }





}


