package com.appjava.aplicativotcc.activity;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appjava.aplicativotcc.R;
import com.appjava.aplicativotcc.helper.HelperMap;
import com.appjava.aplicativotcc.helper.HelperPermission;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.appjava.aplicativotcc.databinding.ActivityMapBinding;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapBinding binding;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private final String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // VALIDATE PERMISSIONS
        HelperPermission.validatePermissions(permissions, this, 1);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setMapType(GoogleMap.MAP_TYPE_NONE); // 0
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); // 1
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); // 2
        //mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); // 3
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); // 4

        // LOCATION MANAGER
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("Location", "onLocationChanged: " + location);

                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                mMap.clear();
                LatLng userLocation = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Minha posição").snippet("Você está aqui"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, HelperMap.zoom));
            }
        };

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