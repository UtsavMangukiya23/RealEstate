package com.example.realestate.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.realestate.adapters.AdapterProperty;
import com.example.realestate.databinding.FragmentHomeBinding;
import com.example.realestate.models.ModelProperty;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private static final String TAG = "HOME_TAG";

    private Context mContext;

    private ArrayList<ModelProperty> propertyArrayList;
    private AdapterProperty adapterProperty;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadProperties();
    }

    private void loadProperties() {
        Log.d(TAG, "loadProperties: ");

        //init propertyArrayList before starting adding data into it
        propertyArrayList = new ArrayList<>();

        //Firebase DB listener to Load Properties
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Properties");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clear propertyArrayList each time starting adding data into it
                propertyArrayList.clear();

                //load Properties list
                for (DataSnapshot ds : snapshot.getChildren()){
                    //Prepare ModelProperty with all data from Firebase DB
                    ModelProperty modelProperty = ds.getValue(ModelProperty.class);
                    //add model to the propertyArrayList
                    propertyArrayList.add(modelProperty);
                }
                //setup adapter and set to recyclerview
                adapterProperty = new AdapterProperty(mContext, propertyArrayList);
                binding.propertiesRv.setAdapter(adapterProperty);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}