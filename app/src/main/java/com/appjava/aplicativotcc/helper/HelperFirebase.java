package com.appjava.aplicativotcc.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HelperFirebase {

    private static DatabaseReference database;
    private static FirebaseAuth authentication;

    public static DatabaseReference getFirebaseDatabase(){
        if(database == null){
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }
    public static FirebaseAuth getFirebaseAuthentication(){

        try{
            if(authentication == null){
                authentication = FirebaseAuth.getInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authentication;
    }
}
