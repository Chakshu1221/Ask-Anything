package org.askanything;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ResideMenu resideMenu;
    ResideMenuItem home;
    ResideMenuItem profile;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState==null){
            changeFrag(new Home());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton imageButton=findViewById(R.id.menuButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });

        resideMenu=new ResideMenu(this);
        resideMenu.setBackground(R.color.black);
        resideMenu.attachToActivity(this);

        String titles[]={"Home","Profile"};
        int icon[]={R.drawable.home,R.drawable.profile};

        home=new ResideMenuItem(this,R.drawable.home,"HOME");
        profile=new ResideMenuItem(this,R.drawable.profile,"PROFILE");
        resideMenu.addMenuItem(home,ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(profile,ResideMenu.DIRECTION_LEFT);

        home.setOnClickListener(this);
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
        } else if (view == profile) {
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