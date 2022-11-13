package org.askanything;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends Fragment {
    CircleImageView proimage;
    ImageView imageButton;
    RatingBar ratingBar;
    Uri imageUri;
    TextView namePro;
    TextView emailPro;
    TextView subrating;
    TextView signout;
    TextView updateprofile;
    String picurl="";
    SwipeRefreshLayout swipe;
    private StorageReference storageRefrence;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        proimage=view.findViewById(R.id.proimage);
        imageButton=view.findViewById(R.id.imageButton);
        namePro=view.findViewById(R.id.namepro);
        emailPro=view.findViewById(R.id.emailpro);
        subrating=view.findViewById(R.id.sabratingepro);
        signout=view.findViewById(R.id.signout);
        updateprofile=view.findViewById(R.id.updateprofile);
        swipe=view.findViewById(R.id.swipe);
        ratingBar=view.findViewById(R.id.ratingBar);


        mAuth=FirebaseAuth.getInstance();
        storageRefrence = FirebaseStorage.getInstance().getReference();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid())
                .child("Personal Data");
        reference.keepSynced(true);




        getName();
        getRate();



        proimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.profileview);
                dialog.setCancelable(true);
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ImageView imageView1=dialog.findViewById(R.id.dimageView);
                if (picurl.equals("")||picurl.toString().isEmpty()||picurl==null){
                    imageView1.setImageResource(R.drawable.icon);
                }
                else{
                    Glide.with(getActivity()).load(picurl).into(imageView1);

                }


            }

        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getName();
                swipe.setRefreshing(false);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select();
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog;
                AlertDialog.Builder builder;
                builder=new AlertDialog.Builder(getActivity());
                LayoutInflater layoutInflater=getActivity().getLayoutInflater();
                builder.setView(layoutInflater.inflate(R.layout.customlogoutdialog,null));
                builder.setCancelable(false);
                alertDialog= builder.create();
                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                Button cancel=alertDialog.findViewById(R.id.sdc);
                Button logout=alertDialog.findViewById(R.id.sdy);

                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAuth.signOut();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        subrating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRating();
            }
        });



        return view;
    }

    private void getRate() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Ratings");
        reference.keepSynced(true);
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        String rating = String.valueOf(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue());
                        ratingBar.setRating(Float.parseFloat(rating));
                    }
                }
            }
        });
    }

    private void submitRating() {
        float rating=ratingBar.getRating();
        if (rating==0.0){
            Snackbar.make(getActivity().findViewById(android.R.id.content),(CharSequence) "😥 Please provide Rating to submit.", Snackbar.LENGTH_LONG).setBackgroundTint(Color.WHITE).show();
        }
        else {
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Ratings").
                    child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.setValue(rating);
            Snackbar.make(getActivity().findViewById(android.R.id.content),(CharSequence) "❤ Thank You ❤", Snackbar.LENGTH_LONG).setBackgroundTint(Color.WHITE).show();

        }

    }

    //get nAme method
    private void getName() {

        reference.keepSynced(true);
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {

                        DataSnapshot dataSnapshot = task.getResult();
                        String name1 = String.valueOf(dataSnapshot.child("name").getValue());
                        String email1 = String.valueOf(dataSnapshot.child("email").getValue());
                        //String picurl = String.valueOf(dataSnapshot.child("profilepic").getValue());

                        if (name1 != null || !name1.isEmpty() || !name1.equals("")) {
                            namePro.setText(name1);

                        }
                        else {
                            namePro.setText("Hey User");


                        }
                        if (email1 != null || !email1.isEmpty() || !email1.equals("")) {
                            emailPro.setText(email1);

                        }
                        else {
                            emailPro.setText("abc@gmail.com");


                        }

                        boolean data;
                        data=dataSnapshot.child("picurl").exists();
                        if (data){
                            picurl=String.valueOf(dataSnapshot.child("picurl").getValue());

                            Glide.with(getActivity()).load(Uri.parse(picurl)).into(proimage);
                        }
                        else if (picurl == null && picurl.isEmpty() && picurl.equals("")) {
                            proimage.setImageResource(R.drawable.icon);

                        }
                        else {
                            proimage.setImageResource(R.drawable.icon);

                        }

                    }
                    else {
                        namePro.setText("Hey User");
                        emailPro.setText("abc@gmail.com");
                        proimage.setImageResource(R.drawable.icon);

                    }
                }
                else {
                    namePro.setText("Hey User");
                    emailPro.setText("abc@gmail.com");
                    proimage.setImageResource(R.drawable.icon);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //select image and upload
    private void select() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(intent,1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1 && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploaUserdPicture();
        }
    }

    private void uploaUserdPicture() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Uploading...........");
        pd.show();
        final StorageReference fileReference = storageRefrence.child("user/" + this.mAuth.getCurrentUser().getUid() + ".jpg");
        fileReference.putFile(this.imageUri).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    public void onSuccess(Uri uri) {
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(mAuth.getCurrentUser().getUid()).child("Personal Data")
                                .child("picurl").setValue(String.valueOf(uri));

                        Glide.with(getActivity()).load(uri).into(proimage);
                        Glide.with(getActivity()).load(uri).into(MainActivity.profileimg);

                        //Glide.with(Home.this).load(uri).into(picd);
                        //refresh();


                    }
                });
                pd.dismiss();
                Snackbar.make(getActivity().findViewById(android.R.id.content), (CharSequence) "Image Uploaded", Snackbar.LENGTH_LONG).setBackgroundTint(Color.RED).show();
            }
        }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
            public void onFailure(Exception e) {
                pd.dismiss();
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener((OnProgressListener) new OnProgressListener<UploadTask.TaskSnapshot>() {
            public void onProgress(UploadTask.TaskSnapshot snapshot) {
                double bytesTransferRED = (double) snapshot.getBytesTransferred();
                Double.isNaN(bytesTransferRED);
                double totalByteCount = (double) snapshot.getTotalByteCount();
                Double.isNaN(totalByteCount);
                pd.setMessage(((int) ((100.0d * bytesTransferRED) / totalByteCount)) + " %"+" Uploaded");
                pd.setProgress(((int) ((100.0d * bytesTransferRED) / totalByteCount)));
            }
        });
    }
}