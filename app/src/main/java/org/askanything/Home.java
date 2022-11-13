package org.askanything;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends Fragment {
    RecyclerView tattoRecyclerView;
    HomeTattooAdapter homeTattooAdapter;
    DatabaseReference reference;
    ImageView home;
    EditText search;




    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home, container, false);
        reference= FirebaseDatabase.getInstance().getReference().child("Globlevariable");

        tattoRecyclerView=(RecyclerView)view.findViewById(R.id.tattoRecyclerView);
        home= (ImageView) view.findViewById(R.id.home);
        search= (EditText) view.findViewById(R.id.search);

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
        search.startAnimation(animation);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchData(String.valueOf(charSequence).toUpperCase().trim().toCharArray());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    DataSnapshot snapshot=task.getResult();
                    String bgurl=snapshot.child("homeBackground").getValue().toString();
                    //Glide.with(getActivity()).load(bgurl).into(home);

                }
            }
        });


        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        FirebaseRecyclerOptions<TattooModel> options = new FirebaseRecyclerOptions.Builder<TattooModel>()
                .setQuery(reference.child("Tattoos"), TattooModel.class).build();
        homeTattooAdapter = new HomeTattooAdapter(options);
        homeTattooAdapter.startListening();
        homeTattooAdapter.notifyDataSetChanged();

        tattoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        tattoRecyclerView.setAdapter(homeTattooAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    private void searchData(char[] charSequence) {

        FirebaseRecyclerOptions<TattooModel> searchOption = new FirebaseRecyclerOptions.Builder<TattooModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Tattoos").orderByKey().startAt(String.valueOf(charSequence).trim())
                        .endAt(charSequence+"/uf8ff"), TattooModel.class).build();

        homeTattooAdapter = new HomeTattooAdapter(searchOption);
        homeTattooAdapter.startListening();
        tattoRecyclerView.setAdapter(homeTattooAdapter);
        homeTattooAdapter.notifyDataSetChanged();

    }
}