package com.example.eventoadmin.common;


import com.google.firebase.auth.FirebaseUser;

public class Common {

    public static FirebaseUser currentUser;
    public static final String NOTIFY = "Notify";
    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    public static String convertCodeToStatus(String code){

        if (code.equals("0"))
            return "organized";
        else if (code.equals("1"))
            return "2 days to go";
        else if (code.equals("2"))
            return "1 day to go";
        else if (code.equals("3"))
            return "Today";
        else if (code.equals("4"))
            return "postponed";
        else
            return "preponed";
    }

}
