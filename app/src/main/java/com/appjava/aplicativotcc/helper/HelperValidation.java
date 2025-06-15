package com.appjava.aplicativotcc.helper;

import com.appjava.aplicativotcc.model.ModelUser;

public class HelperValidation {

    public static boolean methodValidateSignupFields(String parameterUser, String parameterEmail, String parameterPassword){
        ModelUser modelUser;

        boolean emptyUser = false;
        boolean emptyEmail = false;
        boolean emptyPassword = false;

        if(parameterUser.isEmpty()) emptyUser = true;
        if(parameterEmail.isEmpty()) emptyEmail = true;
        if(parameterPassword.isEmpty()) emptyPassword = true;

        if(emptyUser || emptyEmail || emptyPassword){
            return false;
        }else{
            return true;
        }
    }

    public static boolean methodValidateLoginFields(String parameterEmail, String parameterPassword){
        ModelUser modelUser;

        boolean emptyEmail = false;
        boolean emptyPassword = false;

        if(parameterEmail.isEmpty()) emptyEmail = true;
        if(parameterPassword.isEmpty()) emptyPassword = true;

        if(emptyEmail || emptyPassword){
            return false;
        }else{
            return true;
        }
    }
}
