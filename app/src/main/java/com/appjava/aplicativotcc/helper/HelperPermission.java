package com.appjava.aplicativotcc.helper;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class HelperPermission {
    public static boolean validatePermissions(String[] permissoes, Activity activity, int requestCode){

        List<String> listPermission = new ArrayList<>();

        for ( String permission : permissoes ){
            boolean havePermission = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
            if ( !havePermission ) listPermission.add(permission);
        }

        if ( listPermission.isEmpty() ) return true;
        String[] newPermission = new String[ listPermission.size() ];
        listPermission.toArray( newPermission );

        ActivityCompat.requestPermissions(activity, newPermission, requestCode );

//        if (Build.VERSION.SDK_INT >= 23 ){
//        }
        return true;
    }
}
