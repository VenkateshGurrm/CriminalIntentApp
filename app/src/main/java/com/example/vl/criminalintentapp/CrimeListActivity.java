package com.example.vl.criminalintentapp;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

/**
 * Created by vl on 11/12/2015.
 */
public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks{

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

    @Override
    protected int getLayoutID() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if(findViewById(R.id.detailFragementContainer) ==null){
            Intent i = new Intent(this, CrimePagerActivity.class);
            i.putExtra(CrimeFragment.CRIME_ID, crime.getmId());
            startActivity(i);
        }else{

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment old = fm.findFragmentById(R.id.detailFragementContainer);
            Fragment newFrg= CrimeFragment.newInstance(crime.getmId());

            if(old != null)
                ft.remove(old);

            ft.add(R.id.detailFragementContainer, newFrg);
            ft.commit();


        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        FragmentManager fm = getSupportFragmentManager();
        CrimeListFragment listFragment = (CrimeListFragment) fm.findFragmentById(R.id.fragementContainer);
        listFragment.updateUI();
    }
}
