package com.example.travelapp;

public class NearbyItem
{
    private String mType;
    private String mName;
    private String mDesc;
    private String mCode;

    public NearbyItem(String type, String name, String desc, String code){
        mName = name;
        mType = type;
        mDesc = desc;
        mCode = code;
    }

    public String getmType() {
        return mType;
    }

    public String getmName() {
        return mName;
    }

    public String getmDesc() {
        return mDesc;
    }

    public String getmCode(){
        return mCode;
    }
}
