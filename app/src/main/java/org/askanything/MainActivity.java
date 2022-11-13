package org.askanything;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ResideMenu resideMenu;
    ResideMenuItem home;
    public static ResideMenuItem appointment;
    ResideMenuItem profile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AskAnything);
        setContentView(R.layout.activity_main);
        if (savedInstanceState==null){
            changeFrag(new Home());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton imageButton=findViewById(R.id.menuButton);
        CircleImageView profileimg=findViewById(R.id.profileimg);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }catch (Exception e){

        }
        FirebaseDatabase.getInstance().getReference("Users").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Personal Data").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            DataSnapshot snapshot=task.getResult();
                            String bgurl=snapshot.child("picurl").getValue().toString();
                            Glide.with(MainActivity.this).load(bgurl).into(profileimg);

                        }
                    }
                });
        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog()
            }
        });

        resideMenu=new ResideMenu(this);
        resideMenu.setBackground(R.color.back);
        resideMenu.attachToActivity(this);

        //String titles[]={"Home","My Appointments","Profile"};
        //int icon[]={R.drawable.home,R.drawable.appointment_icon,R.drawable.profile};

        home=new ResideMenuItem(this,R.drawable.home,"HOME");

        appointment=new ResideMenuItem(this,R.drawable.appointment_icon,"My Appointments");
        profile=new ResideMenuItem(this,R.drawable.profile,"PROFILE");

        resideMenu.addMenuItem(home,ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(appointment,ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(profile,ResideMenu.DIRECTION_LEFT);

        home.setOnClickListener(this);
        appointment.setOnClickListener(this);
        profile.setOnClickListener(this);




        resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
        resideMenu.closeMenu();
        resideMenu.setScaleValue(0.7f);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);





    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view==home){
            changeFrag(new Home());
        }
        else if (view == appointment) {
            changeFrag(new Appointments());
        }
        else if (view == profile) {
            changeFrag(new Profile());
        }
        resideMenu.closeMenu();

    }
    public void changeFrag(Fragment fragment){
        //resideMenu.clearIgnoredViewList();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.main_frag,fragment,"fragment").
                setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

    }

}