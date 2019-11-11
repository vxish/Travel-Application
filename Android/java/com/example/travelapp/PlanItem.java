package com.example.travelapp;

public class PlanItem
{
    private String mPlatform;
    private String mDestination;
    private String mDepTime, mDepStatus;
    private String mArrTime, mArrStatus;
    private String mLocation;
    private String mStops;
    private String mDuration;

    public PlanItem(String platform, String des, String dTime,
                    String aTime, String location, String stops,
                    String duration, String depTimeStatus, String arrTimeStatus)
    {
        mPlatform = platform;
        mDestination = des;
        mDepTime = dTime;
        mArrTime = aTime;
        mLocation = location;
        mStops = stops;
        mDuration = duration;
        mDepStatus = depTimeStatus;
        mArrStatus = arrTimeStatus;
    }

    public String getmPlatform() {
        return mPlatform;
    }

    public String getmDestination() {
        return mDestination;
    }

    public String getmDepTime() {
        return mDepTime;
    }

    public String getmArrTime() {
        return mArrTime;
    }

    public String getmLocation() {
        return mLocation;
    }

    public String getmDepStatus() {
        return mDepStatus;
    }

    public String getmArrStatus() {
        return mArrStatus;
    }

    public String getmStops() {
        return mStops;
    }

    public String getmDuration() {
        return mDuration;
    }
}
