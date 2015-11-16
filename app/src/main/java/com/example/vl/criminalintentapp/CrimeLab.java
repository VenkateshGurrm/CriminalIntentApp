package com.example.vl.criminalintentapp;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by vl on 11/12/2015.
 */
public class CrimeLab {
    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";
    private ArrayList<Crime> mCrimes;
    private CriminalIntentJSONSerializer mSerializer ;
    private static CrimeLab sCrimeLab;
    private Context mAppContext;

    private static CrimeLab instance = null;

    public ArrayList<Crime> getmCrimes() {
        return mCrimes;
    }

    public void setmCrimes(ArrayList<Crime> mCrimes) {
        this.mCrimes = mCrimes;

    }

    public Crime getCrime(UUID id) {
        for (Crime c : mCrimes) {
            if (c.getmId().equals(id))
                return c;
        }
        return null;
    }

    private CrimeLab(Context appContext){
        mAppContext = appContext;
       /* mCrimes = new ArrayList<Crime>();
        mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);
       */
        mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);

        try {
            mCrimes = mSerializer.loadCrimes();
        } catch (Exception e) {
            mCrimes = new ArrayList<Crime>();
            Log.e(TAG, "Error loading crimes: ", e);
        }

        /*Crime c = new Crime();
        c.setmTitle("Crime #1" );
        c.setmSolved(false); // Every other one
        mCrimes.add(c);*/

        /*for (int i = 0; i < 100; i++) {
            Crime c = new Crime();
            c.setmTitle("Crime #" + i);
            c.setmSolved(i % 2 == 0); // Every other one
            mCrimes.add(c);
        }*/
    }

    public static CrimeLab getInstance(Context c){
        if(instance==null){
            instance = new CrimeLab(c.getApplicationContext());
        }
        return instance;
    }

    public void addCrime(Crime c) {
        mCrimes.add(c);
    }
    public void deleteCrime(Crime c) {
        mCrimes.remove(c);
    }

    public boolean saveCrimes() {
        try {
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "crimes saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving crimes: ", e);
            return false;
        }
    }
}
