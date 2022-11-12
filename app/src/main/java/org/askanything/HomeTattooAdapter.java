package org.askanything;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;



public class HomeTattooAdapter extends FirebaseRecyclerAdapter<TattooModel, HomeTattooAdapter.myViewholder> {

    public HomeTattooAdapter(FirebaseRecyclerOptions<TattooModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(myViewholder holder, int position, @NonNull TattooModel model) {

            Animation animation= AnimationUtils.loadAnimation(holder.imageView.getContext(),R.anim.zoom_in);
            holder.itemView.setAnimation(animation);
            holder.name.setText(model.getName());
            Glide.with(holder.imageView.getContext()).load(model.getImageUrl()).into(holder.imageView);

            holder.itemView.setVisibility(View.VISIBLE);


            holder.imageView.setOnClickListener(view -> {
            Intent i = new Intent(holder.imageView.getContext(), CheckOutDesign.class);
            Pair[] pairs=new Pair[1];
            pairs[0]=new Pair<View,String>(holder.imageView,"transition");
            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation((Activity) holder.imageView.getContext(),
                    pairs);
            i.putExtra("name",holder.name.getText().toString().toUpperCase());
            i.putExtra("imageurl",model.getImageUrl());
            i.putExtra("price",model.getPrice());
            holder.imageView.getContext().startActivity(i,options.toBundle());

        });

    }


    @NonNull
    @Override
    public myViewholder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_tattoo_design, parent, false);
        return new myViewholder(view);
    }

    public class myViewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        @SuppressLint("ResourceAsColor")
        public myViewholder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            itemView.setVisibility(View.INVISIBLE);
            //itemView.setBackgroundColor(android.R.color.transparent);
        }
    }
}