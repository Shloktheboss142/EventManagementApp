package com.fit2081.assignment1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FragmentListCategory extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ArrayList<EventCategoryItem> data = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    EventCategoryAdapter adapter = new EventCategoryAdapter();
    Gson gson = new Gson();
    Type type = new TypeToken<ArrayList<EventCategoryItem>>() {}.getType();


    public FragmentListCategory() {
        // Required empty public constructor
    }

    public static FragmentListCategory newInstance(String param1, String param2) {

        FragmentListCategory fragment = new FragmentListCategory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_list_category, container, false);

        // Get and set the recycler view
        recyclerView = view.findViewById(R.id.category_recycler);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        getDataFromSharedPreferences();

        adapter.setData(data);
        recyclerView.setAdapter(adapter);

        return view;

    }

    // Method to get the data from shared preferences
    public void getDataFromSharedPreferences() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KeyStore.FILE_NAME, Context.MODE_PRIVATE);
        String savedEventCategories = sharedPreferences.getString(KeyStore.ALL_EVENT_CATEGORIES, "");
        data = gson.fromJson(savedEventCategories,type);

    }

}