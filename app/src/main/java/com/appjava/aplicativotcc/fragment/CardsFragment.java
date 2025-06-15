package com.appjava.aplicativotcc.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appjava.aplicativotcc.R;
import com.appjava.aplicativotcc.activity.MapActivity;

public class CardsFragment extends Fragment {

    private CardView cardViewShowMap;
    private CardView cardViewRequestUpdate;
    private CardView cardViewRequestInclusion;
    private CardView cardViewRequestDeletion;

    public CardsFragment() {
    }

//    public static CardsFragment newInstance(String param1, String param2) {
//        CardsFragment fragment = new CardsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cards, container, false);
        initializeComponentsCards(rootView);

        cardViewShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                startActivity(intent);
            }
        });

        return inflater.inflate(R.layout.fragment_cards, container, false);
    }

    public void initializeComponentsCards(View view){
        cardViewShowMap = view.findViewById(R.id.cardViewShowMap);
        cardViewRequestUpdate = view.findViewById(R.id.cardViewRequestUpdate);
        cardViewRequestInclusion = view.findViewById(R.id.cardViewRequestInclusion);
        cardViewRequestDeletion = view.findViewById(R.id.cardViewRequestDeletion);
        //textInputEditarPerfilEmail.setFocusable(false);
    }
}