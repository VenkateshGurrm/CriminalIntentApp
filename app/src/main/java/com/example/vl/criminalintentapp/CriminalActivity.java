package com.example.vl.criminalintentapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.UUID;

public class CriminalActivity extends SingleFragmentActivity {

    @Override
    public Fragment creatFragment() {

        UUID uuid = (UUID) getIntent().getSerializableExtra(CrimeFragment.CRIME_ID);
        return CrimeFragment.newInstance(uuid);
    }



    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        *//*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*//*

        FragmentManager fm = getSupportFragmentManager();
        Fragment  fragment = fm.findFragmentById(R.id.fragementContainer);

        if(fragment == null){
            fragment = new CrimeFragment();
            fm.beginTransaction().add(R.id.fragementContainer, fragment).commit();
        }
    }*/

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_criminal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


}
