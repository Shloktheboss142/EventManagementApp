package com.fit2081.assignment1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FragmentListEvent extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ArrayList<EventItem> data = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    EventAdapter adapter = new EventAdapter();
    Gson gson = new Gson();
    Type type = new TypeToken<ArrayList<EventItem>>() {}.getType();

    public FragmentListEvent() {
        // Required empty public constructor
    }

    public static FragmentListEvent newInstance(String param1, String param2) {

        FragmentListEvent fragment = new FragmentListEvent();
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

        View view =  inflater.inflate(R.layout.fragment_list_event, container, false);

        // Get and set the recycler view
        recyclerView = view.findViewById(R.id.event_recycler);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        getDataFromSharedPreferences();

        adapter.setData(data);
        recyclerView.setAdapter(adapter);

        return view;

    }

    // Method to get data from shared preferences
    public void getDataFromSharedPreferences() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KeyStore.FILE_NAME, Context.MODE_PRIVATE);
        String savedEvents = sharedPreferences.getString(KeyStore.ALL_EVENTS, "");
        data = gson.fromJson(savedEvents,type);

    }

}