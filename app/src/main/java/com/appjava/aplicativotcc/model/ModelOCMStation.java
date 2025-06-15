package com.appjava.aplicativotcc.model;

import com.google.gson.annotations.SerializedName;

public class ModelOCMStation {
    @SerializedName("ID")
    public int ID_OCM;

    @SerializedName("AddressInfo")
    public AddressInfo AddressInfo;



    public static class AddressInfo {
        @SerializedName("Title")
        public String TitleStation;

        @SerializedName("AddressLine1")
        public String AddressLine1;

        @SerializedName("AddressLine2")
        public String AddressLine2;

        @SerializedName("Town")
        public String Town;

        @SerializedName("StateOrProvince")
        public String StateOrProvince;

        @SerializedName("Postcode")
        public String Postcode;

        @SerializedName("ContactTelephone1")
        public String ContactTelephone1;

        @SerializedName("ContactTelephone2")
        public String ContactTelephone2;

        @SerializedName("ContactEmail")
        public String ContactEmail;

        @SerializedName("Latitude")
        public double Latitude;

        @SerializedName("Longitude")
        public double Longitude;


        @SerializedName("Country")
        public Country Country;

        public static class Country {

            @SerializedName("ID")
            public int ID_Country;

            @SerializedName("Title")
            public String TitleCountry;

        }
    }
}
