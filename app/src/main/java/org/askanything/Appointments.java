package org.askanything;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Appointments extends Fragment {

    RecyclerView appointmentsRecyclerview;
    AppointmentAdapter appointmentAdapter;
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_appointments, container, false);

        appointmentsRecyclerview=(RecyclerView)view.findViewById(R.id.appointmentsRecyclerview);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim()).child("My Appointments");
        reference.keepSynced(true);
        FirebaseRecyclerOptions<AppointmentModel> options = new FirebaseRecyclerOptions.Builder<AppointmentModel>()
                .setQuery(reference, AppointmentModel.class).build();
        appointmentAdapter = new AppointmentAdapter(options);
        appointmentAdapter.startListening();
        appointmentAdapter.notifyDataSetChanged();

        appointmentsRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        appointmentsRecyclerview.setAdapter(appointmentAdapter);

        return view;
    }
}