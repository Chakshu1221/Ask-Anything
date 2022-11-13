package org.askanything;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class AppointmentAdapter extends FirebaseRecyclerAdapter<AppointmentModel, AppointmentAdapter.myViewholder> {

    public AppointmentAdapter(FirebaseRecyclerOptions<AppointmentModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(myViewholder holder, int position, @NonNull AppointmentModel model) {

            Animation animation= AnimationUtils.loadAnimation(holder.app_name.getContext(),R.anim.bottom_to_up);
            holder.itemView.setAnimation(animation);
            holder.app_name.setText(model.getTattoName());
            holder.app_date.setText("Date : "+model.getDate());
            holder.app_time.setText("Time : "+model.getTime());
            holder.app_status.setText("Appointment Status : "+model.getStatus());
            holder.app_payStatus.setText("Payment Status : "+model.getPaymentStatus());
            Glide.with(holder.app_imageView.getContext()).load(model.getImageUrl()).into(holder.app_imageView);
            holder.itemView.setVisibility(View.VISIBLE);
            if (model.getStatus().equals("Accepted")){
                holder.app_status.setTextColor(Color.GREEN);
            }

           /* holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(holder.app_time.getContext(), CheckOutDesign.class);
            Pair[] pairs=new Pair[1];
            pairs[0]=new Pair<View,String>(holder.app_imageView,"transition");
            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation((Activity) holder.app_date.getContext(),
                    pairs);
            i.putExtra("name",holder.app_time.getText().toString().toUpperCase());
            i.putExtra("imageurl",model.getImageUrl());
            i.putExtra("price",model.getPrice());
            holder.app_date.getContext().startActivity(i,options.toBundle());

        });*/

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.app_imageView.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        holder.confirmapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.app_imageView.getContext(), "Confirmed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @NonNull
    @Override
    public myViewholder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_appointment_design, parent, false);
        return new myViewholder(view);
    }

    public class myViewholder extends RecyclerView.ViewHolder {
        ImageView app_imageView;
        TextView app_name;
        TextView app_date;
        TextView app_time;
        Button del,confirmapp;
        TextView app_payStatus;
        TextView app_status;
        @SuppressLint("ResourceAsColor")
        public myViewholder(View itemView) {
            super(itemView);
            app_imageView = itemView.findViewById(R.id.app_image);
            app_name = itemView.findViewById(R.id.app_name);
            app_date = itemView.findViewById(R.id.app_date);
            app_time = itemView.findViewById(R.id.app_time);
            app_payStatus = itemView.findViewById(R.id.app_paystatus);
            app_status = itemView.findViewById(R.id.app_status);
            del = itemView.findViewById(R.id.del);
            confirmapp = itemView.findViewById(R.id.confirmapp);
            itemView.setVisibility(View.INVISIBLE);
            //itemView.setBackgroundColor(android.R.color.transparent);
        }
    }
}