package com.example.vl.criminalintentapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;


/**
 * Created by vl on 11/12/2015.
 */
public abstract  class SingleFragmentActivity extends AppCompatActivity  {

    public abstract  Fragment creatFragment();

    @Override
    public void onCreate(Bundle onStateBundle){
        super.onCreate(onStateBundle);
            setContentView(R.layout.activity_fragment);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragementContainer);
        if(fragment ==null){
            fragment = creatFragment();
            fragmentManager.beginTransaction().add( R.id.fragementContainer, fragment).commit();
        }

    }
}
