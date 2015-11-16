package com.example.vl.criminalintentapp;

import java.util.Date;
import java.util.UUID;

/**
 * Created by vl on 11/11/2015.
 */
public class Crime {

    Crime(){
        mDate = new Date();
        mId = UUID.randomUUID();
    }
    private UUID mId;
    private Date mDate;

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    private boolean mSolved;

    public UUID getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    private String mTitle;

    @Override
    public String toString() {
        return mTitle;
    }

}
