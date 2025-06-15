package com.appjava.aplicativotcc.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.appjava.aplicativotcc.R;
import com.appjava.aplicativotcc.databinding.ActivityGlobalBinding;
import com.appjava.aplicativotcc.fragment.CardsFragment;
import com.appjava.aplicativotcc.fragment.Map;
import com.appjava.aplicativotcc.fragment.ProfileFragment;
import com.appjava.aplicativotcc.helper.HelperFirebase;
import com.appjava.aplicativotcc.helper.HelperMap;
import com.appjava.aplicativotcc.helper.HelperPermission;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class GlobalActivity extends AppCompatActivity {

    ActivityGlobalBinding binding;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private final String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGlobalBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_global);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linearLayoutGlobal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // INITIALIZE FIREBASE AUTHENTICATION
        authentication = HelperFirebase.getFirebaseAuthentication();

        // VALIDATE PERMISSIONS
        HelperPermission.validatePermissions(permissions, this, 1);

        replaceFragment(new CardsFragment());
        configureBottomNavigation();
    }

    private void configureBottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        enableNavigation(bottomNavigationView);
    }

    private void enableNavigation(BottomNavigationView view){
        view.setOnItemSelectedListener(item -> {
            var itemId = item.getItemId();
            if(itemId == R.id.bottomNavHome){
                replaceFragment(new CardsFragment());

                //toolbar.setTitle("Home");
            }
            if(itemId == R.id.bottomNavMap){
                replaceFragment(new Map());
                //toolbar.setTitle("Nova postagem");
            }
            if(itemId == R.id.bottomNavProfile){
                replaceFragment(new ProfileFragment());
                //toolbar.setTitle("Perfil");
            }
            if(itemId == R.id.bottomNavLogout){
                userLogoff();
            }

            return true;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int resultPermission : grantResults){
            if( resultPermission == PackageManager.PERMISSION_DENIED){
                validatePermissionAlert();
            } else if (resultPermission == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            HelperMap.time,
                            HelperMap.distance,
                            locationListener
                    );
                }
            }
        }
    }

    private void validatePermissionAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void userLogoff(){
        try{
            authentication.signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        } catch (Exception e) {
            Log.v("ex", (String) Objects.requireNonNull(e.getMessage()));
        }
    }
}