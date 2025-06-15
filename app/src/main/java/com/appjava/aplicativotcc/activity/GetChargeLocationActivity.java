package com.appjava.aplicativotcc.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.appjava.aplicativotcc.R;
import com.appjava.aplicativotcc.model.ModelChargeLocation;
import com.appjava.aplicativotcc.model.ModelOCMStation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GetChargeLocationActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollViewCards), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // GET CHARGE LOCATIONS
        ModelChargeLocation.getChargeLocationFromAPI(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.v("ex", (String) Objects.requireNonNull(e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<ModelOCMStation>>() {}.getType();
                    List<ModelOCMStation> stationList = gson.fromJson(json, listType);

                    // Salvar no Firestore
                    firestoreSaveChargeLocation(stationList);
                }
            }
        });
    }

    private void firestoreSaveChargeLocation(List<ModelOCMStation> stationList) {
        db = FirebaseFirestore.getInstance();

        for(ModelOCMStation station : stationList){
            DocumentReference docRef = db.collection("location_list").document(String.valueOf(station.ID_OCM));

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documento = task.getResult();
                        if(documento.exists()){
                            Log.d("Firestore", "Documento já existe!");
                        }else{
                            // NEW LOCATION
                            Map<String, Object> data = new HashMap<>();
                            data.put("01_ID_OCM", station.ID_OCM);
                            data.put("02_TitleStation", station.AddressInfo.TitleStation);
                            data.put("03_AddressLine1", station.AddressInfo.AddressLine1);
                            data.put("04_AddressLine2", station.AddressInfo.AddressLine2);
                            data.put("05_Town", station.AddressInfo.Town);
                            data.put("06_StateOrProvince", station.AddressInfo.StateOrProvince);
                            data.put("07_Postcode", station.AddressInfo.Postcode);
                            data.put("08_ContactTelephone1", station.AddressInfo.ContactTelephone1);
                            data.put("09_ContactTelephone2", station.AddressInfo.ContactTelephone2);
                            data.put("10_ContactEmail", station.AddressInfo.ContactEmail);


                            data.put("11_Country_ID", station.AddressInfo.Country.ID_Country);
                            data.put("12_Country_Title", station.AddressInfo.Country.TitleCountry);

                            data.put("98_Latitude", station.AddressInfo.Latitude);
                            data.put("99_Longitude", station.AddressInfo.Longitude);


                            docRef.set(data).addOnSuccessListener(unused -> {
                                Log.d("Firestore", "Salvo: " + station.ID_OCM);

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Firestore", "Erro ao salvar", e);
                                }
                            });
                        }
                    }
                }
            });
        }
    }
}