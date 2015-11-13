package com.example.vl.criminalintentapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by vl on 11/12/2015.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    /*@Override
    public void onCreate(Bundle onStateBundle){
        super.onCreate(onStateBundle);
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentById(R.id.fragementContainer);

        if(fragment !=null){
            fragment = new CrimeListFragment();
            fragmentManager.beginTransaction().add( R.id.fragementContainer, fragment).commit();
        }
    }*/

    public  Fragment creatFragment(){
        return new CrimeListFragment();
    }
}
