package org.askanything;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ProfileDialog {
    private final Uri uri;
    private final Activity activity;


    public ProfileDialog(Uri uri, Activity activity) {
        this.uri=uri;
        this.activity = activity;
    }

    void showProfile(){
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.profileview);
        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ImageView imageView=(ImageView) dialog.findViewById(R.id.dimageView);
        if (uri.equals("")||uri.toString().isEmpty()||uri==null){
            imageView.setImageResource(R.drawable.icon);
        }
        else{
            Glide.with(activity).load(uri).into(imageView);
        }


    }
}
